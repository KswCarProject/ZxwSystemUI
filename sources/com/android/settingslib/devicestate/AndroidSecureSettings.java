package com.android.settingslib.devicestate;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.provider.Settings;

public class AndroidSecureSettings implements SecureSettings {
    public final ContentResolver mContentResolver;

    public AndroidSecureSettings(ContentResolver contentResolver) {
        this.mContentResolver = contentResolver;
    }

    public void putStringForUser(String str, String str2, int i) {
        Settings.Secure.putStringForUser(this.mContentResolver, str, str2, i);
    }

    public String getStringForUser(String str, int i) {
        return Settings.Secure.getStringForUser(this.mContentResolver, str, i);
    }

    public void registerContentObserver(String str, boolean z, ContentObserver contentObserver, int i) {
        this.mContentResolver.registerContentObserver(Settings.Secure.getUriFor(str), z, contentObserver, i);
    }
}
