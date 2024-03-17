package com.android.keyguard;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.telephony.TelephonyManager;
import android.view.inputmethod.InputMethodManager;
import com.android.internal.util.LatencyTracker;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.EmergencyButtonController;
import com.android.keyguard.KeyguardInputView;
import com.android.keyguard.KeyguardMessageAreaController;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.systemui.R$id;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.statusbar.policy.DevicePostureController;
import com.android.systemui.util.ViewController;
import com.android.systemui.util.concurrency.DelayableExecutor;

public abstract class KeyguardInputViewController<T extends KeyguardInputView> extends ViewController<T> implements KeyguardSecurityView {
    public final EmergencyButton mEmergencyButton;
    public final EmergencyButtonController mEmergencyButtonController;
    public final KeyguardSecurityCallback mKeyguardSecurityCallback;
    public KeyguardSecurityCallback mNullCallback = new KeyguardSecurityCallback() {
        public void dismiss(boolean z, int i) {
        }

        public void dismiss(boolean z, int i, boolean z2) {
        }

        public void onUserInput() {
        }

        public void reportUnlockAttempt(int i, boolean z, int i2) {
        }

        public void reset() {
        }

        public void userActivity() {
        }
    };
    public boolean mPaused;
    public final KeyguardSecurityModel.SecurityMode mSecurityMode;

    public void onViewAttached() {
    }

    public void onViewDetached() {
    }

    public void reset() {
    }

    public void showMessage(CharSequence charSequence, ColorStateList colorStateList) {
    }

    public void showPromptReason(int i) {
    }

    public KeyguardInputViewController(T t, KeyguardSecurityModel.SecurityMode securityMode, KeyguardSecurityCallback keyguardSecurityCallback, EmergencyButtonController emergencyButtonController) {
        super(t);
        EmergencyButton emergencyButton;
        this.mSecurityMode = securityMode;
        this.mKeyguardSecurityCallback = keyguardSecurityCallback;
        if (t == null) {
            emergencyButton = null;
        } else {
            emergencyButton = (EmergencyButton) t.findViewById(R$id.emergency_call_button);
        }
        this.mEmergencyButton = emergencyButton;
        this.mEmergencyButtonController = emergencyButtonController;
    }

    public void onInit() {
        this.mEmergencyButtonController.init();
    }

    public KeyguardSecurityModel.SecurityMode getSecurityMode() {
        return this.mSecurityMode;
    }

    public KeyguardSecurityCallback getKeyguardSecurityCallback() {
        if (this.mPaused) {
            return this.mNullCallback;
        }
        return this.mKeyguardSecurityCallback;
    }

    public void onPause() {
        this.mPaused = true;
    }

    public void onResume(int i) {
        this.mPaused = false;
    }

    public void reloadColors() {
        EmergencyButton emergencyButton = this.mEmergencyButton;
        if (emergencyButton != null) {
            emergencyButton.reloadColors();
        }
    }

    public void startAppearAnimation() {
        ((KeyguardInputView) this.mView).startAppearAnimation();
    }

    public boolean startDisappearAnimation(Runnable runnable) {
        return ((KeyguardInputView) this.mView).startDisappearAnimation(runnable);
    }

    public int getIndexIn(KeyguardSecurityViewFlipper keyguardSecurityViewFlipper) {
        return keyguardSecurityViewFlipper.indexOfChild(this.mView);
    }

    public static class Factory {
        public final DevicePostureController mDevicePostureController;
        public final EmergencyButtonController.Factory mEmergencyButtonControllerFactory;
        public final FalsingCollector mFalsingCollector;
        public final InputMethodManager mInputMethodManager;
        public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
        public final KeyguardViewController mKeyguardViewController;
        public final LatencyTracker mLatencyTracker;
        public final LiftToActivateListener mLiftToActivateListener;
        public final LockPatternUtils mLockPatternUtils;
        public final DelayableExecutor mMainExecutor;
        public final KeyguardMessageAreaController.Factory mMessageAreaControllerFactory;
        public final Resources mResources;
        public final TelephonyManager mTelephonyManager;

        public Factory(KeyguardUpdateMonitor keyguardUpdateMonitor, LockPatternUtils lockPatternUtils, LatencyTracker latencyTracker, KeyguardMessageAreaController.Factory factory, InputMethodManager inputMethodManager, DelayableExecutor delayableExecutor, Resources resources, LiftToActivateListener liftToActivateListener, TelephonyManager telephonyManager, FalsingCollector falsingCollector, EmergencyButtonController.Factory factory2, DevicePostureController devicePostureController, KeyguardViewController keyguardViewController) {
            this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
            this.mLockPatternUtils = lockPatternUtils;
            this.mLatencyTracker = latencyTracker;
            this.mMessageAreaControllerFactory = factory;
            this.mInputMethodManager = inputMethodManager;
            this.mMainExecutor = delayableExecutor;
            this.mResources = resources;
            this.mLiftToActivateListener = liftToActivateListener;
            this.mTelephonyManager = telephonyManager;
            this.mEmergencyButtonControllerFactory = factory2;
            this.mFalsingCollector = falsingCollector;
            this.mDevicePostureController = devicePostureController;
            this.mKeyguardViewController = keyguardViewController;
        }

        public KeyguardInputViewController create(KeyguardInputView keyguardInputView, KeyguardSecurityModel.SecurityMode securityMode, KeyguardSecurityCallback keyguardSecurityCallback) {
            KeyguardInputView keyguardInputView2 = keyguardInputView;
            EmergencyButtonController create = this.mEmergencyButtonControllerFactory.create((EmergencyButton) keyguardInputView2.findViewById(R$id.emergency_call_button));
            if (keyguardInputView2 instanceof KeyguardPatternView) {
                return new KeyguardPatternViewController((KeyguardPatternView) keyguardInputView2, this.mKeyguardUpdateMonitor, securityMode, this.mLockPatternUtils, keyguardSecurityCallback, this.mLatencyTracker, this.mFalsingCollector, create, this.mMessageAreaControllerFactory, this.mDevicePostureController);
            } else if (keyguardInputView2 instanceof KeyguardPasswordView) {
                return new KeyguardPasswordViewController((KeyguardPasswordView) keyguardInputView2, this.mKeyguardUpdateMonitor, securityMode, this.mLockPatternUtils, keyguardSecurityCallback, this.mMessageAreaControllerFactory, this.mLatencyTracker, this.mInputMethodManager, create, this.mMainExecutor, this.mResources, this.mFalsingCollector, this.mKeyguardViewController);
            } else if (keyguardInputView2 instanceof KeyguardPINView) {
                return new KeyguardPinViewController((KeyguardPINView) keyguardInputView2, this.mKeyguardUpdateMonitor, securityMode, this.mLockPatternUtils, keyguardSecurityCallback, this.mMessageAreaControllerFactory, this.mLatencyTracker, this.mLiftToActivateListener, create, this.mFalsingCollector, this.mDevicePostureController);
            } else if (keyguardInputView2 instanceof KeyguardSimPinView) {
                return new KeyguardSimPinViewController((KeyguardSimPinView) keyguardInputView2, this.mKeyguardUpdateMonitor, securityMode, this.mLockPatternUtils, keyguardSecurityCallback, this.mMessageAreaControllerFactory, this.mLatencyTracker, this.mLiftToActivateListener, this.mTelephonyManager, this.mFalsingCollector, create);
            } else if (keyguardInputView2 instanceof KeyguardSimPukView) {
                return new KeyguardSimPukViewController((KeyguardSimPukView) keyguardInputView2, this.mKeyguardUpdateMonitor, securityMode, this.mLockPatternUtils, keyguardSecurityCallback, this.mMessageAreaControllerFactory, this.mLatencyTracker, this.mLiftToActivateListener, this.mTelephonyManager, this.mFalsingCollector, create);
            } else {
                throw new RuntimeException("Unable to find controller for " + keyguardInputView2);
            }
        }
    }
}
