package com.android.systemui.util.settings;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.Uri;

public interface SettingsProxy {
    ContentResolver getContentResolver();

    String getStringForUser(String str, int i);

    Uri getUriFor(String str);

    boolean putStringForUser(String str, String str2, int i);

    boolean putStringForUser(String str, String str2, String str3, boolean z, int i, boolean z2);

    int getUserId() {
        return getContentResolver().getUserId();
    }

    void registerContentObserver(String str, ContentObserver contentObserver) {
        registerContentObserver(getUriFor(str), contentObserver);
    }

    void registerContentObserver(Uri uri, ContentObserver contentObserver) {
        registerContentObserverForUser(uri, contentObserver, getUserId());
    }

    void registerContentObserver(Uri uri, boolean z, ContentObserver contentObserver) {
        registerContentObserverForUser(uri, z, contentObserver, getUserId());
    }

    void registerContentObserverForUser(String str, ContentObserver contentObserver, int i) {
        registerContentObserverForUser(getUriFor(str), contentObserver, i);
    }

    void registerContentObserverForUser(Uri uri, ContentObserver contentObserver, int i) {
        registerContentObserverForUser(uri, false, contentObserver, i);
    }

    void registerContentObserverForUser(String str, boolean z, ContentObserver contentObserver, int i) {
        registerContentObserverForUser(getUriFor(str), z, contentObserver, i);
    }

    void registerContentObserverForUser(Uri uri, boolean z, ContentObserver contentObserver, int i) {
        getContentResolver().registerContentObserver(uri, z, contentObserver, i);
    }

    void unregisterContentObserver(ContentObserver contentObserver) {
        getContentResolver().unregisterContentObserver(contentObserver);
    }

    String getString(String str) {
        return getStringForUser(str, getUserId());
    }

    int getInt(String str, int i) {
        return getIntForUser(str, i, getUserId());
    }

    int getIntForUser(String str, int i, int i2) {
        String stringForUser = getStringForUser(str, i2);
        if (stringForUser == null) {
            return i;
        }
        try {
            return Integer.parseInt(stringForUser);
        } catch (NumberFormatException unused) {
            return i;
        }
    }

    boolean putInt(String str, int i) {
        return putIntForUser(str, i, getUserId());
    }

    boolean putIntForUser(String str, int i, int i2) {
        return putStringForUser(str, Integer.toString(i), i2);
    }

    boolean getBoolForUser(String str, boolean z, int i) {
        return getIntForUser(str, z ? 1 : 0, i) != 0;
    }

    float getFloat(String str, float f) {
        return getFloatForUser(str, f, getUserId());
    }

    float getFloatForUser(String str, float f, int i) {
        String stringForUser = getStringForUser(str, i);
        if (stringForUser == null) {
            return f;
        }
        try {
            return Float.parseFloat(stringForUser);
        } catch (NumberFormatException unused) {
            return f;
        }
    }
}
