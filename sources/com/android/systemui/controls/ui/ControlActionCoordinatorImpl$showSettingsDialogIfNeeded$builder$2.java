package com.android.systemui.controls.ui;

import android.content.DialogInterface;
import android.content.SharedPreferences;

/* compiled from: ControlActionCoordinatorImpl.kt */
public final class ControlActionCoordinatorImpl$showSettingsDialogIfNeeded$builder$2 implements DialogInterface.OnClickListener {
    public final /* synthetic */ int $attempts;
    public final /* synthetic */ SharedPreferences $prefs;

    public ControlActionCoordinatorImpl$showSettingsDialogIfNeeded$builder$2(int i, SharedPreferences sharedPreferences) {
        this.$attempts = i;
        this.$prefs = sharedPreferences;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        if (this.$attempts != 2) {
            this.$prefs.edit().putInt("show_settings_attempts", 2).commit();
        }
    }
}
