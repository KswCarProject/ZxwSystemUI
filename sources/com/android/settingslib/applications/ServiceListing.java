package com.android.settingslib.applications;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.util.Slog;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ServiceListing {
    public final boolean mAddDeviceLockedFlags;
    public final List<Callback> mCallbacks;
    public final ContentResolver mContentResolver;
    public final Context mContext;
    public final HashSet<ComponentName> mEnabledServices;
    public final String mIntentAction;
    public boolean mListening;
    public final String mNoun;
    public final BroadcastReceiver mPackageReceiver;
    public final String mPermission;
    public final List<ServiceInfo> mServices;
    public final String mSetting;
    public final ContentObserver mSettingsObserver;
    public final String mTag;

    public interface Callback {
        void onServicesReloaded(List<ServiceInfo> list);
    }

    public ServiceListing(Context context, String str, String str2, String str3, String str4, String str5, boolean z) {
        this.mEnabledServices = new HashSet<>();
        this.mServices = new ArrayList();
        this.mCallbacks = new ArrayList();
        this.mSettingsObserver = new ContentObserver(new Handler()) {
            public void onChange(boolean z, Uri uri) {
                ServiceListing.this.reload();
            }
        };
        this.mPackageReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                ServiceListing.this.reload();
            }
        };
        this.mContentResolver = context.getContentResolver();
        this.mContext = context;
        this.mTag = str;
        this.mSetting = str2;
        this.mIntentAction = str3;
        this.mPermission = str4;
        this.mNoun = str5;
        this.mAddDeviceLockedFlags = z;
    }

    public void addCallback(Callback callback) {
        this.mCallbacks.add(callback);
    }

    public void setListening(boolean z) {
        if (this.mListening != z) {
            this.mListening = z;
            if (z) {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
                intentFilter.addAction("android.intent.action.PACKAGE_CHANGED");
                intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
                intentFilter.addAction("android.intent.action.PACKAGE_REPLACED");
                intentFilter.addDataScheme("package");
                this.mContext.registerReceiver(this.mPackageReceiver, intentFilter);
                this.mContentResolver.registerContentObserver(Settings.Secure.getUriFor(this.mSetting), false, this.mSettingsObserver);
                return;
            }
            this.mContext.unregisterReceiver(this.mPackageReceiver);
            this.mContentResolver.unregisterContentObserver(this.mSettingsObserver);
        }
    }

    public final void loadEnabledServices() {
        this.mEnabledServices.clear();
        String string = Settings.Secure.getString(this.mContentResolver, this.mSetting);
        if (string != null && !"".equals(string)) {
            for (String unflattenFromString : string.split(":")) {
                ComponentName unflattenFromString2 = ComponentName.unflattenFromString(unflattenFromString);
                if (unflattenFromString2 != null) {
                    this.mEnabledServices.add(unflattenFromString2);
                }
            }
        }
    }

    public void reload() {
        loadEnabledServices();
        this.mServices.clear();
        for (ResolveInfo resolveInfo : this.mContext.getPackageManager().queryIntentServicesAsUser(new Intent(this.mIntentAction), this.mAddDeviceLockedFlags ? 786564 : 132, ActivityManager.getCurrentUser())) {
            ServiceInfo serviceInfo = resolveInfo.serviceInfo;
            if (!this.mPermission.equals(serviceInfo.permission)) {
                String str = this.mTag;
                Slog.w(str, "Skipping " + this.mNoun + " service " + serviceInfo.packageName + "/" + serviceInfo.name + ": it does not require the permission " + this.mPermission);
            } else {
                this.mServices.add(serviceInfo);
            }
        }
        for (Callback onServicesReloaded : this.mCallbacks) {
            onServicesReloaded.onServicesReloaded(this.mServices);
        }
    }

    public static class Builder {
        public boolean mAddDeviceLockedFlags = false;
        public final Context mContext;
        public String mIntentAction;
        public String mNoun;
        public String mPermission;
        public String mSetting;
        public String mTag;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setTag(String str) {
            this.mTag = str;
            return this;
        }

        public Builder setSetting(String str) {
            this.mSetting = str;
            return this;
        }

        public Builder setIntentAction(String str) {
            this.mIntentAction = str;
            return this;
        }

        public Builder setPermission(String str) {
            this.mPermission = str;
            return this;
        }

        public Builder setNoun(String str) {
            this.mNoun = str;
            return this;
        }

        public Builder setAddDeviceLockedFlags(boolean z) {
            this.mAddDeviceLockedFlags = z;
            return this;
        }

        public ServiceListing build() {
            return new ServiceListing(this.mContext, this.mTag, this.mSetting, this.mIntentAction, this.mPermission, this.mNoun, this.mAddDeviceLockedFlags);
        }
    }
}
