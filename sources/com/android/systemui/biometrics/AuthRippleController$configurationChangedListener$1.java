package com.android.systemui.biometrics;

import com.android.systemui.statusbar.policy.ConfigurationController;

/* compiled from: AuthRippleController.kt */
public final class AuthRippleController$configurationChangedListener$1 implements ConfigurationController.ConfigurationListener {
    public final /* synthetic */ AuthRippleController this$0;

    public AuthRippleController$configurationChangedListener$1(AuthRippleController authRippleController) {
        this.this$0 = authRippleController;
    }

    public void onUiModeChanged() {
        this.this$0.updateRippleColor();
    }

    public void onThemeChanged() {
        this.this$0.updateRippleColor();
    }
}
