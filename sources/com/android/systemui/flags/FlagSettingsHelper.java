package com.android.systemui.flags;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.provider.Settings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: FlagSettingsHelper.kt */
public final class FlagSettingsHelper {
    @NotNull
    public final ContentResolver contentResolver;

    public FlagSettingsHelper(@NotNull ContentResolver contentResolver2) {
        this.contentResolver = contentResolver2;
    }

    @Nullable
    public final String getString(@NotNull String str) {
        return Settings.Secure.getString(this.contentResolver, str);
    }

    public final void registerContentObserver(@NotNull String str, boolean z, @NotNull ContentObserver contentObserver) {
        this.contentResolver.registerContentObserver(Settings.Secure.getUriFor(str), z, contentObserver);
    }
}
