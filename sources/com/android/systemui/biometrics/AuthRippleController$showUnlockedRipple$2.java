package com.android.systemui.biometrics;

/* compiled from: AuthRippleController.kt */
public final class AuthRippleController$showUnlockedRipple$2 implements Runnable {
    public final /* synthetic */ AuthRippleController this$0;

    public AuthRippleController$showUnlockedRipple$2(AuthRippleController authRippleController) {
        this.this$0 = authRippleController;
    }

    public final void run() {
        this.this$0.notificationShadeWindowController.setForcePluginOpen(false, this.this$0);
    }
}
