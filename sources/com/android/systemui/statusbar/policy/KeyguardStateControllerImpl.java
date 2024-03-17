package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.hardware.biometrics.BiometricSourceType;
import android.os.Build;
import android.os.SystemProperties;
import android.os.Trace;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.Dumpable;
import com.android.systemui.R$bool;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.KeyguardUnlockAnimationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import dagger.Lazy;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;

public class KeyguardStateControllerImpl implements KeyguardStateController, Dumpable {
    public boolean mBouncerShowing;
    public boolean mBypassFadingAnimation;
    public final ArrayList<KeyguardStateController.Callback> mCallbacks = new ArrayList<>();
    public boolean mCanDismissLockScreen;
    public final Context mContext;
    public boolean mDebugUnlocked;
    public float mDismissAmount;
    public boolean mDismissingFromTouch;
    public boolean mFaceAuthEnabled;
    public boolean mFlingingToDismissKeyguard;
    public boolean mFlingingToDismissKeyguardDuringSwipeGesture;
    public boolean mKeyguardFadingAway;
    public long mKeyguardFadingAwayDelay;
    public long mKeyguardFadingAwayDuration;
    public boolean mKeyguardGoingAway;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public final KeyguardUpdateMonitorCallback mKeyguardUpdateMonitorCallback;
    public boolean mLaunchTransitionFadingAway;
    public final LockPatternUtils mLockPatternUtils;
    public boolean mOccluded;
    public boolean mSecure;
    public boolean mShowing;
    public boolean mSnappingKeyguardBackAfterSwipe;
    public boolean mTrustManaged;
    public boolean mTrusted;
    public final Lazy<KeyguardUnlockAnimationController> mUnlockAnimationControllerLazy;

    public KeyguardStateControllerImpl(Context context, KeyguardUpdateMonitor keyguardUpdateMonitor, LockPatternUtils lockPatternUtils, Lazy<KeyguardUnlockAnimationController> lazy, DumpManager dumpManager) {
        UpdateMonitorCallback updateMonitorCallback = new UpdateMonitorCallback();
        this.mKeyguardUpdateMonitorCallback = updateMonitorCallback;
        this.mDebugUnlocked = false;
        this.mDismissAmount = 0.0f;
        this.mDismissingFromTouch = false;
        this.mFlingingToDismissKeyguard = false;
        this.mFlingingToDismissKeyguardDuringSwipeGesture = false;
        this.mSnappingKeyguardBackAfterSwipe = false;
        this.mContext = context;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mLockPatternUtils = lockPatternUtils;
        keyguardUpdateMonitor.registerCallback(updateMonitorCallback);
        this.mUnlockAnimationControllerLazy = lazy;
        dumpManager.registerDumpable(getClass().getSimpleName(), this);
        update(true);
        boolean z = Build.IS_DEBUGGABLE;
    }

    public void addCallback(KeyguardStateController.Callback callback) {
        Objects.requireNonNull(callback, "Callback must not be null. b/128895449");
        if (!this.mCallbacks.contains(callback)) {
            this.mCallbacks.add(callback);
        }
    }

    public void removeCallback(KeyguardStateController.Callback callback) {
        Objects.requireNonNull(callback, "Callback must not be null. b/128895449");
        this.mCallbacks.remove(callback);
    }

    public boolean isShowing() {
        return this.mShowing;
    }

    public boolean isBouncerShowing() {
        return this.mBouncerShowing;
    }

    public boolean isMethodSecure() {
        return this.mSecure;
    }

    public boolean isOccluded() {
        return this.mOccluded;
    }

    public void notifyKeyguardState(boolean z, boolean z2) {
        if (this.mShowing != z || this.mOccluded != z2) {
            this.mShowing = z;
            this.mOccluded = z2;
            Trace.instantForTrack(4096, "UI Events", "Keyguard showing: " + z + " occluded: " + z2);
            notifyKeyguardChanged();
            notifyKeyguardDismissAmountChanged(z ? 0.0f : 1.0f, false);
        }
    }

    public final void notifyKeyguardChanged() {
        Trace.beginSection("KeyguardStateController#notifyKeyguardChanged");
        new ArrayList(this.mCallbacks).forEach(new KeyguardStateControllerImpl$$ExternalSyntheticLambda2());
        Trace.endSection();
    }

    public final void notifyUnlockedChanged() {
        Trace.beginSection("KeyguardStateController#notifyUnlockedChanged");
        new ArrayList(this.mCallbacks).forEach(new KeyguardStateControllerImpl$$ExternalSyntheticLambda3());
        Trace.endSection();
    }

    public void notifyKeyguardFadingAway(long j, long j2, boolean z) {
        this.mKeyguardFadingAwayDelay = j;
        this.mKeyguardFadingAwayDuration = j2;
        this.mBypassFadingAnimation = z;
        setKeyguardFadingAway(true);
    }

    public final void setKeyguardFadingAway(boolean z) {
        if (this.mKeyguardFadingAway != z) {
            Trace.traceCounter(4096, "keyguardFadingAway", z ? 1 : 0);
            this.mKeyguardFadingAway = z;
            ArrayList arrayList = new ArrayList(this.mCallbacks);
            for (int i = 0; i < arrayList.size(); i++) {
                ((KeyguardStateController.Callback) arrayList.get(i)).onKeyguardFadingAwayChanged();
            }
        }
    }

    public void notifyKeyguardDoneFading() {
        notifyKeyguardGoingAway(false);
        setKeyguardFadingAway(false);
    }

    public void update(boolean z) {
        boolean z2;
        Trace.beginSection("KeyguardStateController#update");
        int currentUser = KeyguardUpdateMonitor.getCurrentUser();
        boolean isSecure = this.mLockPatternUtils.isSecure(currentUser);
        boolean z3 = false;
        if (!isSecure || this.mKeyguardUpdateMonitor.getUserCanSkipBouncer(currentUser)) {
            z2 = true;
        } else {
            boolean z4 = Build.IS_DEBUGGABLE;
            z2 = false;
        }
        boolean userTrustIsManaged = this.mKeyguardUpdateMonitor.getUserTrustIsManaged(currentUser);
        boolean userHasTrust = this.mKeyguardUpdateMonitor.getUserHasTrust(currentUser);
        boolean isFaceAuthEnabledForUser = this.mKeyguardUpdateMonitor.isFaceAuthEnabledForUser(currentUser);
        if (!(isSecure == this.mSecure && z2 == this.mCanDismissLockScreen && userTrustIsManaged == this.mTrustManaged && this.mTrusted == userHasTrust && this.mFaceAuthEnabled == isFaceAuthEnabledForUser)) {
            z3 = true;
        }
        if (z3 || z) {
            this.mSecure = isSecure;
            this.mCanDismissLockScreen = z2;
            this.mTrusted = userHasTrust;
            this.mTrustManaged = userTrustIsManaged;
            this.mFaceAuthEnabled = isFaceAuthEnabledForUser;
            notifyUnlockedChanged();
        }
        Trace.endSection();
    }

    public boolean canDismissLockScreen() {
        return this.mCanDismissLockScreen;
    }

    public boolean isKeyguardScreenRotationAllowed() {
        if (SystemProperties.getBoolean("lockscreen.rot_override", false) || this.mContext.getResources().getBoolean(R$bool.config_enableLockScreenRotation)) {
            return true;
        }
        return false;
    }

    public boolean isFaceAuthEnabled() {
        return this.mFaceAuthEnabled;
    }

    public boolean isKeyguardFadingAway() {
        return this.mKeyguardFadingAway;
    }

    public boolean isKeyguardGoingAway() {
        return this.mKeyguardGoingAway;
    }

    public boolean isAnimatingBetweenKeyguardAndSurfaceBehind() {
        return this.mUnlockAnimationControllerLazy.get().isAnimatingBetweenKeyguardAndSurfaceBehind();
    }

    public boolean isBypassFadingAnimation() {
        return this.mBypassFadingAnimation;
    }

    public long getKeyguardFadingAwayDelay() {
        return this.mKeyguardFadingAwayDelay;
    }

    public long getKeyguardFadingAwayDuration() {
        return this.mKeyguardFadingAwayDuration;
    }

    public long calculateGoingToFullShadeDelay() {
        return this.mKeyguardFadingAwayDelay + this.mKeyguardFadingAwayDuration;
    }

    public boolean isFlingingToDismissKeyguard() {
        return this.mFlingingToDismissKeyguard;
    }

    public boolean isFlingingToDismissKeyguardDuringSwipeGesture() {
        return this.mFlingingToDismissKeyguardDuringSwipeGesture;
    }

    public boolean isSnappingKeyguardBackAfterSwipe() {
        return this.mSnappingKeyguardBackAfterSwipe;
    }

    public float getDismissAmount() {
        return this.mDismissAmount;
    }

    public boolean isDismissingFromSwipe() {
        return this.mDismissingFromTouch;
    }

    public void notifyKeyguardGoingAway(boolean z) {
        if (this.mKeyguardGoingAway != z) {
            Trace.traceCounter(4096, "keyguardGoingAway", z ? 1 : 0);
            this.mKeyguardGoingAway = z;
            new ArrayList(this.mCallbacks).forEach(new KeyguardStateControllerImpl$$ExternalSyntheticLambda4());
        }
    }

    public void notifyBouncerShowing(boolean z) {
        if (this.mBouncerShowing != z) {
            this.mBouncerShowing = z;
            new ArrayList(this.mCallbacks).forEach(new KeyguardStateControllerImpl$$ExternalSyntheticLambda0());
        }
    }

    public void notifyPanelFlingEnd() {
        this.mFlingingToDismissKeyguard = false;
        this.mFlingingToDismissKeyguardDuringSwipeGesture = false;
        this.mSnappingKeyguardBackAfterSwipe = false;
    }

    public void notifyPanelFlingStart(boolean z) {
        this.mFlingingToDismissKeyguard = z;
        this.mFlingingToDismissKeyguardDuringSwipeGesture = z && this.mDismissingFromTouch;
        this.mSnappingKeyguardBackAfterSwipe = !z;
    }

    public void notifyKeyguardDismissAmountChanged(float f, boolean z) {
        this.mDismissAmount = f;
        this.mDismissingFromTouch = z;
        new ArrayList(this.mCallbacks).forEach(new KeyguardStateControllerImpl$$ExternalSyntheticLambda1());
    }

    public void setLaunchTransitionFadingAway(boolean z) {
        this.mLaunchTransitionFadingAway = z;
        new ArrayList(this.mCallbacks).forEach(new KeyguardStateControllerImpl$$ExternalSyntheticLambda5());
    }

    public boolean isLaunchTransitionFadingAway() {
        return this.mLaunchTransitionFadingAway;
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("KeyguardStateController:");
        printWriter.println("  mSecure: " + this.mSecure);
        printWriter.println("  mCanDismissLockScreen: " + this.mCanDismissLockScreen);
        printWriter.println("  mTrustManaged: " + this.mTrustManaged);
        printWriter.println("  mTrusted: " + this.mTrusted);
        printWriter.println("  mDebugUnlocked: " + this.mDebugUnlocked);
        printWriter.println("  mFaceAuthEnabled: " + this.mFaceAuthEnabled);
        printWriter.println("  isKeyguardFadingAway: " + isKeyguardFadingAway());
        printWriter.println("  isKeyguardGoingAway: " + isKeyguardGoingAway());
    }

    public class UpdateMonitorCallback extends KeyguardUpdateMonitorCallback {
        public UpdateMonitorCallback() {
        }

        public void onUserSwitchComplete(int i) {
            KeyguardStateControllerImpl.this.update(false);
        }

        public void onTrustChanged(int i) {
            KeyguardStateControllerImpl.this.update(false);
            KeyguardStateControllerImpl.this.notifyKeyguardChanged();
        }

        public void onTrustManagedChanged(int i) {
            KeyguardStateControllerImpl.this.update(false);
        }

        public void onStartedWakingUp() {
            KeyguardStateControllerImpl.this.update(false);
        }

        public void onBiometricAuthenticated(int i, BiometricSourceType biometricSourceType, boolean z) {
            Trace.beginSection("KeyguardUpdateMonitorCallback#onBiometricAuthenticated");
            if (KeyguardStateControllerImpl.this.mKeyguardUpdateMonitor.isUnlockingWithBiometricAllowed(z)) {
                KeyguardStateControllerImpl.this.update(false);
            }
            Trace.endSection();
        }

        public void onFaceUnlockStateChanged(boolean z, int i) {
            KeyguardStateControllerImpl.this.update(false);
        }

        public void onStrongAuthStateChanged(int i) {
            KeyguardStateControllerImpl.this.update(false);
        }

        public void onKeyguardVisibilityChanged(boolean z) {
            KeyguardStateControllerImpl.this.update(false);
        }

        public void onBiometricsCleared() {
            KeyguardStateControllerImpl.this.update(false);
        }
    }
}
