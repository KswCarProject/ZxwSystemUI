package com.android.keyguard;

import android.view.View;
import com.android.internal.util.LatencyTracker;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardMessageAreaController;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.systemui.R$id;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.statusbar.policy.DevicePostureController;

public class KeyguardPinViewController extends KeyguardPinBasedInputViewController<KeyguardPINView> {
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public final DevicePostureController.Callback mPostureCallback = new KeyguardPinViewController$$ExternalSyntheticLambda1(this);
    public final DevicePostureController mPostureController;

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int i) {
        ((KeyguardPINView) this.mView).onDevicePostureChanged(i);
    }

    public KeyguardPinViewController(KeyguardPINView keyguardPINView, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardSecurityModel.SecurityMode securityMode, LockPatternUtils lockPatternUtils, KeyguardSecurityCallback keyguardSecurityCallback, KeyguardMessageAreaController.Factory factory, LatencyTracker latencyTracker, LiftToActivateListener liftToActivateListener, EmergencyButtonController emergencyButtonController, FalsingCollector falsingCollector, DevicePostureController devicePostureController) {
        super(keyguardPINView, keyguardUpdateMonitor, securityMode, lockPatternUtils, keyguardSecurityCallback, factory, latencyTracker, liftToActivateListener, emergencyButtonController, falsingCollector);
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mPostureController = devicePostureController;
    }

    public void onViewAttached() {
        super.onViewAttached();
        View findViewById = ((KeyguardPINView) this.mView).findViewById(R$id.cancel_button);
        if (findViewById != null) {
            findViewById.setOnClickListener(new KeyguardPinViewController$$ExternalSyntheticLambda0(this));
        }
        this.mPostureController.addCallback(this.mPostureCallback);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewAttached$1(View view) {
        getKeyguardSecurityCallback().reset();
        getKeyguardSecurityCallback().onCancelClicked();
    }

    public void onViewDetached() {
        super.onViewDetached();
        this.mPostureController.removeCallback(this.mPostureCallback);
    }

    public void reloadColors() {
        super.reloadColors();
        ((KeyguardPINView) this.mView).reloadColors();
    }

    public void resetState() {
        super.resetState();
        this.mMessageAreaController.setMessage((CharSequence) "");
    }

    public boolean startDisappearAnimation(Runnable runnable) {
        return ((KeyguardPINView) this.mView).startDisappearAnimation(this.mKeyguardUpdateMonitor.needsSlowUnlockTransition(), runnable);
    }
}
