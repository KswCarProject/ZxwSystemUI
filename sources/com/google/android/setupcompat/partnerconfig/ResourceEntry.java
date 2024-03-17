package com.google.android.setupcompat.partnerconfig;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

public final class ResourceEntry {
    public static final String KEY_FALLBACK_CONFIG = "fallbackConfig";
    public static final String KEY_PACKAGE_NAME = "packageName";
    public static final String KEY_RESOURCE_ID = "resourceId";
    public static final String KEY_RESOURCE_NAME = "resourceName";
    public static final String TAG = "ResourceEntry";
    public final String packageName;
    public final int resourceId;
    public final String resourceName;
    public final Resources resources;

    public static ResourceEntry fromBundle(Context context, Bundle bundle) {
        if (bundle.containsKey(KEY_PACKAGE_NAME) && bundle.containsKey(KEY_RESOURCE_NAME) && bundle.containsKey(KEY_RESOURCE_ID)) {
            String string = bundle.getString(KEY_PACKAGE_NAME);
            String string2 = bundle.getString(KEY_RESOURCE_NAME);
            try {
                return new ResourceEntry(string, string2, bundle.getInt(KEY_RESOURCE_ID), getResourcesByPackageName(context, string));
            } catch (PackageManager.NameNotFoundException unused) {
                Bundle bundle2 = bundle.getBundle("fallbackConfig");
                if (bundle2 != null) {
                    String str = TAG;
                    Log.w(str, string + " not found, " + string2 + " fallback to default value");
                    return fromBundle(context, bundle2);
                }
            }
        }
        return null;
    }

    public ResourceEntry(String str, String str2, int i, Resources resources2) {
        this.packageName = str;
        this.resourceName = str2;
        this.resourceId = i;
        this.resources = resources2;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public String getResourceName() {
        return this.resourceName;
    }

    public int getResourceId() {
        return this.resourceId;
    }

    public Resources getResources() {
        return this.resources;
    }

    public static Resources getResourcesByPackageName(Context context, String str) throws PackageManager.NameNotFoundException {
        PackageManager packageManager = context.getPackageManager();
        return packageManager.getResourcesForApplication(packageManager.getApplicationInfo(str, 512));
    }
}
