package com.android.systemui.util.settings;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.Settings;

public class GlobalSettingsImpl implements GlobalSettings {
    public final ContentResolver mContentResolver;

    public GlobalSettingsImpl(ContentResolver contentResolver) {
        this.mContentResolver = contentResolver;
    }

    public ContentResolver getContentResolver() {
        return this.mContentResolver;
    }

    public Uri getUriFor(String str) {
        return Settings.Global.getUriFor(str);
    }

    public String getStringForUser(String str, int i) {
        return Settings.Global.getStringForUser(this.mContentResolver, str, i);
    }

    public boolean putStringForUser(String str, String str2, int i) {
        return Settings.Global.putStringForUser(this.mContentResolver, str, str2, i);
    }
}
