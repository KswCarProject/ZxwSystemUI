package com.android.systemui.biometrics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.hardware.biometrics.BiometricFingerprintConstants;
import android.hardware.display.DisplayManager;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.fingerprint.IUdfpsOverlayController;
import android.hardware.fingerprint.IUdfpsOverlayControllerCallback;
import android.os.Handler;
import android.os.PowerManager;
import android.os.Process;
import android.os.Trace;
import android.os.VibrationAttributes;
import android.os.VibrationEffect;
import android.util.Log;
import android.util.RotationUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.LatencyTracker;
import com.android.internal.util.Preconditions;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.biometrics.BiometricDisplayListener;
import com.android.systemui.doze.DozeReceiver;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.LockscreenShadeTransitionController;
import com.android.systemui.statusbar.VibratorHelper;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.phone.SystemUIDialogManager;
import com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.concurrency.Execution;
import com.android.systemui.util.time.SystemClock;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executor;
import kotlin.Unit;

public class UdfpsController implements DozeReceiver {
    public static final VibrationEffect EFFECT_CLICK = VibrationEffect.get(0);
    @VisibleForTesting
    public static final VibrationAttributes VIBRATION_ATTRIBUTES = new VibrationAttributes.Builder().setUsage(65).build();
    public final AccessibilityManager mAccessibilityManager;
    public boolean mAcquiredReceived;
    public int mActivePointerId = -1;
    public final ActivityLaunchAnimator mActivityLaunchAnimator;
    public final AlternateUdfpsTouchProvider mAlternateTouchProvider;
    public Runnable mAodInterruptRunnable;
    public boolean mAttemptedToDismissKeyguard;
    public Runnable mAuthControllerUpdateUdfpsLocation;
    public final Executor mBiometricExecutor;
    public final BroadcastReceiver mBroadcastReceiver;
    public final Set<Callback> mCallbacks = new HashSet();
    public Runnable mCancelAodTimeoutAction;
    public final ConfigurationController mConfigurationController;
    public final Context mContext;
    public final SystemUIDialogManager mDialogManager;
    public final DumpManager mDumpManager;
    public final Execution mExecution;
    public final FalsingManager mFalsingManager;
    public final DelayableExecutor mFgExecutor;
    public final FingerprintManager mFingerprintManager;
    public boolean mHalControlsIllumination;
    public final UdfpsHbmProvider mHbmProvider;
    public final LayoutInflater mInflater;
    public boolean mIsAodInterruptActive;
    public final KeyguardStateController mKeyguardStateController;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public final StatusBarKeyguardViewManager mKeyguardViewManager;
    public final LatencyTracker mLatencyTracker;
    public final LockscreenShadeTransitionController mLockscreenShadeTransitionController;
    public boolean mOnFingerDown;
    @VisibleForTesting
    public final BiometricDisplayListener mOrientationListener;
    public UdfpsControllerOverlay mOverlay;
    @VisibleForTesting
    public UdfpsOverlayParams mOverlayParams = new UdfpsOverlayParams();
    public final PanelExpansionStateManager mPanelExpansionStateManager;
    public final PowerManager mPowerManager;
    public final ScreenLifecycle.Observer mScreenObserver;
    public boolean mScreenOn;
    @VisibleForTesting
    public int mSensorId;
    public final StatusBarStateController mStatusBarStateController;
    public final SystemClock mSystemClock;
    public long mTouchLogTime;
    public final UnlockedScreenOffAnimationController mUnlockedScreenOffAnimationController;
    public VelocityTracker mVelocityTracker;
    public final VibratorHelper mVibrator;
    public final WindowManager mWindowManager;

    public interface Callback {
        void onFingerDown();

        void onFingerUp();
    }

    public static boolean exceedsVelocityThreshold(float f) {
        return f > 750.0f;
    }

    public class UdfpsOverlayController extends IUdfpsOverlayController.Stub {
        public UdfpsOverlayController() {
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$showUdfpsOverlay$1(long j, int i, IUdfpsOverlayControllerCallback iUdfpsOverlayControllerCallback) {
            IUdfpsOverlayControllerCallback iUdfpsOverlayControllerCallback2 = iUdfpsOverlayControllerCallback;
            UdfpsController udfpsController = UdfpsController.this;
            Context r2 = udfpsController.mContext;
            FingerprintManager r3 = UdfpsController.this.mFingerprintManager;
            LayoutInflater r4 = UdfpsController.this.mInflater;
            WindowManager r5 = UdfpsController.this.mWindowManager;
            AccessibilityManager r6 = UdfpsController.this.mAccessibilityManager;
            StatusBarStateController r7 = UdfpsController.this.mStatusBarStateController;
            PanelExpansionStateManager r8 = UdfpsController.this.mPanelExpansionStateManager;
            StatusBarKeyguardViewManager r9 = UdfpsController.this.mKeyguardViewManager;
            KeyguardUpdateMonitor r10 = UdfpsController.this.mKeyguardUpdateMonitor;
            SystemUIDialogManager r11 = UdfpsController.this.mDialogManager;
            DumpManager r12 = UdfpsController.this.mDumpManager;
            LockscreenShadeTransitionController r13 = UdfpsController.this.mLockscreenShadeTransitionController;
            ConfigurationController r14 = UdfpsController.this.mConfigurationController;
            UdfpsController udfpsController2 = udfpsController;
            SystemClock r15 = UdfpsController.this.mSystemClock;
            UdfpsController udfpsController3 = udfpsController2;
            KeyguardStateController r16 = UdfpsController.this.mKeyguardStateController;
            UnlockedScreenOffAnimationController r17 = UdfpsController.this.mUnlockedScreenOffAnimationController;
            boolean r18 = UdfpsController.this.mHalControlsIllumination;
            UdfpsHbmProvider r19 = UdfpsController.this.mHbmProvider;
            UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda6 udfpsController$UdfpsOverlayController$$ExternalSyntheticLambda6 = r1;
            UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda6 udfpsController$UdfpsOverlayController$$ExternalSyntheticLambda62 = new UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda6(this, j);
            Context context = r2;
            FingerprintManager fingerprintManager = r3;
            UdfpsControllerOverlay udfpsControllerOverlay = new UdfpsControllerOverlay(context, fingerprintManager, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, j, i, iUdfpsOverlayControllerCallback2, udfpsController$UdfpsOverlayController$$ExternalSyntheticLambda6, UdfpsController.this.mActivityLaunchAnimator);
            udfpsController3.showUdfpsOverlay(udfpsControllerOverlay);
        }

        public void showUdfpsOverlay(long j, int i, int i2, IUdfpsOverlayControllerCallback iUdfpsOverlayControllerCallback) {
            UdfpsController.this.mFgExecutor.execute(new UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda2(this, j, i2, iUdfpsOverlayControllerCallback));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ Boolean lambda$showUdfpsOverlay$0(long j, View view, MotionEvent motionEvent, Boolean bool) {
            return Boolean.valueOf(UdfpsController.this.onTouch(j, motionEvent, bool.booleanValue()));
        }

        public void hideUdfpsOverlay(int i) {
            UdfpsController.this.mFgExecutor.execute(new UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda0(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$hideUdfpsOverlay$2() {
            if (UdfpsController.this.mKeyguardUpdateMonitor.isFingerprintDetectionRunning()) {
                Log.d("UdfpsController", "hiding udfps overlay when mKeyguardUpdateMonitor.isFingerprintDetectionRunning()=true");
            }
            UdfpsController.this.hideUdfpsOverlay();
        }

        public void onAcquired(int i, int i2) {
            if (BiometricFingerprintConstants.shouldTurnOffHbm(i2)) {
                UdfpsController.this.mFgExecutor.execute(new UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda4(this, i, i2, i2 == 0));
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onAcquired$3(int i, int i2, boolean z) {
            UdfpsController udfpsController = UdfpsController.this;
            if (udfpsController.mOverlay == null) {
                Log.e("UdfpsController", "Null request when onAcquired for sensorId: " + i + " acquiredInfo=" + i2);
                return;
            }
            udfpsController.mAcquiredReceived = true;
            UdfpsView overlayView = UdfpsController.this.mOverlay.getOverlayView();
            if (overlayView != null) {
                overlayView.stopIllumination();
            }
            if (z) {
                UdfpsController.this.mOverlay.onAcquiredGood();
            }
        }

        public void onEnrollmentProgress(int i, int i2) {
            UdfpsController.this.mFgExecutor.execute(new UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda3(this, i2));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onEnrollmentProgress$4(int i) {
            UdfpsControllerOverlay udfpsControllerOverlay = UdfpsController.this.mOverlay;
            if (udfpsControllerOverlay == null) {
                Log.e("UdfpsController", "onEnrollProgress received but serverRequest is null");
            } else {
                udfpsControllerOverlay.onEnrollmentProgress(i);
            }
        }

        public void onEnrollmentHelp(int i) {
            UdfpsController.this.mFgExecutor.execute(new UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda5(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onEnrollmentHelp$5() {
            UdfpsControllerOverlay udfpsControllerOverlay = UdfpsController.this.mOverlay;
            if (udfpsControllerOverlay == null) {
                Log.e("UdfpsController", "onEnrollmentHelp received but serverRequest is null");
            } else {
                udfpsControllerOverlay.onEnrollmentHelp();
            }
        }

        public void setDebugMessage(int i, String str) {
            UdfpsController.this.mFgExecutor.execute(new UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda1(this, str));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$setDebugMessage$6(String str) {
            UdfpsControllerOverlay udfpsControllerOverlay = UdfpsController.this.mOverlay;
            if (udfpsControllerOverlay != null && !udfpsControllerOverlay.isHiding()) {
                UdfpsController.this.mOverlay.getOverlayView().setDebugMessage(str);
            }
        }
    }

    public void updateOverlayParams(int i, UdfpsOverlayParams udfpsOverlayParams) {
        if (i != this.mSensorId) {
            this.mSensorId = i;
            Log.w("UdfpsController", "updateUdfpsParams | sensorId has changed");
        }
        if (!this.mOverlayParams.equals(udfpsOverlayParams)) {
            this.mOverlayParams = udfpsOverlayParams;
            boolean isShowingAlternateAuth = this.mKeyguardViewManager.isShowingAlternateAuth();
            redrawOverlay();
            if (isShowingAlternateAuth) {
                this.mKeyguardViewManager.showGenericBouncer(true);
            }
        }
    }

    public void setAuthControllerUpdateUdfpsLocation(Runnable runnable) {
        this.mAuthControllerUpdateUdfpsLocation = runnable;
    }

    public void setHalControlsIllumination(boolean z) {
        this.mHalControlsIllumination = z;
    }

    public static float computePointerSpeed(VelocityTracker velocityTracker, int i) {
        return (float) Math.sqrt(Math.pow((double) velocityTracker.getXVelocity(i), 2.0d) + Math.pow((double) velocityTracker.getYVelocity(i), 2.0d));
    }

    public boolean onTouch(MotionEvent motionEvent) {
        UdfpsControllerOverlay udfpsControllerOverlay = this.mOverlay;
        if (udfpsControllerOverlay == null || udfpsControllerOverlay.isHiding()) {
            return false;
        }
        return onTouch(this.mOverlay.getRequestId(), motionEvent, false);
    }

    public final boolean isWithinSensorArea(UdfpsView udfpsView, float f, float f2, boolean z) {
        if (z) {
            return udfpsView.isWithinSensorArea(f, f2);
        }
        UdfpsControllerOverlay udfpsControllerOverlay = this.mOverlay;
        if (udfpsControllerOverlay == null || udfpsControllerOverlay.getAnimationViewController() == null || this.mOverlay.getAnimationViewController().shouldPauseAuth() || !this.mOverlayParams.getSensorBounds().contains((int) f, (int) f2)) {
            return false;
        }
        return true;
    }

    public final Point getTouchInNativeCoordinates(MotionEvent motionEvent, int i) {
        Point point = new Point((int) motionEvent.getRawX(i), (int) motionEvent.getRawY(i));
        int rotation = this.mOverlayParams.getRotation();
        if (rotation == 1 || rotation == 3) {
            RotationUtils.rotatePoint(point, RotationUtils.deltaRotation(rotation, 0), this.mOverlayParams.getLogicalDisplayWidth(), this.mOverlayParams.getLogicalDisplayHeight());
        }
        float scaleFactor = this.mOverlayParams.getScaleFactor();
        point.x = (int) (((float) point.x) / scaleFactor);
        point.y = (int) (((float) point.y) / scaleFactor);
        return point;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0064, code lost:
        if (r6 != 10) goto L_0x01b2;
     */
    @com.android.internal.annotations.VisibleForTesting
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onTouch(long r18, android.view.MotionEvent r20, boolean r21) {
        /*
            r17 = this;
            r7 = r17
            r1 = r18
            r0 = r20
            r3 = r21
            com.android.systemui.biometrics.UdfpsControllerOverlay r4 = r7.mOverlay
            java.lang.String r8 = "UdfpsController"
            r9 = 0
            if (r4 != 0) goto L_0x0015
            java.lang.String r0 = "ignoring onTouch with null overlay"
            android.util.Log.w(r8, r0)
            return r9
        L_0x0015:
            boolean r4 = r4.matchesRequestId(r1)
            if (r4 != 0) goto L_0x003e
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r3 = "ignoring stale touch event: "
            r0.append(r3)
            r0.append(r1)
            java.lang.String r1 = " current: "
            r0.append(r1)
            com.android.systemui.biometrics.UdfpsControllerOverlay r1 = r7.mOverlay
            long r1 = r1.getRequestId()
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            android.util.Log.w(r8, r0)
            return r9
        L_0x003e:
            com.android.systemui.biometrics.UdfpsControllerOverlay r4 = r7.mOverlay
            com.android.systemui.biometrics.UdfpsView r4 = r4.getOverlayView()
            boolean r5 = r4.isIlluminationRequested()
            int r6 = r20.getActionMasked()
            r10 = 1
            if (r6 == 0) goto L_0x01b4
            r11 = -1
            if (r6 == r10) goto L_0x018c
            r12 = 4
            r13 = 3
            r14 = 2
            if (r6 == r14) goto L_0x006c
            if (r6 == r13) goto L_0x018c
            if (r6 == r12) goto L_0x0068
            r15 = 7
            if (r6 == r15) goto L_0x006c
            r5 = 9
            if (r6 == r5) goto L_0x01b4
            r0 = 10
            if (r6 == r0) goto L_0x018c
            goto L_0x01b2
        L_0x0068:
            r4.onTouchOutsideView()
            return r10
        L_0x006c:
            java.lang.String r6 = "UdfpsController.onTouch.ACTION_MOVE"
            android.os.Trace.beginSection(r6)
            int r6 = r7.mActivePointerId
            if (r6 != r11) goto L_0x007a
            int r6 = r0.getPointerId(r9)
            goto L_0x007e
        L_0x007a:
            int r6 = r0.findPointerIndex(r6)
        L_0x007e:
            int r11 = r20.getActionIndex()
            if (r6 != r11) goto L_0x0186
            float r11 = r0.getX(r6)
            float r15 = r0.getY(r6)
            boolean r11 = r7.isWithinSensorArea(r4, r11, r15, r3)
            if (r3 != 0) goto L_0x0094
            if (r11 == 0) goto L_0x00af
        L_0x0094:
            boolean r3 = r17.shouldTryToDismissKeyguard()
            if (r3 == 0) goto L_0x00af
            java.lang.String r0 = "onTouch | dismiss keyguard ACTION_MOVE"
            android.util.Log.v(r8, r0)
            boolean r0 = r7.mOnFingerDown
            if (r0 != 0) goto L_0x00a6
            r17.playStartHaptic()
        L_0x00a6:
            com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager r0 = r7.mKeyguardViewManager
            r0.notifyKeyguardAuthenticated(r9)
            r7.mAttemptedToDismissKeyguard = r10
            goto L_0x01b2
        L_0x00af:
            android.graphics.Point r3 = r7.getTouchInNativeCoordinates(r0, r6)
            if (r11 == 0) goto L_0x0174
            android.view.VelocityTracker r4 = r7.mVelocityTracker
            if (r4 != 0) goto L_0x00bf
            android.view.VelocityTracker r4 = android.view.VelocityTracker.obtain()
            r7.mVelocityTracker = r4
        L_0x00bf:
            android.view.VelocityTracker r4 = r7.mVelocityTracker
            r4.addMovement(r0)
            android.view.VelocityTracker r4 = r7.mVelocityTracker
            r11 = 1000(0x3e8, float:1.401E-42)
            r4.computeCurrentVelocity(r11)
            android.view.VelocityTracker r4 = r7.mVelocityTracker
            int r11 = r7.mActivePointerId
            float r4 = computePointerSpeed(r4, r11)
            float r11 = r0.getTouchMinor(r6)
            float r0 = r0.getTouchMajor(r6)
            boolean r6 = exceedsVelocityThreshold(r4)
            java.lang.Object[] r12 = new java.lang.Object[r12]
            java.lang.Float r15 = java.lang.Float.valueOf(r11)
            r12[r9] = r15
            java.lang.Float r15 = java.lang.Float.valueOf(r0)
            r12[r10] = r15
            java.lang.Float r4 = java.lang.Float.valueOf(r4)
            r12[r14] = r4
            java.lang.Boolean r4 = java.lang.Boolean.valueOf(r6)
            r12[r13] = r4
            java.lang.String r4 = "minor: %.1f, major: %.1f, v: %.1f, exceedsVelocityThreshold: %b"
            java.lang.String r12 = java.lang.String.format(r4, r12)
            com.android.systemui.util.time.SystemClock r4 = r7.mSystemClock
            long r15 = r4.elapsedRealtime()
            long r9 = r7.mTouchLogTime
            long r15 = r15 - r9
            if (r5 != 0) goto L_0x0151
            boolean r4 = r7.mAcquiredReceived
            if (r4 != 0) goto L_0x0151
            if (r6 != 0) goto L_0x0151
            com.android.systemui.biometrics.UdfpsOverlayParams r4 = r7.mOverlayParams
            float r4 = r4.getScaleFactor()
            float r5 = r11 / r4
            float r6 = r0 / r4
            int r4 = r3.x
            int r9 = r3.y
            r0 = r17
            r1 = r18
            r3 = r4
            r4 = r9
            r0.onFingerDown(r1, r3, r4, r5, r6)
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "onTouch | finger down: "
            r0.append(r1)
            r0.append(r12)
            java.lang.String r0 = r0.toString()
            android.util.Log.v(r8, r0)
            com.android.systemui.util.time.SystemClock r0 = r7.mSystemClock
            long r0 = r0.elapsedRealtime()
            r7.mTouchLogTime = r0
            android.os.PowerManager r0 = r7.mPowerManager
            com.android.systemui.util.time.SystemClock r1 = r7.mSystemClock
            long r1 = r1.uptimeMillis()
            r3 = 0
            r0.userActivity(r1, r14, r3)
            r9 = 1
            goto L_0x0187
        L_0x0151:
            r0 = 50
            int r0 = (r15 > r0 ? 1 : (r15 == r0 ? 0 : -1))
            if (r0 < 0) goto L_0x0186
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "onTouch | finger move: "
            r0.append(r1)
            r0.append(r12)
            java.lang.String r0 = r0.toString()
            android.util.Log.v(r8, r0)
            com.android.systemui.util.time.SystemClock r0 = r7.mSystemClock
            long r0 = r0.elapsedRealtime()
            r7.mTouchLogTime = r0
            goto L_0x0186
        L_0x0174:
            java.lang.String r0 = "onTouch | finger outside"
            android.util.Log.v(r8, r0)
            r7.onFingerUp(r1, r4)
            com.android.systemui.util.concurrency.DelayableExecutor r0 = r7.mFgExecutor
            com.android.systemui.biometrics.UdfpsController$$ExternalSyntheticLambda0 r1 = new com.android.systemui.biometrics.UdfpsController$$ExternalSyntheticLambda0
            r1.<init>(r7, r3)
            r0.execute(r1)
        L_0x0186:
            r9 = 0
        L_0x0187:
            android.os.Trace.endSection()
            goto L_0x0213
        L_0x018c:
            java.lang.String r0 = "UdfpsController.onTouch.ACTION_UP"
            android.os.Trace.beginSection(r0)
            r7.mActivePointerId = r11
            android.view.VelocityTracker r0 = r7.mVelocityTracker
            if (r0 == 0) goto L_0x019d
            r0.recycle()
            r0 = 0
            r7.mVelocityTracker = r0
        L_0x019d:
            java.lang.String r0 = "onTouch | finger up"
            android.util.Log.v(r8, r0)
            r0 = 0
            r7.mAttemptedToDismissKeyguard = r0
            r7.onFingerUp(r1, r4)
            com.android.systemui.plugins.FalsingManager r0 = r7.mFalsingManager
            r1 = 13
            r0.isFalseTouch(r1)
            android.os.Trace.endSection()
        L_0x01b2:
            r9 = 0
            goto L_0x0213
        L_0x01b4:
            java.lang.String r1 = "UdfpsController.onTouch.ACTION_DOWN"
            android.os.Trace.beginSection(r1)
            android.view.VelocityTracker r1 = r7.mVelocityTracker
            if (r1 != 0) goto L_0x01c4
            android.view.VelocityTracker r1 = android.view.VelocityTracker.obtain()
            r7.mVelocityTracker = r1
            goto L_0x01c7
        L_0x01c4:
            r1.clear()
        L_0x01c7:
            float r1 = r20.getX()
            float r2 = r20.getY()
            boolean r1 = r7.isWithinSensorArea(r4, r1, r2, r3)
            if (r1 == 0) goto L_0x01ef
            java.lang.String r2 = "UdfpsController.e2e.onPointerDown"
            r4 = 0
            android.os.Trace.beginAsyncSection(r2, r4)
            java.lang.String r2 = "onTouch | action down"
            android.util.Log.v(r8, r2)
            int r2 = r0.getPointerId(r4)
            r7.mActivePointerId = r2
            android.view.VelocityTracker r2 = r7.mVelocityTracker
            r2.addMovement(r0)
            r7.mAcquiredReceived = r4
            r0 = 1
            goto L_0x01f0
        L_0x01ef:
            r0 = 0
        L_0x01f0:
            if (r1 != 0) goto L_0x01f4
            if (r3 == 0) goto L_0x020f
        L_0x01f4:
            boolean r1 = r17.shouldTryToDismissKeyguard()
            if (r1 == 0) goto L_0x020f
            java.lang.String r1 = "onTouch | dismiss keyguard ACTION_DOWN"
            android.util.Log.v(r8, r1)
            boolean r1 = r7.mOnFingerDown
            if (r1 != 0) goto L_0x0206
            r17.playStartHaptic()
        L_0x0206:
            com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager r1 = r7.mKeyguardViewManager
            r2 = 0
            r1.notifyKeyguardAuthenticated(r2)
            r1 = 1
            r7.mAttemptedToDismissKeyguard = r1
        L_0x020f:
            android.os.Trace.endSection()
            r9 = r0
        L_0x0213:
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.biometrics.UdfpsController.onTouch(long, android.view.MotionEvent, boolean):boolean");
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onTouch$0(Point point) {
        if (this.mOverlay == null) {
            Log.e("UdfpsController", "touch outside sensor area receivedbut serverRequest is null");
            return;
        }
        float scaleFactor = this.mOverlayParams.getScaleFactor();
        this.mOverlay.onTouchOutsideOfSensorArea((float) point.x, (float) point.y, ((float) this.mOverlayParams.getSensorBounds().centerX()) / scaleFactor, ((float) this.mOverlayParams.getSensorBounds().centerY()) / scaleFactor, this.mOverlayParams.getRotation());
    }

    public final boolean shouldTryToDismissKeyguard() {
        UdfpsControllerOverlay udfpsControllerOverlay = this.mOverlay;
        return udfpsControllerOverlay != null && (udfpsControllerOverlay.getAnimationViewController() instanceof UdfpsKeyguardViewController) && this.mKeyguardStateController.canDismissLockScreen() && !this.mAttemptedToDismissKeyguard;
    }

    public UdfpsController(Context context, Execution execution, LayoutInflater layoutInflater, FingerprintManager fingerprintManager, WindowManager windowManager, StatusBarStateController statusBarStateController, DelayableExecutor delayableExecutor, PanelExpansionStateManager panelExpansionStateManager, StatusBarKeyguardViewManager statusBarKeyguardViewManager, DumpManager dumpManager, KeyguardUpdateMonitor keyguardUpdateMonitor, FalsingManager falsingManager, PowerManager powerManager, AccessibilityManager accessibilityManager, LockscreenShadeTransitionController lockscreenShadeTransitionController, ScreenLifecycle screenLifecycle, VibratorHelper vibratorHelper, UdfpsHapticsSimulator udfpsHapticsSimulator, UdfpsShell udfpsShell, Optional<UdfpsHbmProvider> optional, KeyguardStateController keyguardStateController, DisplayManager displayManager, Handler handler, ConfigurationController configurationController, SystemClock systemClock, UnlockedScreenOffAnimationController unlockedScreenOffAnimationController, SystemUIDialogManager systemUIDialogManager, LatencyTracker latencyTracker, ActivityLaunchAnimator activityLaunchAnimator, Optional<AlternateUdfpsTouchProvider> optional2, Executor executor) {
        AnonymousClass1 r2 = new ScreenLifecycle.Observer() {
            public void onScreenTurnedOn() {
                UdfpsController.this.mScreenOn = true;
                if (UdfpsController.this.mAodInterruptRunnable != null) {
                    UdfpsController.this.mAodInterruptRunnable.run();
                    UdfpsController.this.mAodInterruptRunnable = null;
                }
            }

            public void onScreenTurnedOff() {
                UdfpsController.this.mScreenOn = false;
            }
        };
        this.mScreenObserver = r2;
        AnonymousClass2 r3 = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                UdfpsControllerOverlay udfpsControllerOverlay = UdfpsController.this.mOverlay;
                if (udfpsControllerOverlay != null && udfpsControllerOverlay.getRequestReason() != 4 && "android.intent.action.CLOSE_SYSTEM_DIALOGS".equals(intent.getAction())) {
                    Log.d("UdfpsController", "ACTION_CLOSE_SYSTEM_DIALOGS received, mRequestReason: " + UdfpsController.this.mOverlay.getRequestReason());
                    UdfpsController.this.mOverlay.cancel();
                    UdfpsController.this.hideUdfpsOverlay();
                }
            }
        };
        this.mBroadcastReceiver = r3;
        this.mContext = context;
        this.mExecution = execution;
        this.mVibrator = vibratorHelper;
        this.mInflater = layoutInflater;
        FingerprintManager fingerprintManager2 = (FingerprintManager) Preconditions.checkNotNull(fingerprintManager);
        this.mFingerprintManager = fingerprintManager2;
        this.mWindowManager = windowManager;
        this.mFgExecutor = delayableExecutor;
        this.mPanelExpansionStateManager = panelExpansionStateManager;
        this.mStatusBarStateController = statusBarStateController;
        this.mKeyguardStateController = keyguardStateController;
        this.mKeyguardViewManager = statusBarKeyguardViewManager;
        this.mDumpManager = dumpManager;
        this.mDialogManager = systemUIDialogManager;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mFalsingManager = falsingManager;
        this.mPowerManager = powerManager;
        this.mAccessibilityManager = accessibilityManager;
        this.mLockscreenShadeTransitionController = lockscreenShadeTransitionController;
        this.mHbmProvider = optional.orElse((Object) null);
        screenLifecycle.addObserver(r2);
        this.mScreenOn = screenLifecycle.getScreenState() == 2;
        this.mConfigurationController = configurationController;
        this.mSystemClock = systemClock;
        this.mUnlockedScreenOffAnimationController = unlockedScreenOffAnimationController;
        this.mLatencyTracker = latencyTracker;
        this.mActivityLaunchAnimator = activityLaunchAnimator;
        this.mAlternateTouchProvider = optional2.orElse((Object) null);
        this.mBiometricExecutor = executor;
        this.mOrientationListener = new BiometricDisplayListener(context, displayManager, handler, BiometricDisplayListener.SensorType.UnderDisplayFingerprint.INSTANCE, new UdfpsController$$ExternalSyntheticLambda4(this));
        UdfpsOverlayController udfpsOverlayController = new UdfpsOverlayController();
        fingerprintManager2.setUdfpsOverlayController(udfpsOverlayController);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CLOSE_SYSTEM_DIALOGS");
        context.registerReceiver(r3, intentFilter, 2);
        udfpsHapticsSimulator.setUdfpsController(this);
        udfpsShell.setUdfpsOverlayController(udfpsOverlayController);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Unit lambda$new$1() {
        Runnable runnable = this.mAuthControllerUpdateUdfpsLocation;
        if (runnable != null) {
            runnable.run();
        }
        return Unit.INSTANCE;
    }

    @VisibleForTesting
    public void playStartHaptic() {
        if (this.mAccessibilityManager.isTouchExplorationEnabled()) {
            this.mVibrator.vibrate(Process.myUid(), this.mContext.getOpPackageName(), EFFECT_CLICK, "udfps-onStart-click", VIBRATION_ATTRIBUTES);
        }
    }

    public void dozeTimeTick() {
        UdfpsView overlayView;
        UdfpsControllerOverlay udfpsControllerOverlay = this.mOverlay;
        if (udfpsControllerOverlay != null && (overlayView = udfpsControllerOverlay.getOverlayView()) != null) {
            overlayView.dozeTimeTick();
        }
    }

    public final void redrawOverlay() {
        UdfpsControllerOverlay udfpsControllerOverlay = this.mOverlay;
        if (udfpsControllerOverlay != null) {
            hideUdfpsOverlay();
            showUdfpsOverlay(udfpsControllerOverlay);
        }
    }

    public final void showUdfpsOverlay(UdfpsControllerOverlay udfpsControllerOverlay) {
        this.mExecution.assertIsMainThread();
        this.mOverlay = udfpsControllerOverlay;
        int requestReason = udfpsControllerOverlay.getRequestReason();
        if (requestReason == 4 && !this.mKeyguardUpdateMonitor.isFingerprintDetectionRunning()) {
            Log.d("UdfpsController", "Attempting to showUdfpsOverlay when fingerprint detection isn't running on keyguard. Skip show.");
        } else if (udfpsControllerOverlay.show(this, this.mOverlayParams)) {
            Log.v("UdfpsController", "showUdfpsOverlay | adding window reason=" + requestReason);
            this.mOnFingerDown = false;
            this.mAttemptedToDismissKeyguard = false;
            this.mOrientationListener.enable();
        } else {
            Log.v("UdfpsController", "showUdfpsOverlay | the overlay is already showing");
        }
    }

    public final void hideUdfpsOverlay() {
        this.mExecution.assertIsMainThread();
        UdfpsControllerOverlay udfpsControllerOverlay = this.mOverlay;
        if (udfpsControllerOverlay != null) {
            UdfpsView overlayView = udfpsControllerOverlay.getOverlayView();
            if (overlayView != null) {
                onFingerUp(this.mOverlay.getRequestId(), overlayView);
            }
            boolean hide = this.mOverlay.hide();
            if (this.mKeyguardViewManager.isShowingAlternateAuth()) {
                this.mKeyguardViewManager.resetAlternateAuth(true);
            }
            Log.v("UdfpsController", "hideUdfpsOverlay | removing window: " + hide);
        } else {
            Log.v("UdfpsController", "hideUdfpsOverlay | the overlay is already hidden");
        }
        this.mOverlay = null;
        this.mOrientationListener.disable();
    }

    public void onAodInterrupt(int i, int i2, float f, float f2) {
        if (!this.mIsAodInterruptActive) {
            if (!this.mKeyguardUpdateMonitor.isFingerprintDetectionRunning()) {
                this.mKeyguardViewManager.showBouncer(true);
                return;
            }
            UdfpsControllerOverlay udfpsControllerOverlay = this.mOverlay;
            UdfpsController$$ExternalSyntheticLambda6 udfpsController$$ExternalSyntheticLambda6 = new UdfpsController$$ExternalSyntheticLambda6(this, udfpsControllerOverlay != null ? udfpsControllerOverlay.getRequestId() : -1, i, i2, f2, f);
            this.mAodInterruptRunnable = udfpsController$$ExternalSyntheticLambda6;
            if (this.mScreenOn) {
                udfpsController$$ExternalSyntheticLambda6.run();
                this.mAodInterruptRunnable = null;
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onAodInterrupt$2(long j, int i, int i2, float f, float f2) {
        this.mIsAodInterruptActive = true;
        this.mCancelAodTimeoutAction = this.mFgExecutor.executeDelayed(new UdfpsController$$ExternalSyntheticLambda7(this), 1000);
        onFingerDown(j, i, i2, f, f2);
    }

    public void addCallback(Callback callback) {
        this.mCallbacks.add(callback);
    }

    public void removeCallback(Callback callback) {
        this.mCallbacks.remove(callback);
    }

    public void onCancelUdfps() {
        UdfpsControllerOverlay udfpsControllerOverlay = this.mOverlay;
        if (!(udfpsControllerOverlay == null || udfpsControllerOverlay.getOverlayView() == null)) {
            onFingerUp(this.mOverlay.getRequestId(), this.mOverlay.getOverlayView());
        }
        if (this.mIsAodInterruptActive) {
            Runnable runnable = this.mCancelAodTimeoutAction;
            if (runnable != null) {
                runnable.run();
                this.mCancelAodTimeoutAction = null;
            }
            this.mIsAodInterruptActive = false;
        }
    }

    public boolean isFingerDown() {
        return this.mOnFingerDown;
    }

    public final void onFingerDown(long j, int i, int i2, float f, float f2) {
        long j2 = j;
        this.mExecution.assertIsMainThread();
        UdfpsControllerOverlay udfpsControllerOverlay = this.mOverlay;
        if (udfpsControllerOverlay == null) {
            Log.w("UdfpsController", "Null request in onFingerDown");
        } else if (!udfpsControllerOverlay.matchesRequestId(j2)) {
            Log.w("UdfpsController", "Mismatched fingerDown: " + j2 + " current: " + this.mOverlay.getRequestId());
        } else {
            this.mLatencyTracker.onActionStart(14);
            if (!this.mOnFingerDown) {
                playStartHaptic();
                if (!this.mKeyguardUpdateMonitor.isFaceDetectionRunning()) {
                    this.mKeyguardUpdateMonitor.requestFaceAuth(false);
                }
            }
            this.mOnFingerDown = true;
            if (this.mAlternateTouchProvider != null) {
                this.mBiometricExecutor.execute(new UdfpsController$$ExternalSyntheticLambda1(this, j, i, i2, f, f2));
            } else {
                this.mFingerprintManager.onPointerDown(j, this.mSensorId, i, i2, f, f2);
            }
            Trace.endAsyncSection("UdfpsController.e2e.onPointerDown", 0);
            UdfpsView overlayView = this.mOverlay.getOverlayView();
            if (overlayView != null) {
                overlayView.startIllumination(new UdfpsController$$ExternalSyntheticLambda2(this, j2));
            }
            for (Callback onFingerDown : this.mCallbacks) {
                onFingerDown.onFingerDown();
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onFingerDown$3(long j, int i, int i2, float f, float f2) {
        this.mAlternateTouchProvider.onPointerDown(j, i, i2, f, f2);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onFingerDown$5(long j) {
        if (this.mAlternateTouchProvider != null) {
            this.mBiometricExecutor.execute(new UdfpsController$$ExternalSyntheticLambda5(this));
            return;
        }
        this.mFingerprintManager.onUiReady(j, this.mSensorId);
        this.mLatencyTracker.onActionEnd(14);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onFingerDown$4() {
        this.mAlternateTouchProvider.onUiReady();
        this.mLatencyTracker.onActionEnd(14);
    }

    public final void onFingerUp(long j, UdfpsView udfpsView) {
        this.mExecution.assertIsMainThread();
        this.mActivePointerId = -1;
        this.mAcquiredReceived = false;
        if (this.mOnFingerDown) {
            if (this.mAlternateTouchProvider != null) {
                this.mBiometricExecutor.execute(new UdfpsController$$ExternalSyntheticLambda3(this, j));
            } else {
                this.mFingerprintManager.onPointerUp(j, this.mSensorId);
            }
            for (Callback onFingerUp : this.mCallbacks) {
                onFingerUp.onFingerUp();
            }
        }
        this.mOnFingerDown = false;
        if (udfpsView.isIlluminationRequested()) {
            udfpsView.stopIllumination();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onFingerUp$6(long j) {
        this.mAlternateTouchProvider.onPointerUp(j);
    }
}
