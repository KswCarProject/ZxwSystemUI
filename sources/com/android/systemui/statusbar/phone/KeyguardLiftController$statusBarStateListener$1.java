package com.android.systemui.statusbar.phone;

import com.android.systemui.plugins.statusbar.StatusBarStateController;

/* compiled from: KeyguardLiftController.kt */
public final class KeyguardLiftController$statusBarStateListener$1 implements StatusBarStateController.StateListener {
    public final /* synthetic */ KeyguardLiftController this$0;

    public KeyguardLiftController$statusBarStateListener$1(KeyguardLiftController keyguardLiftController) {
        this.this$0 = keyguardLiftController;
    }

    public void onDozingChanged(boolean z) {
        this.this$0.updateListeningState();
    }
}
