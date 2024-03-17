package com.android.systemui.statusbar.phone;

import com.android.keyguard.KeyguardUpdateMonitorCallback;

/* compiled from: KeyguardLiftController.kt */
public final class KeyguardLiftController$keyguardUpdateMonitorCallback$1 extends KeyguardUpdateMonitorCallback {
    public final /* synthetic */ KeyguardLiftController this$0;

    public KeyguardLiftController$keyguardUpdateMonitorCallback$1(KeyguardLiftController keyguardLiftController) {
        this.this$0 = keyguardLiftController;
    }

    public void onKeyguardBouncerFullyShowingChanged(boolean z) {
        this.this$0.bouncerVisible = z;
        this.this$0.updateListeningState();
    }

    public void onKeyguardVisibilityChanged(boolean z) {
        this.this$0.updateListeningState();
    }
}
