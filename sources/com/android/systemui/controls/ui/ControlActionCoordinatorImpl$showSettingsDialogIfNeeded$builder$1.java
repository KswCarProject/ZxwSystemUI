package com.android.systemui.controls.ui;

import android.content.DialogInterface;
import android.content.SharedPreferences;

/* compiled from: ControlActionCoordinatorImpl.kt */
public final class ControlActionCoordinatorImpl$showSettingsDialogIfNeeded$builder$1 implements DialogInterface.OnCancelListener {
    public final /* synthetic */ int $attempts;
    public final /* synthetic */ SharedPreferences $prefs;

    public ControlActionCoordinatorImpl$showSettingsDialogIfNeeded$builder$1(int i, SharedPreferences sharedPreferences) {
        this.$attempts = i;
        this.$prefs = sharedPreferences;
    }

    public final void onCancel(DialogInterface dialogInterface) {
        if (this.$attempts < 2) {
            this.$prefs.edit().putInt("show_settings_attempts", this.$attempts + 1).commit();
        }
    }
}
