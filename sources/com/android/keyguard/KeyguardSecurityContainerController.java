package com.android.keyguard;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.res.ColorStateList;
import android.metrics.LogMaker;
import android.util.Log;
import android.util.Slog;
import android.view.MotionEvent;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.InstanceId;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.ActiveUnlockConfig;
import com.android.keyguard.AdminSecondaryLockScreenController;
import com.android.keyguard.KeyguardSecurityContainer;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.settingslib.utils.ThreadUtils;
import com.android.systemui.DejankUtils;
import com.android.systemui.Gefingerpoken;
import com.android.systemui.R$bool;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.log.SessionTracker;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.shared.system.SysUiStatsLog;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import com.android.systemui.util.ViewController;
import com.android.systemui.util.settings.GlobalSettings;

public class KeyguardSecurityContainerController extends ViewController<KeyguardSecurityContainer> implements KeyguardSecurityView {
    public static final boolean DEBUG = KeyguardConstants.DEBUG;
    public final AdminSecondaryLockScreenController mAdminSecondaryLockScreenController;
    public final ConfigurationController mConfigurationController;
    public ConfigurationController.ConfigurationListener mConfigurationListener;
    public KeyguardSecurityModel.SecurityMode mCurrentSecurityMode;
    public final FalsingCollector mFalsingCollector;
    public final FalsingManager mFalsingManager;
    public final FeatureFlags mFeatureFlags;
    public final GlobalSettings mGlobalSettings;
    @VisibleForTesting
    public final Gefingerpoken mGlobalTouchListener;
    public KeyguardSecurityCallback mKeyguardSecurityCallback;
    public final KeyguardStateController mKeyguardStateController;
    public final KeyguardUpdateMonitorCallback mKeyguardUpdateMonitorCallback;
    public int mLastOrientation;
    public final LockPatternUtils mLockPatternUtils;
    public final MetricsLogger mMetricsLogger;
    public final KeyguardSecurityContainer.SecurityCallback mSecurityCallback;
    public final KeyguardSecurityModel mSecurityModel;
    public final KeyguardSecurityViewFlipperController mSecurityViewFlipperController;
    public final SessionTracker mSessionTracker;
    public KeyguardSecurityContainer.SwipeListener mSwipeListener;
    public final UiEventLogger mUiEventLogger;
    public final KeyguardUpdateMonitor mUpdateMonitor;
    public UserSwitcherController.UserSwitchCallback mUserSwitchCallback;
    public final UserSwitcherController mUserSwitcherController;

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        showPrimarySecurityScreen(false);
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public KeyguardSecurityContainerController(KeyguardSecurityContainer keyguardSecurityContainer, AdminSecondaryLockScreenController.Factory factory, LockPatternUtils lockPatternUtils, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardSecurityModel keyguardSecurityModel, MetricsLogger metricsLogger, UiEventLogger uiEventLogger, KeyguardStateController keyguardStateController, KeyguardSecurityContainer.SecurityCallback securityCallback, KeyguardSecurityViewFlipperController keyguardSecurityViewFlipperController, ConfigurationController configurationController, FalsingCollector falsingCollector, FalsingManager falsingManager, UserSwitcherController userSwitcherController, FeatureFlags featureFlags, GlobalSettings globalSettings, SessionTracker sessionTracker) {
        super(keyguardSecurityContainer);
        this.mLastOrientation = 0;
        this.mCurrentSecurityMode = KeyguardSecurityModel.SecurityMode.Invalid;
        this.mUserSwitchCallback = new KeyguardSecurityContainerController$$ExternalSyntheticLambda1(this);
        this.mGlobalTouchListener = new Gefingerpoken() {
            public MotionEvent mTouchDown;

            public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
                return false;
            }

            public boolean onTouchEvent(MotionEvent motionEvent) {
                boolean isOneHandedModeLeftAligned;
                if (motionEvent.getActionMasked() == 0) {
                    if (((KeyguardSecurityContainer) KeyguardSecurityContainerController.this.mView).getMode() == 1 && ((isOneHandedModeLeftAligned && motionEvent.getX() > ((float) ((KeyguardSecurityContainer) KeyguardSecurityContainerController.this.mView).getWidth()) / 2.0f) || (!(isOneHandedModeLeftAligned = ((KeyguardSecurityContainer) KeyguardSecurityContainerController.this.mView).isOneHandedModeLeftAligned()) && motionEvent.getX() <= ((float) ((KeyguardSecurityContainer) KeyguardSecurityContainerController.this.mView).getWidth()) / 2.0f))) {
                        KeyguardSecurityContainerController.this.mFalsingCollector.avoidGesture();
                    }
                    MotionEvent motionEvent2 = this.mTouchDown;
                    if (motionEvent2 != null) {
                        motionEvent2.recycle();
                        this.mTouchDown = null;
                    }
                    this.mTouchDown = MotionEvent.obtain(motionEvent);
                    return false;
                } else if (this.mTouchDown == null) {
                    return false;
                } else {
                    if (motionEvent.getActionMasked() != 1 && motionEvent.getActionMasked() != 3) {
                        return false;
                    }
                    this.mTouchDown.recycle();
                    this.mTouchDown = null;
                    return false;
                }
            }
        };
        this.mKeyguardSecurityCallback = new KeyguardSecurityCallback() {
            public void userActivity() {
                if (KeyguardSecurityContainerController.this.mSecurityCallback != null) {
                    KeyguardSecurityContainerController.this.mSecurityCallback.userActivity();
                }
            }

            public void onUserInput() {
                KeyguardSecurityContainerController.this.mUpdateMonitor.cancelFaceAuth();
            }

            public void dismiss(boolean z, int i) {
                dismiss(z, i, false);
            }

            public void dismiss(boolean z, int i, boolean z2) {
                KeyguardSecurityContainerController.this.mSecurityCallback.dismiss(z, i, z2);
            }

            public void reportUnlockAttempt(int i, boolean z, int i2) {
                KeyguardSecurityContainer.BouncerUiEvent bouncerUiEvent;
                int i3 = ((KeyguardSecurityContainer) KeyguardSecurityContainerController.this.mView).getMode() == 1 ? ((KeyguardSecurityContainer) KeyguardSecurityContainerController.this.mView).isOneHandedModeLeftAligned() ? 1 : 2 : 0;
                if (z) {
                    SysUiStatsLog.write(64, 2, i3);
                    KeyguardSecurityContainerController.this.mLockPatternUtils.reportSuccessfulPasswordAttempt(i);
                    ThreadUtils.postOnBackgroundThread(new KeyguardSecurityContainerController$2$$ExternalSyntheticLambda0());
                } else {
                    SysUiStatsLog.write(64, 1, i3);
                    KeyguardSecurityContainerController.this.reportFailedUnlockAttempt(i, i2);
                }
                KeyguardSecurityContainerController.this.mMetricsLogger.write(new LogMaker(197).setType(z ? 10 : 11));
                UiEventLogger r5 = KeyguardSecurityContainerController.this.mUiEventLogger;
                if (z) {
                    bouncerUiEvent = KeyguardSecurityContainer.BouncerUiEvent.BOUNCER_PASSWORD_SUCCESS;
                } else {
                    bouncerUiEvent = KeyguardSecurityContainer.BouncerUiEvent.BOUNCER_PASSWORD_FAILURE;
                }
                r5.log(bouncerUiEvent, KeyguardSecurityContainerController.this.getSessionId());
            }

            public static /* synthetic */ void lambda$reportUnlockAttempt$0() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException unused) {
                }
                System.gc();
                System.runFinalization();
                System.gc();
            }

            public void reset() {
                KeyguardSecurityContainerController.this.mSecurityCallback.reset();
            }

            public void onCancelClicked() {
                KeyguardSecurityContainerController.this.mSecurityCallback.onCancelClicked();
            }
        };
        this.mSwipeListener = new KeyguardSecurityContainer.SwipeListener() {
            public void onSwipeUp() {
                if (!KeyguardSecurityContainerController.this.mUpdateMonitor.isFaceDetectionRunning()) {
                    KeyguardSecurityContainerController.this.mUpdateMonitor.requestFaceAuth(true);
                    KeyguardSecurityContainerController.this.mKeyguardSecurityCallback.userActivity();
                    KeyguardSecurityContainerController.this.showMessage((CharSequence) null, (ColorStateList) null);
                }
                if (KeyguardSecurityContainerController.this.mUpdateMonitor.isFaceEnrolled()) {
                    KeyguardSecurityContainerController.this.mUpdateMonitor.requestActiveUnlock(ActiveUnlockConfig.ACTIVE_UNLOCK_REQUEST_ORIGIN.UNLOCK_INTENT, "swipeUpOnBouncer");
                }
            }
        };
        this.mConfigurationListener = new ConfigurationController.ConfigurationListener() {
            public void onThemeChanged() {
                KeyguardSecurityContainerController.this.reloadColors();
            }

            public void onUiModeChanged() {
                KeyguardSecurityContainerController.this.reloadColors();
            }
        };
        this.mKeyguardUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() {
            public void onDevicePolicyManagerStateChanged() {
                KeyguardSecurityContainerController.this.showPrimarySecurityScreen(false);
            }
        };
        this.mLockPatternUtils = lockPatternUtils;
        this.mUpdateMonitor = keyguardUpdateMonitor;
        this.mSecurityModel = keyguardSecurityModel;
        this.mMetricsLogger = metricsLogger;
        this.mUiEventLogger = uiEventLogger;
        this.mKeyguardStateController = keyguardStateController;
        this.mSecurityCallback = securityCallback;
        this.mSecurityViewFlipperController = keyguardSecurityViewFlipperController;
        AdminSecondaryLockScreenController.Factory factory2 = factory;
        this.mAdminSecondaryLockScreenController = factory.create(this.mKeyguardSecurityCallback);
        this.mConfigurationController = configurationController;
        this.mLastOrientation = getResources().getConfiguration().orientation;
        this.mFalsingCollector = falsingCollector;
        this.mFalsingManager = falsingManager;
        this.mUserSwitcherController = userSwitcherController;
        this.mFeatureFlags = featureFlags;
        this.mGlobalSettings = globalSettings;
        this.mSessionTracker = sessionTracker;
    }

    public void onInit() {
        this.mSecurityViewFlipperController.init();
    }

    public void onViewAttached() {
        this.mUpdateMonitor.registerCallback(this.mKeyguardUpdateMonitorCallback);
        ((KeyguardSecurityContainer) this.mView).setSwipeListener(this.mSwipeListener);
        ((KeyguardSecurityContainer) this.mView).addMotionEventListener(this.mGlobalTouchListener);
        this.mConfigurationController.addCallback(this.mConfigurationListener);
        this.mUserSwitcherController.addUserSwitchCallback(this.mUserSwitchCallback);
    }

    public void onViewDetached() {
        this.mUpdateMonitor.removeCallback(this.mKeyguardUpdateMonitorCallback);
        this.mConfigurationController.removeCallback(this.mConfigurationListener);
        ((KeyguardSecurityContainer) this.mView).removeMotionEventListener(this.mGlobalTouchListener);
        this.mUserSwitcherController.removeUserSwitchCallback(this.mUserSwitchCallback);
    }

    public void onPause() {
        this.mAdminSecondaryLockScreenController.hide();
        if (this.mCurrentSecurityMode != KeyguardSecurityModel.SecurityMode.None) {
            getCurrentSecurityController().onPause();
        }
        ((KeyguardSecurityContainer) this.mView).onPause();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ KeyguardSecurityModel.SecurityMode lambda$showPrimarySecurityScreen$1() {
        return this.mSecurityModel.getSecurityMode(KeyguardUpdateMonitor.getCurrentUser());
    }

    public void showPrimarySecurityScreen(boolean z) {
        KeyguardSecurityModel.SecurityMode securityMode = (KeyguardSecurityModel.SecurityMode) DejankUtils.whitelistIpcs(new KeyguardSecurityContainerController$$ExternalSyntheticLambda0(this));
        if (DEBUG) {
            Log.v("KeyguardSecurityView", "showPrimarySecurityScreen(turningOff=" + z + ")");
        }
        showSecurityScreen(securityMode);
    }

    public void showPromptReason(int i) {
        if (this.mCurrentSecurityMode != KeyguardSecurityModel.SecurityMode.None) {
            if (i != 0) {
                Log.i("KeyguardSecurityView", "Strong auth required, reason: " + i);
            }
            getCurrentSecurityController().showPromptReason(i);
        }
    }

    public void showMessage(CharSequence charSequence, ColorStateList colorStateList) {
        if (this.mCurrentSecurityMode != KeyguardSecurityModel.SecurityMode.None) {
            getCurrentSecurityController().showMessage(charSequence, colorStateList);
        }
    }

    public KeyguardSecurityModel.SecurityMode getCurrentSecurityMode() {
        return this.mCurrentSecurityMode;
    }

    public void reset() {
        ((KeyguardSecurityContainer) this.mView).reset();
        this.mSecurityViewFlipperController.reset();
    }

    public CharSequence getTitle() {
        return ((KeyguardSecurityContainer) this.mView).getTitle();
    }

    public void onResume(int i) {
        if (this.mCurrentSecurityMode != KeyguardSecurityModel.SecurityMode.None) {
            int i2 = 2;
            if (((KeyguardSecurityContainer) this.mView).getMode() == 1) {
                i2 = ((KeyguardSecurityContainer) this.mView).isOneHandedModeLeftAligned() ? 3 : 4;
            }
            SysUiStatsLog.write(63, i2);
            getCurrentSecurityController().onResume(i);
        }
        ((KeyguardSecurityContainer) this.mView).onResume(this.mSecurityModel.getSecurityMode(KeyguardUpdateMonitor.getCurrentUser()), this.mKeyguardStateController.isFaceAuthEnabled());
    }

    public void startAppearAnimation() {
        KeyguardSecurityModel.SecurityMode securityMode = this.mCurrentSecurityMode;
        if (securityMode != KeyguardSecurityModel.SecurityMode.None) {
            ((KeyguardSecurityContainer) this.mView).startAppearAnimation(securityMode);
            getCurrentSecurityController().startAppearAnimation();
        }
    }

    public boolean startDisappearAnimation(Runnable runnable) {
        KeyguardSecurityModel.SecurityMode securityMode = this.mCurrentSecurityMode;
        if (securityMode == KeyguardSecurityModel.SecurityMode.None) {
            return false;
        }
        ((KeyguardSecurityContainer) this.mView).startDisappearAnimation(securityMode);
        return getCurrentSecurityController().startDisappearAnimation(runnable);
    }

    public void onStartingToHide() {
        if (this.mCurrentSecurityMode != KeyguardSecurityModel.SecurityMode.None) {
            getCurrentSecurityController().onStartingToHide();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:0x00bd A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00cf  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00e5  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x00f0  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean showNextSecurityScreenOrFinish(boolean r12, int r13, boolean r14) {
        /*
            r11 = this;
            boolean r0 = DEBUG
            java.lang.String r1 = "KeyguardSecurityView"
            if (r0 == 0) goto L_0x0020
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r2 = "showNextSecurityScreenOrFinish("
            r0.append(r2)
            r0.append(r12)
            java.lang.String r2 = ")"
            r0.append(r2)
            java.lang.String r0 = r0.toString()
            android.util.Log.d(r1, r0)
        L_0x0020:
            com.android.keyguard.KeyguardSecurityContainer$BouncerUiEvent r0 = com.android.keyguard.KeyguardSecurityContainer.BouncerUiEvent.UNKNOWN
            com.android.keyguard.KeyguardUpdateMonitor r2 = r11.mUpdateMonitor
            boolean r2 = r2.getUserHasTrust(r13)
            r3 = 5
            r4 = 4
            r5 = 2
            r6 = 3
            r7 = -1
            r8 = 0
            r9 = 1
            if (r2 == 0) goto L_0x0037
            com.android.keyguard.KeyguardSecurityContainer$BouncerUiEvent r12 = com.android.keyguard.KeyguardSecurityContainer.BouncerUiEvent.BOUNCER_DISMISS_EXTENDED_ACCESS
            r4 = r6
        L_0x0034:
            r1 = r8
            goto L_0x00bb
        L_0x0037:
            com.android.keyguard.KeyguardUpdateMonitor r2 = r11.mUpdateMonitor
            boolean r2 = r2.getUserUnlockedWithBiometric(r13)
            if (r2 == 0) goto L_0x0043
            com.android.keyguard.KeyguardSecurityContainer$BouncerUiEvent r12 = com.android.keyguard.KeyguardSecurityContainer.BouncerUiEvent.BOUNCER_DISMISS_BIOMETRIC
            r4 = r5
            goto L_0x0034
        L_0x0043:
            com.android.keyguard.KeyguardSecurityModel$SecurityMode r2 = com.android.keyguard.KeyguardSecurityModel.SecurityMode.None
            com.android.keyguard.KeyguardSecurityModel$SecurityMode r10 = r11.getCurrentSecurityMode()
            if (r2 != r10) goto L_0x005e
            com.android.keyguard.KeyguardSecurityModel r12 = r11.mSecurityModel
            com.android.keyguard.KeyguardSecurityModel$SecurityMode r12 = r12.getSecurityMode(r13)
            if (r2 != r12) goto L_0x0057
            com.android.keyguard.KeyguardSecurityContainer$BouncerUiEvent r12 = com.android.keyguard.KeyguardSecurityContainer.BouncerUiEvent.BOUNCER_DISMISS_NONE_SECURITY
            r4 = r8
            goto L_0x0034
        L_0x0057:
            r11.showSecurityScreen(r12)
            r12 = r0
            r4 = r7
            r9 = r8
            goto L_0x0034
        L_0x005e:
            if (r12 == 0) goto L_0x00b7
            int[] r12 = com.android.keyguard.KeyguardSecurityContainerController.AnonymousClass6.$SwitchMap$com$android$keyguard$KeyguardSecurityModel$SecurityMode
            com.android.keyguard.KeyguardSecurityModel$SecurityMode r10 = r11.getCurrentSecurityMode()
            int r10 = r10.ordinal()
            r12 = r12[r10]
            if (r12 == r9) goto L_0x00b2
            if (r12 == r5) goto L_0x00b2
            if (r12 == r6) goto L_0x00b2
            if (r12 == r4) goto L_0x0097
            if (r12 == r3) goto L_0x0097
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r2 = "Bad security screen "
            r12.append(r2)
            com.android.keyguard.KeyguardSecurityModel$SecurityMode r2 = r11.getCurrentSecurityMode()
            r12.append(r2)
            java.lang.String r2 = ", fail safe"
            r12.append(r2)
            java.lang.String r12 = r12.toString()
            android.util.Log.v(r1, r12)
            r11.showPrimarySecurityScreen(r8)
            goto L_0x00b7
        L_0x0097:
            com.android.keyguard.KeyguardSecurityModel r12 = r11.mSecurityModel
            com.android.keyguard.KeyguardSecurityModel$SecurityMode r12 = r12.getSecurityMode(r13)
            if (r12 != r2) goto L_0x00ae
            com.android.internal.widget.LockPatternUtils r1 = r11.mLockPatternUtils
            int r2 = com.android.keyguard.KeyguardUpdateMonitor.getCurrentUser()
            boolean r1 = r1.isLockScreenDisabled(r2)
            if (r1 == 0) goto L_0x00ae
            com.android.keyguard.KeyguardSecurityContainer$BouncerUiEvent r12 = com.android.keyguard.KeyguardSecurityContainer.BouncerUiEvent.BOUNCER_DISMISS_SIM
            goto L_0x0034
        L_0x00ae:
            r11.showSecurityScreen(r12)
            goto L_0x00b7
        L_0x00b2:
            com.android.keyguard.KeyguardSecurityContainer$BouncerUiEvent r12 = com.android.keyguard.KeyguardSecurityContainer.BouncerUiEvent.BOUNCER_DISMISS_PASSWORD
            r1 = r9
            r4 = r1
            goto L_0x00bb
        L_0x00b7:
            r12 = r0
            r4 = r7
            r1 = r8
            r9 = r1
        L_0x00bb:
            if (r9 == 0) goto L_0x00cd
            if (r14 != 0) goto L_0x00cd
            com.android.keyguard.KeyguardUpdateMonitor r14 = r11.mUpdateMonitor
            android.content.Intent r14 = r14.getSecondaryLockscreenRequirement(r13)
            if (r14 == 0) goto L_0x00cd
            com.android.keyguard.AdminSecondaryLockScreenController r11 = r11.mAdminSecondaryLockScreenController
            r11.show(r14)
            return r8
        L_0x00cd:
            if (r4 == r7) goto L_0x00e3
            com.android.internal.logging.MetricsLogger r14 = r11.mMetricsLogger
            android.metrics.LogMaker r2 = new android.metrics.LogMaker
            r5 = 197(0xc5, float:2.76E-43)
            r2.<init>(r5)
            android.metrics.LogMaker r2 = r2.setType(r3)
            android.metrics.LogMaker r2 = r2.setSubtype(r4)
            r14.write(r2)
        L_0x00e3:
            if (r12 == r0) goto L_0x00ee
            com.android.internal.logging.UiEventLogger r14 = r11.mUiEventLogger
            com.android.internal.logging.InstanceId r0 = r11.getSessionId()
            r14.log(r12, r0)
        L_0x00ee:
            if (r9 == 0) goto L_0x00f5
            com.android.keyguard.KeyguardSecurityContainer$SecurityCallback r11 = r11.mSecurityCallback
            r11.finish(r1, r13)
        L_0x00f5:
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.keyguard.KeyguardSecurityContainerController.showNextSecurityScreenOrFinish(boolean, int, boolean):boolean");
    }

    /* renamed from: com.android.keyguard.KeyguardSecurityContainerController$6  reason: invalid class name */
    public static /* synthetic */ class AnonymousClass6 {
        public static final /* synthetic */ int[] $SwitchMap$com$android$keyguard$KeyguardSecurityModel$SecurityMode;

        /* JADX WARNING: Can't wrap try/catch for region: R(12:0|1|2|3|4|5|6|7|8|9|10|12) */
        /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                com.android.keyguard.KeyguardSecurityModel$SecurityMode[] r0 = com.android.keyguard.KeyguardSecurityModel.SecurityMode.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$android$keyguard$KeyguardSecurityModel$SecurityMode = r0
                com.android.keyguard.KeyguardSecurityModel$SecurityMode r1 = com.android.keyguard.KeyguardSecurityModel.SecurityMode.Pattern     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$com$android$keyguard$KeyguardSecurityModel$SecurityMode     // Catch:{ NoSuchFieldError -> 0x001d }
                com.android.keyguard.KeyguardSecurityModel$SecurityMode r1 = com.android.keyguard.KeyguardSecurityModel.SecurityMode.Password     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$com$android$keyguard$KeyguardSecurityModel$SecurityMode     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.android.keyguard.KeyguardSecurityModel$SecurityMode r1 = com.android.keyguard.KeyguardSecurityModel.SecurityMode.PIN     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$com$android$keyguard$KeyguardSecurityModel$SecurityMode     // Catch:{ NoSuchFieldError -> 0x0033 }
                com.android.keyguard.KeyguardSecurityModel$SecurityMode r1 = com.android.keyguard.KeyguardSecurityModel.SecurityMode.SimPin     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$com$android$keyguard$KeyguardSecurityModel$SecurityMode     // Catch:{ NoSuchFieldError -> 0x003e }
                com.android.keyguard.KeyguardSecurityModel$SecurityMode r1 = com.android.keyguard.KeyguardSecurityModel.SecurityMode.SimPuk     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.keyguard.KeyguardSecurityContainerController.AnonymousClass6.<clinit>():void");
        }
    }

    public boolean needsInput() {
        return getCurrentSecurityController().needsInput();
    }

    @VisibleForTesting
    public void showSecurityScreen(KeyguardSecurityModel.SecurityMode securityMode) {
        if (DEBUG) {
            Log.d("KeyguardSecurityView", "showSecurityScreen(" + securityMode + ")");
        }
        if (securityMode != KeyguardSecurityModel.SecurityMode.Invalid && securityMode != this.mCurrentSecurityMode) {
            KeyguardInputViewController<KeyguardInputView> currentSecurityController = getCurrentSecurityController();
            if (currentSecurityController != null) {
                currentSecurityController.onPause();
            }
            KeyguardInputViewController<KeyguardInputView> changeSecurityMode = changeSecurityMode(securityMode);
            if (changeSecurityMode != null) {
                changeSecurityMode.onResume(2);
                this.mSecurityViewFlipperController.show(changeSecurityMode);
                configureMode();
            }
            this.mSecurityCallback.onSecurityModeChanged(securityMode, changeSecurityMode != null && changeSecurityMode.needsInput());
        }
    }

    public final boolean canUseOneHandedBouncer() {
        KeyguardSecurityModel.SecurityMode securityMode = this.mCurrentSecurityMode;
        if (securityMode == KeyguardSecurityModel.SecurityMode.Pattern || securityMode == KeyguardSecurityModel.SecurityMode.PIN) {
            return getResources().getBoolean(R$bool.can_use_one_handed_bouncer);
        }
        return false;
    }

    public final boolean canDisplayUserSwitcher() {
        return this.mFeatureFlags.isEnabled(Flags.BOUNCER_USER_SWITCHER);
    }

    public final void configureMode() {
        KeyguardSecurityModel.SecurityMode securityMode = this.mCurrentSecurityMode;
        int i = 0;
        boolean z = securityMode == KeyguardSecurityModel.SecurityMode.SimPin || securityMode == KeyguardSecurityModel.SecurityMode.SimPuk;
        if (canDisplayUserSwitcher() && !z) {
            i = 2;
        } else if (canUseOneHandedBouncer()) {
            i = 1;
        }
        ((KeyguardSecurityContainer) this.mView).initMode(i, this.mGlobalSettings, this.mFalsingManager, this.mUserSwitcherController);
    }

    public void reportFailedUnlockAttempt(int i, int i2) {
        int i3 = 1;
        int currentFailedPasswordAttempts = this.mLockPatternUtils.getCurrentFailedPasswordAttempts(i) + 1;
        if (DEBUG) {
            Log.d("KeyguardSecurityView", "reportFailedPatternAttempt: #" + currentFailedPasswordAttempts);
        }
        DevicePolicyManager devicePolicyManager = this.mLockPatternUtils.getDevicePolicyManager();
        int maximumFailedPasswordsForWipe = devicePolicyManager.getMaximumFailedPasswordsForWipe((ComponentName) null, i);
        int i4 = maximumFailedPasswordsForWipe > 0 ? maximumFailedPasswordsForWipe - currentFailedPasswordAttempts : Integer.MAX_VALUE;
        if (i4 < 5) {
            int profileWithMinimumFailedPasswordsForWipe = devicePolicyManager.getProfileWithMinimumFailedPasswordsForWipe(i);
            if (profileWithMinimumFailedPasswordsForWipe == i) {
                if (profileWithMinimumFailedPasswordsForWipe != 0) {
                    i3 = 3;
                }
            } else if (profileWithMinimumFailedPasswordsForWipe != -10000) {
                i3 = 2;
            }
            if (i4 > 0) {
                ((KeyguardSecurityContainer) this.mView).showAlmostAtWipeDialog(currentFailedPasswordAttempts, i4, i3);
            } else {
                Slog.i("KeyguardSecurityView", "Too many unlock attempts; user " + profileWithMinimumFailedPasswordsForWipe + " will be wiped!");
                ((KeyguardSecurityContainer) this.mView).showWipeDialog(currentFailedPasswordAttempts, i3);
            }
        }
        this.mLockPatternUtils.reportFailedPasswordAttempt(i);
        if (i2 > 0) {
            this.mLockPatternUtils.reportPasswordLockout(i2, i);
            ((KeyguardSecurityContainer) this.mView).showTimeoutDialog(i, i2, this.mLockPatternUtils, this.mSecurityModel.getSecurityMode(i));
        }
    }

    public final KeyguardInputViewController<KeyguardInputView> getCurrentSecurityController() {
        return this.mSecurityViewFlipperController.getSecurityView(this.mCurrentSecurityMode, this.mKeyguardSecurityCallback);
    }

    public final KeyguardInputViewController<KeyguardInputView> changeSecurityMode(KeyguardSecurityModel.SecurityMode securityMode) {
        this.mCurrentSecurityMode = securityMode;
        return getCurrentSecurityController();
    }

    public void updateResources() {
        int i = getResources().getConfiguration().orientation;
        if (i != this.mLastOrientation) {
            this.mLastOrientation = i;
            configureMode();
        }
    }

    public final InstanceId getSessionId() {
        return this.mSessionTracker.getSessionId(1);
    }

    public void updateKeyguardPosition(float f) {
        ((KeyguardSecurityContainer) this.mView).updatePositionByTouchX(f);
    }

    public final void reloadColors() {
        this.mSecurityViewFlipperController.reloadColors();
        ((KeyguardSecurityContainer) this.mView).reloadColors();
    }

    public static class Factory {
        public final AdminSecondaryLockScreenController.Factory mAdminSecondaryLockScreenControllerFactory;
        public final ConfigurationController mConfigurationController;
        public final FalsingCollector mFalsingCollector;
        public final FalsingManager mFalsingManager;
        public final FeatureFlags mFeatureFlags;
        public final GlobalSettings mGlobalSettings;
        public final KeyguardSecurityModel mKeyguardSecurityModel;
        public final KeyguardStateController mKeyguardStateController;
        public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
        public final LockPatternUtils mLockPatternUtils;
        public final MetricsLogger mMetricsLogger;
        public final KeyguardSecurityViewFlipperController mSecurityViewFlipperController;
        public final SessionTracker mSessionTracker;
        public final UiEventLogger mUiEventLogger;
        public final UserSwitcherController mUserSwitcherController;
        public final KeyguardSecurityContainer mView;

        public Factory(KeyguardSecurityContainer keyguardSecurityContainer, AdminSecondaryLockScreenController.Factory factory, LockPatternUtils lockPatternUtils, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardSecurityModel keyguardSecurityModel, MetricsLogger metricsLogger, UiEventLogger uiEventLogger, KeyguardStateController keyguardStateController, KeyguardSecurityViewFlipperController keyguardSecurityViewFlipperController, ConfigurationController configurationController, FalsingCollector falsingCollector, FalsingManager falsingManager, UserSwitcherController userSwitcherController, FeatureFlags featureFlags, GlobalSettings globalSettings, SessionTracker sessionTracker) {
            this.mView = keyguardSecurityContainer;
            this.mAdminSecondaryLockScreenControllerFactory = factory;
            this.mLockPatternUtils = lockPatternUtils;
            this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
            this.mKeyguardSecurityModel = keyguardSecurityModel;
            this.mMetricsLogger = metricsLogger;
            this.mUiEventLogger = uiEventLogger;
            this.mKeyguardStateController = keyguardStateController;
            this.mSecurityViewFlipperController = keyguardSecurityViewFlipperController;
            this.mConfigurationController = configurationController;
            this.mFalsingCollector = falsingCollector;
            this.mFalsingManager = falsingManager;
            this.mFeatureFlags = featureFlags;
            this.mGlobalSettings = globalSettings;
            this.mUserSwitcherController = userSwitcherController;
            this.mSessionTracker = sessionTracker;
        }

        public KeyguardSecurityContainerController create(KeyguardSecurityContainer.SecurityCallback securityCallback) {
            return new KeyguardSecurityContainerController(this.mView, this.mAdminSecondaryLockScreenControllerFactory, this.mLockPatternUtils, this.mKeyguardUpdateMonitor, this.mKeyguardSecurityModel, this.mMetricsLogger, this.mUiEventLogger, this.mKeyguardStateController, securityCallback, this.mSecurityViewFlipperController, this.mConfigurationController, this.mFalsingCollector, this.mFalsingManager, this.mUserSwitcherController, this.mFeatureFlags, this.mGlobalSettings, this.mSessionTracker);
        }
    }
}
