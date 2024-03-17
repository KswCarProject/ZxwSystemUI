package com.android.keyguard;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.AnimatedStateListDrawable;
import android.hardware.biometrics.BiometricSourceType;
import android.os.Process;
import android.os.VibrationAttributes;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.MathUtils;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import com.android.systemui.Dumpable;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.biometrics.AuthRippleController;
import com.android.systemui.biometrics.UdfpsController;
import com.android.systemui.doze.util.BurnInHelperKt;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.StatusBarState;
import com.android.systemui.statusbar.VibratorHelper;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.ViewController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.io.PrintWriter;
import java.util.Objects;

public class LockIconViewController extends ViewController<LockIconView> implements Dumpable {
    public static final VibrationAttributes TOUCH_VIBRATION_ATTRIBUTES = VibrationAttributes.createForUsage(18);
    public static final float sDefaultDensity;
    public static final int sLockIconRadiusPx;
    public final View.OnClickListener mA11yClickListener = new LockIconViewController$$ExternalSyntheticLambda0(this);
    public final View.AccessibilityDelegate mAccessibilityDelegate = new View.AccessibilityDelegate() {
        public final AccessibilityNodeInfo.AccessibilityAction mAccessibilityAuthenticateHint;
        public final AccessibilityNodeInfo.AccessibilityAction mAccessibilityEnterHint;

        {
            this.mAccessibilityAuthenticateHint = new AccessibilityNodeInfo.AccessibilityAction(16, LockIconViewController.this.getResources().getString(R$string.accessibility_authenticate_hint));
            this.mAccessibilityEnterHint = new AccessibilityNodeInfo.AccessibilityAction(16, LockIconViewController.this.getResources().getString(R$string.accessibility_enter_hint));
        }

        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
            if (!LockIconViewController.this.isActionable()) {
                return;
            }
            if (LockIconViewController.this.mShowLockIcon) {
                accessibilityNodeInfo.addAction(this.mAccessibilityAuthenticateHint);
            } else if (LockIconViewController.this.mShowUnlockIcon) {
                accessibilityNodeInfo.addAction(this.mAccessibilityEnterHint);
            }
        }
    };
    public final AccessibilityManager mAccessibilityManager;
    public final AccessibilityManager.AccessibilityStateChangeListener mAccessibilityStateChangeListener = new LockIconViewController$$ExternalSyntheticLambda1(this);
    public int mActivePointerId = -1;
    public final AuthController mAuthController;
    public final AuthController.Callback mAuthControllerCallback = new AuthController.Callback() {
        public void onAllAuthenticatorsRegistered() {
            LockIconViewController.this.updateUdfpsConfig();
        }

        public void onEnrollmentsChanged() {
            LockIconViewController.this.updateUdfpsConfig();
        }

        public void onUdfpsLocationChanged() {
            LockIconViewController.this.updateUdfpsConfig();
        }
    };
    public final AuthRippleController mAuthRippleController;
    public int mBottomPaddingPx;
    public boolean mCanDismissLockScreen;
    public Runnable mCancelDelayedUpdateVisibilityRunnable;
    public final ConfigurationController mConfigurationController;
    public final ConfigurationController.ConfigurationListener mConfigurationListener = new ConfigurationController.ConfigurationListener() {
        public void onUiModeChanged() {
            LockIconViewController.this.updateColors();
        }

        public void onThemeChanged() {
            LockIconViewController.this.updateColors();
        }

        public void onConfigChanged(Configuration configuration) {
            LockIconViewController.this.updateConfiguration();
            LockIconViewController.this.updateColors();
        }
    };
    public int mDefaultPaddingPx;
    public boolean mDownDetected;
    public final DelayableExecutor mExecutor;
    public final FalsingManager mFalsingManager;
    public float mHeightPixels;
    public final AnimatedStateListDrawable mIcon;
    public float mInterpolatedDarkAmount;
    public boolean mIsBouncerShowing;
    public boolean mIsDozing;
    public boolean mIsKeyguardShowing;
    public final KeyguardStateController.Callback mKeyguardStateCallback = new KeyguardStateController.Callback() {
        public void onUnlockedChanged() {
            LockIconViewController lockIconViewController = LockIconViewController.this;
            lockIconViewController.mCanDismissLockScreen = lockIconViewController.mKeyguardStateController.canDismissLockScreen();
            LockIconViewController.this.updateKeyguardShowing();
            LockIconViewController.this.updateVisibility();
        }

        public void onKeyguardShowingChanged() {
            LockIconViewController lockIconViewController = LockIconViewController.this;
            lockIconViewController.mCanDismissLockScreen = lockIconViewController.mKeyguardStateController.canDismissLockScreen();
            LockIconViewController.this.updateKeyguardShowing();
            if (LockIconViewController.this.mIsKeyguardShowing) {
                LockIconViewController lockIconViewController2 = LockIconViewController.this;
                lockIconViewController2.mUserUnlockedWithBiometric = lockIconViewController2.mKeyguardUpdateMonitor.getUserUnlockedWithBiometric(KeyguardUpdateMonitor.getCurrentUser());
            }
            LockIconViewController.this.updateVisibility();
        }

        public void onKeyguardFadingAwayChanged() {
            LockIconViewController.this.updateKeyguardShowing();
            LockIconViewController.this.updateVisibility();
        }
    };
    public final KeyguardStateController mKeyguardStateController;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public final KeyguardUpdateMonitorCallback mKeyguardUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() {
        public void onKeyguardVisibilityChanged(boolean z) {
            LockIconViewController lockIconViewController = LockIconViewController.this;
            lockIconViewController.mIsBouncerShowing = lockIconViewController.mKeyguardViewController.isBouncerShowing();
            LockIconViewController.this.updateVisibility();
        }

        public void onKeyguardBouncerStateChanged(boolean z) {
            LockIconViewController.this.mIsBouncerShowing = z;
            LockIconViewController.this.updateVisibility();
        }

        public void onBiometricRunningStateChanged(boolean z, BiometricSourceType biometricSourceType) {
            boolean r0 = LockIconViewController.this.mRunningFPS;
            boolean r1 = LockIconViewController.this.mUserUnlockedWithBiometric;
            LockIconViewController lockIconViewController = LockIconViewController.this;
            lockIconViewController.mUserUnlockedWithBiometric = lockIconViewController.mKeyguardUpdateMonitor.getUserUnlockedWithBiometric(KeyguardUpdateMonitor.getCurrentUser());
            if (biometricSourceType == BiometricSourceType.FINGERPRINT) {
                LockIconViewController.this.mRunningFPS = z;
                if (r0 && !LockIconViewController.this.mRunningFPS) {
                    if (LockIconViewController.this.mCancelDelayedUpdateVisibilityRunnable != null) {
                        LockIconViewController.this.mCancelDelayedUpdateVisibilityRunnable.run();
                    }
                    LockIconViewController lockIconViewController2 = LockIconViewController.this;
                    lockIconViewController2.mCancelDelayedUpdateVisibilityRunnable = lockIconViewController2.mExecutor.executeDelayed(new LockIconViewController$3$$ExternalSyntheticLambda0(this), 50);
                    return;
                }
            }
            if (r1 != LockIconViewController.this.mUserUnlockedWithBiometric || r0 != LockIconViewController.this.mRunningFPS) {
                LockIconViewController.this.updateVisibility();
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onBiometricRunningStateChanged$0() {
            LockIconViewController.this.updateVisibility();
        }
    };
    public final KeyguardViewController mKeyguardViewController;
    public CharSequence mLockedLabel;
    public Runnable mLongPressCancelRunnable;
    public final int mMaxBurnInOffsetX;
    public final int mMaxBurnInOffsetY;
    public Runnable mOnGestureDetectedRunnable;
    public boolean mRunningFPS;
    public final Rect mSensorTouchLocation = new Rect();
    public boolean mShowAodLockIcon;
    public boolean mShowAodUnlockedIcon;
    public boolean mShowLockIcon;
    public boolean mShowUnlockIcon;
    public int mStatusBarState;
    public final StatusBarStateController mStatusBarStateController;
    public StatusBarStateController.StateListener mStatusBarStateListener = new StatusBarStateController.StateListener() {
        public void onDozeAmountChanged(float f, float f2) {
            LockIconViewController.this.mInterpolatedDarkAmount = f2;
            ((LockIconView) LockIconViewController.this.mView).setDozeAmount(f2);
            LockIconViewController.this.updateBurnInOffsets();
        }

        public void onDozingChanged(boolean z) {
            LockIconViewController.this.mIsDozing = z;
            LockIconViewController.this.updateBurnInOffsets();
            LockIconViewController.this.updateVisibility();
        }

        public void onStateChanged(int i) {
            LockIconViewController.this.mStatusBarState = i;
            LockIconViewController.this.updateVisibility();
        }
    };
    public boolean mUdfpsEnrolled;
    public boolean mUdfpsSupported;
    public CharSequence mUnlockedLabel;
    public boolean mUserUnlockedWithBiometric;
    public VelocityTracker mVelocityTracker;
    public final VibratorHelper mVibrator;
    public float mWidthPixels;

    static {
        float f = ((float) DisplayMetrics.DENSITY_DEVICE_STABLE) / 160.0f;
        sDefaultDensity = f;
        sLockIconRadiusPx = (int) (f * 36.0f);
    }

    public LockIconViewController(LockIconView lockIconView, StatusBarStateController statusBarStateController, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardViewController keyguardViewController, KeyguardStateController keyguardStateController, FalsingManager falsingManager, AuthController authController, DumpManager dumpManager, AccessibilityManager accessibilityManager, ConfigurationController configurationController, DelayableExecutor delayableExecutor, VibratorHelper vibratorHelper, AuthRippleController authRippleController, Resources resources) {
        super(lockIconView);
        this.mStatusBarStateController = statusBarStateController;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mAuthController = authController;
        this.mKeyguardViewController = keyguardViewController;
        this.mKeyguardStateController = keyguardStateController;
        this.mFalsingManager = falsingManager;
        this.mAccessibilityManager = accessibilityManager;
        this.mConfigurationController = configurationController;
        this.mExecutor = delayableExecutor;
        this.mVibrator = vibratorHelper;
        this.mAuthRippleController = authRippleController;
        this.mMaxBurnInOffsetX = resources.getDimensionPixelSize(R$dimen.udfps_burn_in_offset_x);
        this.mMaxBurnInOffsetY = resources.getDimensionPixelSize(R$dimen.udfps_burn_in_offset_y);
        AnimatedStateListDrawable animatedStateListDrawable = (AnimatedStateListDrawable) resources.getDrawable(R$drawable.super_lock_icon, ((LockIconView) this.mView).getContext().getTheme());
        this.mIcon = animatedStateListDrawable;
        ((LockIconView) this.mView).setImageDrawable(animatedStateListDrawable);
        this.mUnlockedLabel = resources.getString(R$string.accessibility_unlock_button);
        this.mLockedLabel = resources.getString(R$string.accessibility_lock_icon);
        dumpManager.registerDumpable("LockIconViewController", this);
    }

    public void onInit() {
        ((LockIconView) this.mView).setAccessibilityDelegate(this.mAccessibilityDelegate);
    }

    public void onViewAttached() {
        updateIsUdfpsEnrolled();
        updateConfiguration();
        updateKeyguardShowing();
        this.mUserUnlockedWithBiometric = false;
        this.mIsBouncerShowing = this.mKeyguardViewController.isBouncerShowing();
        this.mIsDozing = this.mStatusBarStateController.isDozing();
        this.mInterpolatedDarkAmount = this.mStatusBarStateController.getDozeAmount();
        this.mRunningFPS = this.mKeyguardUpdateMonitor.isFingerprintDetectionRunning();
        this.mCanDismissLockScreen = this.mKeyguardStateController.canDismissLockScreen();
        this.mStatusBarState = this.mStatusBarStateController.getState();
        updateColors();
        this.mConfigurationController.addCallback(this.mConfigurationListener);
        this.mAuthController.addCallback(this.mAuthControllerCallback);
        this.mKeyguardUpdateMonitor.registerCallback(this.mKeyguardUpdateMonitorCallback);
        this.mStatusBarStateController.addCallback(this.mStatusBarStateListener);
        this.mKeyguardStateController.addCallback(this.mKeyguardStateCallback);
        this.mDownDetected = false;
        updateBurnInOffsets();
        updateVisibility();
        this.mAccessibilityManager.addAccessibilityStateChangeListener(this.mAccessibilityStateChangeListener);
        updateAccessibility();
    }

    public final void updateAccessibility() {
        if (this.mAccessibilityManager.isEnabled()) {
            ((LockIconView) this.mView).setOnClickListener(this.mA11yClickListener);
        } else {
            ((LockIconView) this.mView).setOnClickListener((View.OnClickListener) null);
        }
    }

    public void onViewDetached() {
        this.mAuthController.removeCallback(this.mAuthControllerCallback);
        this.mConfigurationController.removeCallback(this.mConfigurationListener);
        this.mKeyguardUpdateMonitor.removeCallback(this.mKeyguardUpdateMonitorCallback);
        this.mStatusBarStateController.removeCallback(this.mStatusBarStateListener);
        this.mKeyguardStateController.removeCallback(this.mKeyguardStateCallback);
        Runnable runnable = this.mCancelDelayedUpdateVisibilityRunnable;
        if (runnable != null) {
            runnable.run();
            this.mCancelDelayedUpdateVisibilityRunnable = null;
        }
        this.mAccessibilityManager.removeAccessibilityStateChangeListener(this.mAccessibilityStateChangeListener);
    }

    public float getTop() {
        return ((LockIconView) this.mView).getLocationTop();
    }

    public final void updateVisibility() {
        Runnable runnable = this.mCancelDelayedUpdateVisibilityRunnable;
        if (runnable != null) {
            runnable.run();
            this.mCancelDelayedUpdateVisibilityRunnable = null;
        }
        if (this.mIsKeyguardShowing || this.mIsDozing) {
            boolean z = this.mUdfpsEnrolled && !this.mShowUnlockIcon && !this.mShowLockIcon && !this.mShowAodUnlockedIcon && !this.mShowAodLockIcon;
            this.mShowLockIcon = !this.mCanDismissLockScreen && !this.mUserUnlockedWithBiometric && isLockScreen() && (!this.mUdfpsEnrolled || !this.mRunningFPS);
            this.mShowUnlockIcon = (this.mCanDismissLockScreen || this.mUserUnlockedWithBiometric) && isLockScreen();
            boolean z2 = this.mIsDozing;
            this.mShowAodUnlockedIcon = z2 && this.mUdfpsEnrolled && !this.mRunningFPS && this.mCanDismissLockScreen;
            this.mShowAodLockIcon = z2 && this.mUdfpsEnrolled && !this.mRunningFPS && !this.mCanDismissLockScreen;
            CharSequence contentDescription = ((LockIconView) this.mView).getContentDescription();
            if (this.mShowLockIcon) {
                ((LockIconView) this.mView).updateIcon(0, false);
                ((LockIconView) this.mView).setContentDescription(this.mLockedLabel);
                ((LockIconView) this.mView).setVisibility(0);
            } else if (this.mShowUnlockIcon) {
                if (z) {
                    ((LockIconView) this.mView).updateIcon(1, false);
                }
                ((LockIconView) this.mView).updateIcon(2, false);
                ((LockIconView) this.mView).setContentDescription(this.mUnlockedLabel);
                ((LockIconView) this.mView).setVisibility(0);
            } else if (this.mShowAodUnlockedIcon) {
                ((LockIconView) this.mView).updateIcon(2, true);
                ((LockIconView) this.mView).setContentDescription(this.mUnlockedLabel);
                ((LockIconView) this.mView).setVisibility(0);
            } else if (this.mShowAodLockIcon) {
                ((LockIconView) this.mView).updateIcon(0, true);
                ((LockIconView) this.mView).setContentDescription(this.mLockedLabel);
                ((LockIconView) this.mView).setVisibility(0);
            } else {
                ((LockIconView) this.mView).clearIcon();
                ((LockIconView) this.mView).setVisibility(4);
                ((LockIconView) this.mView).setContentDescription((CharSequence) null);
            }
            if (!Objects.equals(contentDescription, ((LockIconView) this.mView).getContentDescription()) && ((LockIconView) this.mView).getContentDescription() != null) {
                T t = this.mView;
                ((LockIconView) t).announceForAccessibility(((LockIconView) t).getContentDescription());
                return;
            }
            return;
        }
        ((LockIconView) this.mView).setVisibility(4);
    }

    public final boolean isLockScreen() {
        return !this.mIsDozing && !this.mIsBouncerShowing && this.mStatusBarState == 1;
    }

    public final void updateKeyguardShowing() {
        this.mIsKeyguardShowing = this.mKeyguardStateController.isShowing() && !this.mKeyguardStateController.isKeyguardGoingAway();
    }

    public final void updateColors() {
        ((LockIconView) this.mView).updateColorAndBackgroundVisibility();
    }

    public final void updateConfiguration() {
        Rect bounds = ((WindowManager) getContext().getSystemService(WindowManager.class)).getCurrentWindowMetrics().getBounds();
        this.mWidthPixels = (float) bounds.right;
        this.mHeightPixels = (float) bounds.bottom;
        this.mBottomPaddingPx = getResources().getDimensionPixelSize(R$dimen.lock_icon_margin_bottom);
        this.mDefaultPaddingPx = getResources().getDimensionPixelSize(R$dimen.lock_icon_padding);
        this.mUnlockedLabel = ((LockIconView) this.mView).getContext().getResources().getString(R$string.accessibility_unlock_button);
        this.mLockedLabel = ((LockIconView) this.mView).getContext().getResources().getString(R$string.accessibility_lock_icon);
        updateLockIconLocation();
    }

    public final void updateLockIconLocation() {
        float scaleFactor = this.mAuthController.getScaleFactor();
        int i = (int) (((float) this.mDefaultPaddingPx) * scaleFactor);
        if (this.mUdfpsSupported) {
            ((LockIconView) this.mView).setCenterLocation(this.mAuthController.getUdfpsLocation(), this.mAuthController.getUdfpsRadius(), i);
            return;
        }
        float f = this.mHeightPixels;
        int i2 = this.mBottomPaddingPx;
        int i3 = sLockIconRadiusPx;
        ((LockIconView) this.mView).setCenterLocation(new PointF(this.mWidthPixels / 2.0f, f - (((float) (i2 + i3)) * scaleFactor)), ((float) i3) * scaleFactor, i);
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("mUdfpsSupported: " + this.mUdfpsSupported);
        printWriter.println("mUdfpsEnrolled: " + this.mUdfpsEnrolled);
        printWriter.println("mIsKeyguardShowing: " + this.mIsKeyguardShowing);
        printWriter.println(" mIcon: ");
        for (int i : this.mIcon.getState()) {
            printWriter.print(" " + i);
        }
        printWriter.println();
        printWriter.println(" mShowUnlockIcon: " + this.mShowUnlockIcon);
        printWriter.println(" mShowLockIcon: " + this.mShowLockIcon);
        printWriter.println(" mShowAodUnlockedIcon: " + this.mShowAodUnlockedIcon);
        printWriter.println("  mIsDozing: " + this.mIsDozing);
        printWriter.println("  mIsBouncerShowing: " + this.mIsBouncerShowing);
        printWriter.println("  mUserUnlockedWithBiometric: " + this.mUserUnlockedWithBiometric);
        printWriter.println("  mRunningFPS: " + this.mRunningFPS);
        printWriter.println("  mCanDismissLockScreen: " + this.mCanDismissLockScreen);
        printWriter.println("  mStatusBarState: " + StatusBarState.toString(this.mStatusBarState));
        printWriter.println("  mInterpolatedDarkAmount: " + this.mInterpolatedDarkAmount);
        printWriter.println("  mSensorTouchLocation: " + this.mSensorTouchLocation);
        T t = this.mView;
        if (t != null) {
            ((LockIconView) t).dump(printWriter, strArr);
        }
    }

    public void dozeTimeTick() {
        updateBurnInOffsets();
    }

    public final void updateBurnInOffsets() {
        float lerp = MathUtils.lerp(0.0f, (float) (BurnInHelperKt.getBurnInOffset(this.mMaxBurnInOffsetX * 2, true) - this.mMaxBurnInOffsetX), this.mInterpolatedDarkAmount);
        float lerp2 = MathUtils.lerp(0.0f, (float) (BurnInHelperKt.getBurnInOffset(this.mMaxBurnInOffsetY * 2, false) - this.mMaxBurnInOffsetY), this.mInterpolatedDarkAmount);
        MathUtils.lerp(0.0f, BurnInHelperKt.getBurnInProgressOffset(), this.mInterpolatedDarkAmount);
        ((LockIconView) this.mView).setTranslationX(lerp);
        ((LockIconView) this.mView).setTranslationY(lerp2);
    }

    public final void updateIsUdfpsEnrolled() {
        boolean z = this.mUdfpsSupported;
        boolean z2 = this.mUdfpsEnrolled;
        boolean isUdfpsSupported = this.mKeyguardUpdateMonitor.isUdfpsSupported();
        this.mUdfpsSupported = isUdfpsSupported;
        ((LockIconView) this.mView).setUseBackground(isUdfpsSupported);
        boolean isUdfpsEnrolled = this.mKeyguardUpdateMonitor.isUdfpsEnrolled();
        this.mUdfpsEnrolled = isUdfpsEnrolled;
        if (z != this.mUdfpsSupported || z2 != isUdfpsEnrolled) {
            updateVisibility();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0027, code lost:
        if (r12 != 10) goto L_0x00c3;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onTouchEvent(android.view.MotionEvent r11, java.lang.Runnable r12) {
        /*
            r10 = this;
            boolean r0 = r10.onInterceptTouchEvent(r11)
            r1 = 0
            if (r0 != 0) goto L_0x000b
            r10.cancelTouches()
            return r1
        L_0x000b:
            r10.mOnGestureDetectedRunnable = r12
            int r12 = r11.getActionMasked()
            r2 = 200(0xc8, double:9.9E-322)
            r0 = 1
            if (r12 == 0) goto L_0x0078
            if (r12 == r0) goto L_0x0074
            r4 = 2
            if (r12 == r4) goto L_0x002b
            r5 = 3
            if (r12 == r5) goto L_0x0074
            r5 = 7
            if (r12 == r5) goto L_0x002b
            r4 = 9
            if (r12 == r4) goto L_0x0078
            r11 = 10
            if (r12 == r11) goto L_0x0074
            goto L_0x00c3
        L_0x002b:
            android.view.VelocityTracker r12 = r10.mVelocityTracker
            r12.addMovement(r11)
            android.view.VelocityTracker r12 = r10.mVelocityTracker
            r1 = 1000(0x3e8, float:1.401E-42)
            r12.computeCurrentVelocity(r1)
            android.view.VelocityTracker r12 = r10.mVelocityTracker
            int r1 = r10.mActivePointerId
            float r12 = com.android.systemui.biometrics.UdfpsController.computePointerSpeed(r12, r1)
            int r11 = r11.getClassification()
            if (r11 == r4) goto L_0x00c3
            boolean r11 = com.android.systemui.biometrics.UdfpsController.exceedsVelocityThreshold(r12)
            if (r11 == 0) goto L_0x00c3
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r1 = "lock icon long-press rescheduled due to high pointer velocity="
            r11.append(r1)
            r11.append(r12)
            java.lang.String r11 = r11.toString()
            java.lang.String r12 = "LockIconViewController"
            android.util.Log.v(r12, r11)
            java.lang.Runnable r11 = r10.mLongPressCancelRunnable
            r11.run()
            com.android.systemui.util.concurrency.DelayableExecutor r11 = r10.mExecutor
            com.android.keyguard.LockIconViewController$$ExternalSyntheticLambda3 r12 = new com.android.keyguard.LockIconViewController$$ExternalSyntheticLambda3
            r12.<init>(r10)
            java.lang.Runnable r11 = r11.executeDelayed(r12, r2)
            r10.mLongPressCancelRunnable = r11
            goto L_0x00c3
        L_0x0074:
            r10.cancelTouches()
            goto L_0x00c3
        L_0x0078:
            boolean r12 = r10.mDownDetected
            if (r12 != 0) goto L_0x009b
            android.view.accessibility.AccessibilityManager r12 = r10.mAccessibilityManager
            boolean r12 = r12.isTouchExplorationEnabled()
            if (r12 == 0) goto L_0x009b
            com.android.systemui.statusbar.VibratorHelper r4 = r10.mVibrator
            int r5 = android.os.Process.myUid()
            android.content.Context r12 = r10.getContext()
            java.lang.String r6 = r12.getOpPackageName()
            android.os.VibrationEffect r7 = com.android.systemui.biometrics.UdfpsController.EFFECT_CLICK
            android.os.VibrationAttributes r9 = TOUCH_VIBRATION_ATTRIBUTES
            java.lang.String r8 = "lock-icon-down"
            r4.vibrate(r5, r6, r7, r8, r9)
        L_0x009b:
            int r12 = r11.getPointerId(r1)
            r10.mActivePointerId = r12
            android.view.VelocityTracker r12 = r10.mVelocityTracker
            if (r12 != 0) goto L_0x00ac
            android.view.VelocityTracker r12 = android.view.VelocityTracker.obtain()
            r10.mVelocityTracker = r12
            goto L_0x00af
        L_0x00ac:
            r12.clear()
        L_0x00af:
            android.view.VelocityTracker r12 = r10.mVelocityTracker
            r12.addMovement(r11)
            r10.mDownDetected = r0
            com.android.systemui.util.concurrency.DelayableExecutor r11 = r10.mExecutor
            com.android.keyguard.LockIconViewController$$ExternalSyntheticLambda3 r12 = new com.android.keyguard.LockIconViewController$$ExternalSyntheticLambda3
            r12.<init>(r10)
            java.lang.Runnable r11 = r11.executeDelayed(r12, r2)
            r10.mLongPressCancelRunnable = r11
        L_0x00c3:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.keyguard.LockIconViewController.onTouchEvent(android.view.MotionEvent, java.lang.Runnable):boolean");
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (!inLockIconArea(motionEvent) || !isActionable()) {
            return false;
        }
        if (motionEvent.getActionMasked() == 0) {
            return true;
        }
        return this.mDownDetected;
    }

    public final void onLongPress() {
        AuthRippleController authRippleController;
        cancelTouches();
        if (this.mFalsingManager.isFalseTouch(14)) {
            Log.v("LockIconViewController", "lock icon long-press rejected by the falsing manager.");
            return;
        }
        this.mIsBouncerShowing = true;
        if (this.mUdfpsSupported && this.mShowUnlockIcon && (authRippleController = this.mAuthRippleController) != null) {
            authRippleController.showUnlockRipple(BiometricSourceType.FINGERPRINT);
        }
        updateVisibility();
        Runnable runnable = this.mOnGestureDetectedRunnable;
        if (runnable != null) {
            runnable.run();
        }
        this.mVibrator.vibrate(Process.myUid(), getContext().getOpPackageName(), UdfpsController.EFFECT_CLICK, "lock-icon-device-entry", TOUCH_VIBRATION_ATTRIBUTES);
        this.mKeyguardViewController.showBouncer(true);
    }

    public final void cancelTouches() {
        this.mDownDetected = false;
        Runnable runnable = this.mLongPressCancelRunnable;
        if (runnable != null) {
            runnable.run();
        }
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    public final boolean inLockIconArea(MotionEvent motionEvent) {
        ((LockIconView) this.mView).getHitRect(this.mSensorTouchLocation);
        return this.mSensorTouchLocation.contains((int) motionEvent.getX(), (int) motionEvent.getY()) && ((LockIconView) this.mView).getVisibility() == 0;
    }

    public final boolean isActionable() {
        return this.mUdfpsSupported || this.mShowUnlockIcon;
    }

    public void setAlpha(float f) {
        ((LockIconView) this.mView).setAlpha(f);
    }

    public final void updateUdfpsConfig() {
        this.mExecutor.execute(new LockIconViewController$$ExternalSyntheticLambda2(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateUdfpsConfig$0() {
        updateIsUdfpsEnrolled();
        updateConfiguration();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(View view) {
        onLongPress();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(boolean z) {
        updateAccessibility();
    }
}
