package com.android.systemui.controls.ui;

import android.content.DialogInterface;
import android.content.SharedPreferences;

/* compiled from: ControlActionCoordinatorImpl.kt */
public final class ControlActionCoordinatorImpl$showSettingsDialogIfNeeded$2 implements DialogInterface.OnClickListener {
    public final /* synthetic */ int $attempts;
    public final /* synthetic */ SharedPreferences $prefs;
    public final /* synthetic */ ControlActionCoordinatorImpl this$0;

    public ControlActionCoordinatorImpl$showSettingsDialogIfNeeded$2(int i, SharedPreferences sharedPreferences, ControlActionCoordinatorImpl controlActionCoordinatorImpl) {
        this.$attempts = i;
        this.$prefs = sharedPreferences;
        this.this$0 = controlActionCoordinatorImpl;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        if (this.$attempts != 2) {
            this.$prefs.edit().putInt("show_settings_attempts", 2).commit();
        }
        this.this$0.secureSettings.putIntForUser("lockscreen_show_controls", 1, -2);
        this.this$0.secureSettings.putIntForUser("lockscreen_allow_trivial_controls", 1, -2);
    }
}
