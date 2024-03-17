package com.android.wifitrackerlib;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.res.Resources;
import android.os.UserManager;
import android.util.ArraySet;
import android.util.Log;
import java.util.Set;

public class WifiTrackerInjector {
    public static Context mContext;
    public static boolean mGbkSsidSupported;
    public static Resources mWifiRes;
    public static Context mWifiResContext;
    public final DevicePolicyManager mDevicePolicyManager;
    public final boolean mIsDemoMode;
    public final Set<String> mNoAttributionAnnotationPackages = new ArraySet();
    public final UserManager mUserManager;

    public static boolean isGbkSsidSupported() {
        return mGbkSsidSupported;
    }

    public final void initializeWifiRes() {
        if (mWifiRes == null) {
            try {
                Context createPackageContext = mContext.createPackageContext("com.android.wifi.resources", 3);
                mWifiResContext = createPackageContext;
                mWifiRes = createPackageContext.getResources();
                mGbkSsidSupported = mWifiRes.getBoolean(getWifiResId("bool", "config_vendor_wifi_gbk_ssid_supported"));
            } catch (Exception e) {
                Log.e("WifiTrackerInjector", "exception in createPackageContext: " + e);
                throw new RuntimeException(e);
            }
        }
    }

    public final int getWifiResId(String str, String str2) {
        Resources resources = mWifiRes;
        if (resources != null) {
            return resources.getIdentifier(str2, str, "com.android.wifi.resources");
        }
        Log.e("WifiTrackerInjector", "no WIFI resources, fail to get " + str + "." + str2);
        return -1;
    }

    public WifiTrackerInjector(Context context) {
        this.mIsDemoMode = NonSdkApiWrapper.isDemoMode(context);
        mContext = context;
        this.mUserManager = (UserManager) context.getSystemService(UserManager.class);
        this.mDevicePolicyManager = (DevicePolicyManager) context.getSystemService(DevicePolicyManager.class);
        String[] split = context.getString(R$string.wifitrackerlib_no_attribution_annotation_packages).split(",");
        for (String add : split) {
            this.mNoAttributionAnnotationPackages.add(add);
        }
        initializeWifiRes();
    }

    public UserManager getUserManager() {
        return this.mUserManager;
    }

    public DevicePolicyManager getDevicePolicyManager() {
        return this.mDevicePolicyManager;
    }

    public Set<String> getNoAttributionAnnotationPackages() {
        return this.mNoAttributionAnnotationPackages;
    }
}
