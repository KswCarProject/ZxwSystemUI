package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.ColorStateList;
import android.hardware.biometrics.BiometricSourceType;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Trace;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewRootImpl;
import android.view.WindowInsets;
import android.view.WindowManagerGlobal;
import com.android.internal.util.LatencyTracker;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardMessageArea;
import com.android.keyguard.KeyguardMessageAreaController;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.keyguard.KeyguardViewController;
import com.android.keyguard.ViewMediatorCallback;
import com.android.systemui.dock.DockManager;
import com.android.systemui.dreams.DreamOverlayStateController;
import com.android.systemui.navigationbar.NavigationBarView;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.shared.system.QuickStepContract;
import com.android.systemui.shared.system.SysUiStatsLog;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.RemoteInputController;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.notification.ViewGroupFadeHelper;
import com.android.systemui.statusbar.phone.KeyguardBouncer;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionChangeEvent;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionListener;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.unfold.FoldAodAnimationController;
import com.android.systemui.unfold.SysUIUnfoldComponent;
import dagger.Lazy;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class StatusBarKeyguardViewManager implements RemoteInputController.Callback, StatusBarStateController.StateListener, ConfigurationController.ConfigurationListener, PanelExpansionListener, NavigationModeController.ModeChangedListener, KeyguardViewController, FoldAodAnimationController.FoldAodAnimationStatus {
    public ActivityStarter.OnDismissAction mAfterKeyguardGoneAction;
    public final ArrayList<Runnable> mAfterKeyguardGoneRunnables = new ArrayList<>();
    public AlternateAuthInterceptor mAlternateAuthInterceptor;
    public BiometricUnlockController mBiometricUnlockController;
    public KeyguardBouncer mBouncer;
    public KeyguardBypassController mBypassController;
    public CentralSurfaces mCentralSurfaces;
    public final ConfigurationController mConfigurationController;
    public final Context mContext;
    public boolean mDismissActionWillAnimateOnKeyguard;
    public final DockManager.DockEventListener mDockEventListener = new DockManager.DockEventListener() {
    };
    public final DockManager mDockManager;
    public boolean mDozing;
    public final DreamOverlayStateController mDreamOverlayStateController;
    public final KeyguardBouncer.BouncerExpansionCallback mExpansionCallback = new KeyguardBouncer.BouncerExpansionCallback() {
        public boolean mBouncerAnimating;

        public void onFullyShown() {
            this.mBouncerAnimating = false;
            StatusBarKeyguardViewManager.this.updateStates();
            StatusBarKeyguardViewManager.this.mCentralSurfaces.wakeUpIfDozing(SystemClock.uptimeMillis(), StatusBarKeyguardViewManager.this.mCentralSurfaces.getBouncerContainer(), "BOUNCER_VISIBLE");
        }

        public void onStartingToHide() {
            this.mBouncerAnimating = true;
            StatusBarKeyguardViewManager.this.updateStates();
        }

        public void onStartingToShow() {
            this.mBouncerAnimating = true;
            StatusBarKeyguardViewManager.this.updateStates();
        }

        public void onFullyHidden() {
            this.mBouncerAnimating = false;
        }

        public void onExpansionChanged(float f) {
            if (StatusBarKeyguardViewManager.this.mAlternateAuthInterceptor != null) {
                StatusBarKeyguardViewManager.this.mAlternateAuthInterceptor.setBouncerExpansionChanged(f);
            }
            if (this.mBouncerAnimating) {
                StatusBarKeyguardViewManager.this.mCentralSurfaces.setBouncerHiddenFraction(f);
            }
            StatusBarKeyguardViewManager.this.updateStates();
        }

        public void onVisibilityChanged(boolean z) {
            if (!z) {
                StatusBarKeyguardViewManager.this.mCentralSurfaces.setBouncerHiddenFraction(1.0f);
            }
            if (StatusBarKeyguardViewManager.this.mAlternateAuthInterceptor != null) {
                StatusBarKeyguardViewManager.this.mAlternateAuthInterceptor.onBouncerVisibilityChanged();
            }
        }
    };
    public boolean mFirstUpdate = true;
    public final FoldAodAnimationController mFoldAodAnimationController;
    public boolean mGesturalNav;
    public boolean mGlobalActionsVisible = false;
    public boolean mIsDocked;
    public final KeyguardBouncer.Factory mKeyguardBouncerFactory;
    public Runnable mKeyguardGoneCancelAction;
    public KeyguardMessageAreaController mKeyguardMessageAreaController;
    public final KeyguardMessageAreaController.Factory mKeyguardMessageAreaFactory;
    public final KeyguardStateController mKeyguardStateController;
    public final KeyguardUpdateMonitor mKeyguardUpdateManager;
    public int mLastBiometricMode;
    public boolean mLastBouncerDismissible;
    public boolean mLastBouncerIsOrWillBeShowing;
    public boolean mLastBouncerShowing;
    public boolean mLastDozing;
    public boolean mLastGesturalNav;
    public boolean mLastGlobalActionsVisible = false;
    public boolean mLastIsDocked;
    public boolean mLastOccluded;
    public boolean mLastPulsing;
    public boolean mLastRemoteInputActive;
    public boolean mLastScreenOffAnimationPlaying;
    public boolean mLastShowing;
    public final LatencyTracker mLatencyTracker;
    public LockPatternUtils mLockPatternUtils;
    public Runnable mMakeNavigationBarVisibleRunnable = new Runnable() {
        public void run() {
            NavigationBarView navigationBarView = StatusBarKeyguardViewManager.this.mCentralSurfaces.getNavigationBarView();
            if (navigationBarView != null) {
                navigationBarView.setVisibility(0);
            }
            StatusBarKeyguardViewManager.this.mCentralSurfaces.getNotificationShadeWindowView().getWindowInsetsController().show(WindowInsets.Type.navigationBars());
        }
    };
    public final NotificationMediaManager mMediaManager;
    public final NavigationModeController mNavigationModeController;
    public View mNotificationContainer;
    public NotificationPanelViewController mNotificationPanelViewController;
    public final NotificationShadeWindowController mNotificationShadeWindowController;
    public boolean mOccluded;
    public DismissWithActionRequest mPendingWakeupAction;
    public boolean mPulsing;
    public float mQsExpansion;
    public boolean mRemoteInputActive;
    public boolean mScreenOffAnimationPlaying;
    public final Lazy<ShadeController> mShadeController;
    public boolean mShowing;
    public final SysuiStatusBarStateController mStatusBarStateController;
    public final KeyguardUpdateMonitorCallback mUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() {
        public void onEmergencyCallAction() {
            StatusBarKeyguardViewManager statusBarKeyguardViewManager = StatusBarKeyguardViewManager.this;
            if (statusBarKeyguardViewManager.mOccluded) {
                statusBarKeyguardViewManager.reset(true);
            }
        }
    };
    public ViewMediatorCallback mViewMediatorCallback;

    public interface AlternateAuthInterceptor {
        void dump(PrintWriter printWriter);

        boolean hideAlternateAuthBouncer();

        boolean isAnimating();

        boolean isShowingAlternateAuthBouncer();

        void onBouncerVisibilityChanged();

        boolean onTouch(MotionEvent motionEvent);

        void requestUdfps(boolean z, int i);

        void setBouncerExpansionChanged(float f);

        void setQsExpansion(float f);

        boolean showAlternateAuthBouncer();
    }

    public void onCancelClicked() {
    }

    public StatusBarKeyguardViewManager(Context context, ViewMediatorCallback viewMediatorCallback, LockPatternUtils lockPatternUtils, SysuiStatusBarStateController sysuiStatusBarStateController, ConfigurationController configurationController, KeyguardUpdateMonitor keyguardUpdateMonitor, DreamOverlayStateController dreamOverlayStateController, NavigationModeController navigationModeController, DockManager dockManager, NotificationShadeWindowController notificationShadeWindowController, KeyguardStateController keyguardStateController, NotificationMediaManager notificationMediaManager, KeyguardBouncer.Factory factory, KeyguardMessageAreaController.Factory factory2, Optional<SysUIUnfoldComponent> optional, Lazy<ShadeController> lazy, LatencyTracker latencyTracker) {
        this.mContext = context;
        this.mViewMediatorCallback = viewMediatorCallback;
        this.mLockPatternUtils = lockPatternUtils;
        this.mConfigurationController = configurationController;
        this.mNavigationModeController = navigationModeController;
        this.mNotificationShadeWindowController = notificationShadeWindowController;
        this.mDreamOverlayStateController = dreamOverlayStateController;
        this.mKeyguardStateController = keyguardStateController;
        this.mMediaManager = notificationMediaManager;
        this.mKeyguardUpdateManager = keyguardUpdateMonitor;
        this.mStatusBarStateController = sysuiStatusBarStateController;
        this.mDockManager = dockManager;
        this.mKeyguardBouncerFactory = factory;
        this.mKeyguardMessageAreaFactory = factory2;
        this.mShadeController = lazy;
        this.mLatencyTracker = latencyTracker;
        this.mFoldAodAnimationController = (FoldAodAnimationController) optional.map(new DozeParameters$$ExternalSyntheticLambda0()).orElse((Object) null);
    }

    public void registerCentralSurfaces(CentralSurfaces centralSurfaces, NotificationPanelViewController notificationPanelViewController, PanelExpansionStateManager panelExpansionStateManager, BiometricUnlockController biometricUnlockController, View view, KeyguardBypassController keyguardBypassController) {
        this.mCentralSurfaces = centralSurfaces;
        this.mBiometricUnlockController = biometricUnlockController;
        ViewGroup bouncerContainer = centralSurfaces.getBouncerContainer();
        this.mBouncer = this.mKeyguardBouncerFactory.create(bouncerContainer, this.mExpansionCallback);
        this.mNotificationPanelViewController = notificationPanelViewController;
        if (panelExpansionStateManager != null) {
            panelExpansionStateManager.addExpansionListener(this);
        }
        this.mBypassController = keyguardBypassController;
        this.mNotificationContainer = view;
        this.mKeyguardMessageAreaController = this.mKeyguardMessageAreaFactory.create(KeyguardMessageArea.findSecurityMessageDisplay(bouncerContainer));
        registerListeners();
    }

    public void removeAlternateAuthInterceptor(AlternateAuthInterceptor alternateAuthInterceptor) {
        if (Objects.equals(this.mAlternateAuthInterceptor, alternateAuthInterceptor)) {
            this.mAlternateAuthInterceptor = null;
            resetAlternateAuth(true);
        }
    }

    public void setAlternateAuthInterceptor(AlternateAuthInterceptor alternateAuthInterceptor) {
        if (!Objects.equals(this.mAlternateAuthInterceptor, alternateAuthInterceptor)) {
            this.mAlternateAuthInterceptor = alternateAuthInterceptor;
            resetAlternateAuth(false);
        }
    }

    public final void registerListeners() {
        this.mKeyguardUpdateManager.registerCallback(this.mUpdateMonitorCallback);
        this.mStatusBarStateController.addCallback(this);
        this.mConfigurationController.addCallback(this);
        this.mGesturalNav = QuickStepContract.isGesturalMode(this.mNavigationModeController.addListener(this));
        FoldAodAnimationController foldAodAnimationController = this.mFoldAodAnimationController;
        if (foldAodAnimationController != null) {
            foldAodAnimationController.addCallback((FoldAodAnimationController.FoldAodAnimationStatus) this);
        }
        DockManager dockManager = this.mDockManager;
        if (dockManager != null) {
            dockManager.addListener(this.mDockEventListener);
            this.mIsDocked = this.mDockManager.isDocked();
        }
    }

    public void onDensityOrFontScaleChanged() {
        hideBouncer(true);
    }

    public void onPanelExpansionChanged(PanelExpansionChangeEvent panelExpansionChangeEvent) {
        float fraction = panelExpansionChangeEvent.getFraction();
        boolean tracking = panelExpansionChangeEvent.getTracking();
        boolean z = this.mDreamOverlayStateController.isOverlayActive() && (this.mNotificationPanelViewController.isExpanded() || this.mNotificationPanelViewController.isExpanding());
        if (this.mDozing && !this.mPulsing) {
            return;
        }
        if (this.mNotificationPanelViewController.isUnlockHintRunning()) {
            this.mBouncer.setExpansion(1.0f);
        } else if (this.mStatusBarStateController.getState() == 2 && this.mKeyguardUpdateManager.isUdfpsEnrolled()) {
        } else {
            if (bouncerNeedsScrimming()) {
                this.mBouncer.setExpansion(0.0f);
                return;
            }
            boolean z2 = this.mShowing;
            if (z2 && !z) {
                if (!isWakeAndUnlocking() && !this.mCentralSurfaces.isInLaunchTransition() && !isUnlockCollapsing()) {
                    this.mBouncer.setExpansion(fraction);
                }
                if (fraction != 1.0f && tracking && !this.mKeyguardStateController.canDismissLockScreen() && !this.mBouncer.isShowing() && !this.mBouncer.isAnimatingAway()) {
                    this.mBouncer.show(false, false);
                }
            } else if (!z2 && this.mBouncer.inTransit()) {
                this.mBouncer.setExpansion(fraction);
            } else if (this.mPulsing && fraction == 0.0f) {
                this.mCentralSurfaces.wakeUpIfDozing(SystemClock.uptimeMillis(), this.mCentralSurfaces.getBouncerContainer(), "BOUNCER_VISIBLE");
            }
        }
    }

    public void setGlobalActionsVisible(boolean z) {
        this.mGlobalActionsVisible = z;
        updateStates();
    }

    public void show(Bundle bundle) {
        Trace.beginSection("StatusBarKeyguardViewManager#show");
        this.mShowing = true;
        this.mNotificationShadeWindowController.setKeyguardShowing(true);
        KeyguardStateController keyguardStateController = this.mKeyguardStateController;
        keyguardStateController.notifyKeyguardState(this.mShowing, keyguardStateController.isOccluded());
        reset(true);
        SysUiStatsLog.write(62, 2);
        Trace.endSection();
    }

    public void showBouncerOrKeyguard(boolean z) {
        if (!this.mBouncer.needsFullscreenBouncer() || this.mDozing) {
            this.mCentralSurfaces.showKeyguard();
            if (z) {
                hideBouncer(false);
                this.mBouncer.prepare();
            }
        } else {
            this.mCentralSurfaces.hideKeyguard();
            this.mBouncer.show(true);
        }
        updateStates();
    }

    public void showGenericBouncer(boolean z) {
        if (shouldShowAltAuth()) {
            updateAlternateAuthShowing(this.mAlternateAuthInterceptor.showAlternateAuthBouncer());
        } else {
            showBouncer(z);
        }
    }

    public final boolean shouldShowAltAuth() {
        if (this.mAlternateAuthInterceptor == null || !this.mKeyguardUpdateManager.isUnlockingWithBiometricAllowed(true)) {
            return false;
        }
        return true;
    }

    public void hideBouncer(boolean z) {
        KeyguardBouncer keyguardBouncer = this.mBouncer;
        if (keyguardBouncer != null) {
            keyguardBouncer.hide(z);
            if (this.mShowing) {
                cancelPostAuthActions();
            }
            cancelPendingWakeupAction();
        }
    }

    public void showBouncer(boolean z) {
        resetAlternateAuth(false);
        if (this.mShowing && !this.mBouncer.isShowing()) {
            this.mBouncer.show(false, z);
        }
        updateStates();
    }

    public void dismissWithAction(ActivityStarter.OnDismissAction onDismissAction, Runnable runnable, boolean z) {
        dismissWithAction(onDismissAction, runnable, z, (String) null);
    }

    public void dismissWithAction(ActivityStarter.OnDismissAction onDismissAction, Runnable runnable, boolean z, String str) {
        if (this.mShowing) {
            try {
                Trace.beginSection("StatusBarKeyguardViewManager#dismissWithAction");
                cancelPendingWakeupAction();
                if (!this.mDozing || isWakeAndUnlocking()) {
                    this.mAfterKeyguardGoneAction = onDismissAction;
                    this.mKeyguardGoneCancelAction = runnable;
                    this.mDismissActionWillAnimateOnKeyguard = onDismissAction != null && onDismissAction.willRunAnimationOnKeyguard();
                    if (shouldShowAltAuth()) {
                        if (!z) {
                            this.mBouncer.setDismissAction(this.mAfterKeyguardGoneAction, this.mKeyguardGoneCancelAction);
                            this.mAfterKeyguardGoneAction = null;
                            this.mKeyguardGoneCancelAction = null;
                        }
                        updateAlternateAuthShowing(this.mAlternateAuthInterceptor.showAlternateAuthBouncer());
                        Trace.endSection();
                        return;
                    }
                    if (z) {
                        this.mBouncer.show(false);
                    } else {
                        this.mBouncer.showWithDismissAction(this.mAfterKeyguardGoneAction, this.mKeyguardGoneCancelAction);
                        this.mAfterKeyguardGoneAction = null;
                        this.mKeyguardGoneCancelAction = null;
                    }
                    Trace.endSection();
                } else {
                    this.mPendingWakeupAction = new DismissWithActionRequest(onDismissAction, runnable, z, str);
                    return;
                }
            } finally {
                Trace.endSection();
            }
        }
        updateStates();
    }

    public final boolean isWakeAndUnlocking() {
        int mode = this.mBiometricUnlockController.getMode();
        return mode == 1 || mode == 2;
    }

    public final boolean isUnlockCollapsing() {
        return this.mBiometricUnlockController.getMode() == 5;
    }

    public void addAfterKeyguardGoneRunnable(Runnable runnable) {
        this.mAfterKeyguardGoneRunnables.add(runnable);
    }

    public void reset(boolean z) {
        if (this.mShowing) {
            this.mNotificationPanelViewController.resetViews(true);
            if (!this.mOccluded || this.mDozing) {
                showBouncerOrKeyguard(z);
            } else {
                this.mCentralSurfaces.hideKeyguard();
                if (z || this.mBouncer.needsFullscreenBouncer()) {
                    hideBouncer(false);
                }
            }
            resetAlternateAuth(false);
            this.mKeyguardUpdateManager.sendKeyguardReset();
            updateStates();
        }
    }

    public void resetAlternateAuth(boolean z) {
        AlternateAuthInterceptor alternateAuthInterceptor = this.mAlternateAuthInterceptor;
        updateAlternateAuthShowing((alternateAuthInterceptor != null && alternateAuthInterceptor.hideAlternateAuthBouncer()) || z);
    }

    public final void updateAlternateAuthShowing(boolean z) {
        boolean isShowingAlternateAuth = isShowingAlternateAuth();
        KeyguardMessageAreaController keyguardMessageAreaController = this.mKeyguardMessageAreaController;
        if (keyguardMessageAreaController != null) {
            keyguardMessageAreaController.setAltBouncerShowing(isShowingAlternateAuth);
        }
        this.mBypassController.setAltBouncerShowing(isShowingAlternateAuth);
        this.mKeyguardUpdateManager.setUdfpsBouncerShowing(isShowingAlternateAuth);
        if (z) {
            this.mCentralSurfaces.updateScrimController();
        }
    }

    public void onStartedWakingUp() {
        this.mCentralSurfaces.getNotificationShadeWindowView().getWindowInsetsController().setAnimationsDisabled(false);
        NavigationBarView navigationBarView = this.mCentralSurfaces.getNavigationBarView();
        if (navigationBarView != null) {
            navigationBarView.forEachView(new StatusBarKeyguardViewManager$$ExternalSyntheticLambda2());
        }
    }

    public void onStartedGoingToSleep() {
        this.mCentralSurfaces.getNotificationShadeWindowView().getWindowInsetsController().setAnimationsDisabled(true);
        NavigationBarView navigationBarView = this.mCentralSurfaces.getNavigationBarView();
        if (navigationBarView != null) {
            navigationBarView.forEachView(new StatusBarKeyguardViewManager$$ExternalSyntheticLambda0());
        }
    }

    public void onFinishedGoingToSleep() {
        this.mBouncer.onScreenTurnedOff();
    }

    public void onRemoteInputActive(boolean z) {
        this.mRemoteInputActive = z;
        updateStates();
    }

    public final void setDozing(boolean z) {
        if (this.mDozing != z) {
            this.mDozing = z;
            if (z || this.mBouncer.needsFullscreenBouncer() || this.mOccluded) {
                reset(z);
            }
            updateStates();
            if (!z) {
                launchPendingWakeupAction();
            }
        }
    }

    public void setPulsing(boolean z) {
        if (this.mPulsing != z) {
            this.mPulsing = z;
            updateStates();
        }
    }

    public void setNeedsInput(boolean z) {
        this.mNotificationShadeWindowController.setKeyguardNeedsInput(z);
    }

    public boolean isUnlockWithWallpaper() {
        return this.mNotificationShadeWindowController.isShowingWallpaper();
    }

    public void setOccluded(boolean z, boolean z2) {
        boolean z3 = this.mOccluded;
        boolean z4 = true;
        boolean z5 = !z3 && z;
        boolean z6 = z3 && !z;
        setOccludedAndUpdateStates(z);
        boolean z7 = this.mShowing;
        if (z7 && z5) {
            SysUiStatsLog.write(62, 3);
            if (this.mCentralSurfaces.isInLaunchTransition()) {
                AnonymousClass4 r6 = new Runnable() {
                    public void run() {
                        StatusBarKeyguardViewManager.this.mNotificationShadeWindowController.setKeyguardOccluded(StatusBarKeyguardViewManager.this.mOccluded);
                        StatusBarKeyguardViewManager.this.reset(true);
                    }
                };
                this.mCentralSurfaces.fadeKeyguardAfterLaunchTransition((Runnable) null, r6, r6);
                return;
            } else if (this.mCentralSurfaces.isLaunchingActivityOverLockscreen()) {
                this.mShadeController.get().addPostCollapseAction(new StatusBarKeyguardViewManager$$ExternalSyntheticLambda5(this));
                return;
            }
        } else if (z7 && z6) {
            SysUiStatsLog.write(62, 2);
        }
        if (this.mShowing) {
            NotificationMediaManager notificationMediaManager = this.mMediaManager;
            if (!z2 || this.mOccluded) {
                z4 = false;
            }
            notificationMediaManager.updateMediaMetaData(false, z4);
        }
        this.mNotificationShadeWindowController.setKeyguardOccluded(this.mOccluded);
        if (!this.mDozing) {
            reset(z5);
        }
        if (z2 && !this.mOccluded && this.mShowing && !this.mBouncer.isShowing()) {
            this.mCentralSurfaces.animateKeyguardUnoccluding();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setOccluded$2() {
        this.mNotificationShadeWindowController.setKeyguardOccluded(this.mOccluded);
        reset(true);
    }

    public final void setOccludedAndUpdateStates(boolean z) {
        this.mOccluded = z;
        updateStates();
    }

    public boolean isOccluded() {
        return this.mOccluded;
    }

    public void startPreHideAnimation(Runnable runnable) {
        if (this.mBouncer.isShowing()) {
            this.mBouncer.startPreHideAnimation(runnable);
            this.mCentralSurfaces.onBouncerPreHideAnimation();
            if (this.mDismissActionWillAnimateOnKeyguard) {
                updateStates();
            }
        } else if (runnable != null) {
            runnable.run();
        }
        this.mNotificationPanelViewController.blockExpansionForCurrentTouch();
    }

    public void blockPanelExpansionFromCurrentTouch() {
        this.mNotificationPanelViewController.blockExpansionForCurrentTouch();
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x007b  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0097  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void hide(long r19, long r21) {
        /*
            r18 = this;
            r0 = r18
            java.lang.String r1 = "StatusBarKeyguardViewManager#hide"
            android.os.Trace.beginSection(r1)
            r1 = 0
            r0.mShowing = r1
            com.android.systemui.statusbar.policy.KeyguardStateController r2 = r0.mKeyguardStateController
            boolean r3 = r2.isOccluded()
            r2.notifyKeyguardState(r1, r3)
            r18.launchPendingWakeupAction()
            com.android.keyguard.KeyguardUpdateMonitor r2 = r0.mKeyguardUpdateManager
            boolean r2 = r2.needsSlowUnlockTransition()
            if (r2 == 0) goto L_0x0021
            r2 = 2000(0x7d0, double:9.88E-321)
            goto L_0x0023
        L_0x0021:
            r2 = r21
        L_0x0023:
            long r4 = android.os.SystemClock.uptimeMillis()
            r6 = -48
            long r6 = r19 + r6
            long r6 = r6 - r4
            r4 = 0
            long r6 = java.lang.Math.max(r4, r6)
            com.android.systemui.statusbar.phone.CentralSurfaces r8 = r0.mCentralSurfaces
            boolean r8 = r8.isInLaunchTransition()
            r15 = 1
            if (r8 != 0) goto L_0x00e2
            com.android.systemui.statusbar.policy.KeyguardStateController r8 = r0.mKeyguardStateController
            boolean r8 = r8.isFlingingToDismissKeyguard()
            if (r8 == 0) goto L_0x0045
            goto L_0x00e2
        L_0x0045:
            r18.executeAfterKeyguardGoneAction()
            com.android.systemui.statusbar.phone.BiometricUnlockController r8 = r0.mBiometricUnlockController
            int r8 = r8.getMode()
            r9 = 2
            if (r8 != r9) goto L_0x0054
            r16 = r15
            goto L_0x0056
        L_0x0054:
            r16 = r1
        L_0x0056:
            boolean r17 = r18.needsBypassFading()
            if (r17 == 0) goto L_0x0060
            r2 = 67
        L_0x005e:
            r11 = r4
            goto L_0x0066
        L_0x0060:
            if (r16 == 0) goto L_0x0065
            r2 = 240(0xf0, double:1.186E-321)
            goto L_0x005e
        L_0x0065:
            r11 = r6
        L_0x0066:
            com.android.systemui.statusbar.phone.CentralSurfaces r8 = r0.mCentralSurfaces
            r9 = r19
            r13 = r2
            r4 = r15
            r15 = r17
            r8.setKeyguardFadingAway(r9, r11, r13, r15)
            com.android.systemui.statusbar.phone.BiometricUnlockController r5 = r0.mBiometricUnlockController
            r5.startKeyguardFadingAway()
            r0.hideBouncer(r4)
            if (r16 == 0) goto L_0x0097
            if (r17 == 0) goto L_0x008e
            com.android.systemui.statusbar.phone.NotificationPanelViewController r5 = r0.mNotificationPanelViewController
            android.view.ViewGroup r5 = r5.getView()
            android.view.View r6 = r0.mNotificationContainer
            com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager$$ExternalSyntheticLambda3 r7 = new com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager$$ExternalSyntheticLambda3
            r7.<init>(r0)
            com.android.systemui.statusbar.notification.ViewGroupFadeHelper.fadeOutAllChildrenExcept(r5, r6, r2, r7)
            goto L_0x0093
        L_0x008e:
            com.android.systemui.statusbar.phone.CentralSurfaces r2 = r0.mCentralSurfaces
            r2.fadeKeyguardWhilePulsing()
        L_0x0093:
            r18.wakeAndUnlockDejank()
            goto L_0x00d4
        L_0x0097:
            com.android.systemui.statusbar.SysuiStatusBarStateController r5 = r0.mStatusBarStateController
            boolean r5 = r5.leaveOpenOnKeyguardHide()
            if (r5 != 0) goto L_0x00c5
            com.android.systemui.statusbar.NotificationShadeWindowController r5 = r0.mNotificationShadeWindowController
            r5.setKeyguardFadingAway(r4)
            if (r17 == 0) goto L_0x00b7
            com.android.systemui.statusbar.phone.NotificationPanelViewController r5 = r0.mNotificationPanelViewController
            android.view.ViewGroup r5 = r5.getView()
            android.view.View r6 = r0.mNotificationContainer
            com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager$$ExternalSyntheticLambda4 r7 = new com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager$$ExternalSyntheticLambda4
            r7.<init>(r0)
            com.android.systemui.statusbar.notification.ViewGroupFadeHelper.fadeOutAllChildrenExcept(r5, r6, r2, r7)
            goto L_0x00bc
        L_0x00b7:
            com.android.systemui.statusbar.phone.CentralSurfaces r2 = r0.mCentralSurfaces
            r2.hideKeyguard()
        L_0x00bc:
            com.android.systemui.statusbar.phone.CentralSurfaces r2 = r0.mCentralSurfaces
            r2.updateScrimController()
            r18.wakeAndUnlockDejank()
            goto L_0x00d4
        L_0x00c5:
            com.android.systemui.statusbar.phone.CentralSurfaces r2 = r0.mCentralSurfaces
            r2.hideKeyguard()
            com.android.systemui.statusbar.phone.CentralSurfaces r2 = r0.mCentralSurfaces
            r2.finishKeyguardFadingAway()
            com.android.systemui.statusbar.phone.BiometricUnlockController r2 = r0.mBiometricUnlockController
            r2.finishKeyguardFadingAway()
        L_0x00d4:
            r18.updateStates()
            com.android.systemui.statusbar.NotificationShadeWindowController r2 = r0.mNotificationShadeWindowController
            r2.setKeyguardShowing(r1)
            com.android.keyguard.ViewMediatorCallback r0 = r0.mViewMediatorCallback
            r0.keyguardGone()
            goto L_0x00fd
        L_0x00e2:
            r4 = r15
            com.android.systemui.statusbar.policy.KeyguardStateController r1 = r0.mKeyguardStateController
            boolean r1 = r1.isFlingingToDismissKeyguard()
            com.android.systemui.statusbar.phone.CentralSurfaces r2 = r0.mCentralSurfaces
            com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager$5 r3 = new com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager$5
            r3.<init>()
            com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager$6 r5 = new com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager$6
            r5.<init>(r1)
            com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager$7 r6 = new com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager$7
            r6.<init>(r1)
            r2.fadeKeyguardAfterLaunchTransition(r3, r5, r6)
        L_0x00fd:
            r0 = 62
            com.android.systemui.shared.system.SysUiStatsLog.write(r0, r4)
            android.os.Trace.endSection()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager.hide(long, long):void");
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$hide$3() {
        this.mCentralSurfaces.hideKeyguard();
        onKeyguardFadedAway();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$hide$4() {
        this.mCentralSurfaces.hideKeyguard();
    }

    public final boolean needsBypassFading() {
        if ((this.mBiometricUnlockController.getMode() == 7 || this.mBiometricUnlockController.getMode() == 2 || this.mBiometricUnlockController.getMode() == 1) && this.mBypassController.getBypassEnabled()) {
            return true;
        }
        return false;
    }

    public void onNavigationModeChanged(int i) {
        boolean isGesturalMode = QuickStepContract.isGesturalMode(i);
        if (isGesturalMode != this.mGesturalNav) {
            this.mGesturalNav = isGesturalMode;
            updateStates();
        }
    }

    public void onThemeChanged() {
        boolean isShowing = this.mBouncer.isShowing();
        boolean isScrimmed = this.mBouncer.isScrimmed();
        hideBouncer(true);
        this.mBouncer.prepare();
        if (isShowing) {
            showBouncer(isScrimmed);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onKeyguardFadedAway$5() {
        this.mNotificationShadeWindowController.setKeyguardFadingAway(false);
    }

    public void onKeyguardFadedAway() {
        this.mNotificationContainer.postDelayed(new StatusBarKeyguardViewManager$$ExternalSyntheticLambda1(this), 100);
        ViewGroupFadeHelper.reset(this.mNotificationPanelViewController.getView());
        this.mCentralSurfaces.finishKeyguardFadingAway();
        this.mBiometricUnlockController.finishKeyguardFadingAway();
        WindowManagerGlobal.getInstance().trimMemory(20);
    }

    public final void wakeAndUnlockDejank() {
        if (this.mBiometricUnlockController.isWakeAndUnlock() && this.mLatencyTracker.isEnabled()) {
            this.mLatencyTracker.onActionEnd(this.mBiometricUnlockController.getBiometricType() == BiometricSourceType.FACE ? 7 : 2);
        }
    }

    public final void executeAfterKeyguardGoneAction() {
        ActivityStarter.OnDismissAction onDismissAction = this.mAfterKeyguardGoneAction;
        if (onDismissAction != null) {
            onDismissAction.onDismiss();
            this.mAfterKeyguardGoneAction = null;
        }
        this.mKeyguardGoneCancelAction = null;
        this.mDismissActionWillAnimateOnKeyguard = false;
        for (int i = 0; i < this.mAfterKeyguardGoneRunnables.size(); i++) {
            this.mAfterKeyguardGoneRunnables.get(i).run();
        }
        this.mAfterKeyguardGoneRunnables.clear();
    }

    public void dismissAndCollapse() {
        this.mCentralSurfaces.executeRunnableDismissingKeyguard((Runnable) null, (Runnable) null, true, false, true);
    }

    public boolean isSecure() {
        return this.mBouncer.isSecure();
    }

    public boolean isShowing() {
        return this.mShowing;
    }

    public boolean onBackPressed(boolean z) {
        if (!this.mBouncer.isShowing()) {
            return false;
        }
        this.mCentralSurfaces.endAffordanceLaunch();
        if (!this.mBouncer.isScrimmed() || this.mBouncer.needsFullscreenBouncer()) {
            reset(z);
            return true;
        }
        hideBouncer(false);
        updateStates();
        return true;
    }

    public boolean isBouncerShowing() {
        return this.mBouncer.isShowing() || isShowingAlternateAuth();
    }

    public boolean bouncerIsOrWillBeShowing() {
        return isBouncerShowing() || this.mBouncer.getShowingSoon();
    }

    public void cancelPostAuthActions() {
        if (!bouncerIsOrWillBeShowing()) {
            this.mAfterKeyguardGoneAction = null;
            this.mDismissActionWillAnimateOnKeyguard = false;
            Runnable runnable = this.mKeyguardGoneCancelAction;
            if (runnable != null) {
                runnable.run();
                this.mKeyguardGoneCancelAction = null;
            }
        }
    }

    public final long getNavBarShowDelay() {
        if (this.mKeyguardStateController.isKeyguardFadingAway()) {
            return this.mKeyguardStateController.getKeyguardFadingAwayDelay();
        }
        return this.mBouncer.isShowing() ? 320 : 0;
    }

    public void updateStates() {
        boolean z = this.mShowing;
        boolean z2 = this.mOccluded;
        boolean isShowing = this.mBouncer.isShowing();
        boolean bouncerIsOrWillBeShowing = bouncerIsOrWillBeShowing();
        boolean z3 = true;
        boolean z4 = !this.mBouncer.isFullscreenBouncer();
        boolean z5 = this.mRemoteInputActive;
        if ((z4 || !z || z5) != (this.mLastBouncerDismissible || !this.mLastShowing || this.mLastRemoteInputActive) || this.mFirstUpdate) {
            if (z4 || !z || z5) {
                this.mBouncer.setBackButtonEnabled(true);
            } else {
                this.mBouncer.setBackButtonEnabled(false);
            }
        }
        boolean isNavBarVisible = isNavBarVisible();
        if (isNavBarVisible != getLastNavBarVisible() || this.mFirstUpdate) {
            updateNavigationBarVisibility(isNavBarVisible);
        }
        if (isShowing != this.mLastBouncerShowing || this.mFirstUpdate) {
            this.mNotificationShadeWindowController.setBouncerShowing(isShowing);
            this.mCentralSurfaces.setBouncerShowing(isShowing);
            this.mKeyguardMessageAreaController.setBouncerShowing(isShowing);
        }
        if (z2 != this.mLastOccluded || this.mFirstUpdate) {
            this.mKeyguardUpdateManager.onKeyguardOccludedChanged(z2);
            this.mKeyguardStateController.notifyKeyguardState(z, z2);
        }
        if ((z && !z2) != (this.mLastShowing && !this.mLastOccluded) || this.mFirstUpdate) {
            KeyguardUpdateMonitor keyguardUpdateMonitor = this.mKeyguardUpdateManager;
            if (!z || z2) {
                z3 = false;
            }
            keyguardUpdateMonitor.onKeyguardVisibilityChanged(z3);
        }
        if (!(bouncerIsOrWillBeShowing == this.mLastBouncerIsOrWillBeShowing && !this.mFirstUpdate && isShowing == this.mLastBouncerShowing)) {
            this.mKeyguardUpdateManager.sendKeyguardBouncerChanged(bouncerIsOrWillBeShowing, isShowing);
        }
        this.mFirstUpdate = false;
        this.mLastShowing = z;
        this.mLastGlobalActionsVisible = this.mGlobalActionsVisible;
        this.mLastOccluded = z2;
        this.mLastBouncerShowing = isShowing;
        this.mLastBouncerIsOrWillBeShowing = bouncerIsOrWillBeShowing;
        this.mLastBouncerDismissible = z4;
        this.mLastRemoteInputActive = z5;
        this.mLastDozing = this.mDozing;
        this.mLastPulsing = this.mPulsing;
        this.mLastScreenOffAnimationPlaying = this.mScreenOffAnimationPlaying;
        this.mLastBiometricMode = this.mBiometricUnlockController.getMode();
        this.mLastGesturalNav = this.mGesturalNav;
        this.mLastIsDocked = this.mIsDocked;
        this.mCentralSurfaces.onKeyguardViewManagerStatesUpdated();
    }

    public void updateNavigationBarVisibility(boolean z) {
        if (this.mCentralSurfaces.getNavigationBarView() == null) {
            return;
        }
        if (z) {
            long navBarShowDelay = getNavBarShowDelay();
            if (navBarShowDelay == 0) {
                this.mMakeNavigationBarVisibleRunnable.run();
            } else {
                this.mNotificationContainer.postOnAnimationDelayed(this.mMakeNavigationBarVisibleRunnable, navBarShowDelay);
            }
        } else {
            this.mNotificationContainer.removeCallbacks(this.mMakeNavigationBarVisibleRunnable);
            this.mCentralSurfaces.getNotificationShadeWindowView().getWindowInsetsController().hide(WindowInsets.Type.navigationBars());
        }
    }

    public boolean isNavBarVisible() {
        BiometricUnlockController biometricUnlockController = this.mBiometricUnlockController;
        boolean z = biometricUnlockController != null && biometricUnlockController.getMode() == 2;
        boolean z2 = this.mShowing && !this.mOccluded;
        boolean z3 = this.mDozing;
        boolean z4 = z3 && !z;
        boolean z5 = ((z2 && !z3 && !this.mScreenOffAnimationPlaying) || (this.mPulsing && !this.mIsDocked)) && this.mGesturalNav;
        if ((z2 || z4 || this.mScreenOffAnimationPlaying) && !this.mBouncer.isShowing() && !this.mRemoteInputActive && !z5 && !this.mGlobalActionsVisible) {
            return false;
        }
        return true;
    }

    public boolean getLastNavBarVisible() {
        boolean z = this.mLastShowing && !this.mLastOccluded;
        boolean z2 = this.mLastDozing;
        boolean z3 = z2 && this.mLastBiometricMode != 2;
        boolean z4 = ((z && !z2 && !this.mLastScreenOffAnimationPlaying) || (this.mLastPulsing && !this.mLastIsDocked)) && this.mLastGesturalNav;
        if ((z || z3 || this.mLastScreenOffAnimationPlaying) && !this.mLastBouncerShowing && !this.mLastRemoteInputActive && !z4 && !this.mLastGlobalActionsVisible) {
            return false;
        }
        return true;
    }

    public boolean shouldDismissOnMenuPressed() {
        return this.mBouncer.shouldDismissOnMenuPressed();
    }

    public boolean interceptMediaKey(KeyEvent keyEvent) {
        return this.mBouncer.interceptMediaKey(keyEvent);
    }

    public boolean dispatchBackKeyEventPreIme() {
        return this.mBouncer.dispatchBackKeyEventPreIme();
    }

    public void readyForKeyguardDone() {
        this.mViewMediatorCallback.readyForKeyguardDone();
    }

    public boolean shouldDisableWindowAnimationsForUnlock() {
        return this.mCentralSurfaces.isInLaunchTransition();
    }

    public boolean shouldSubtleWindowAnimationsForUnlock() {
        return needsBypassFading();
    }

    public boolean isGoingToNotificationShade() {
        return this.mStatusBarStateController.leaveOpenOnKeyguardHide();
    }

    public void keyguardGoingAway() {
        this.mCentralSurfaces.keyguardGoingAway();
    }

    public void setKeyguardGoingAwayState(boolean z) {
        this.mNotificationShadeWindowController.setKeyguardGoingAway(z);
    }

    public void notifyKeyguardAuthenticated(boolean z) {
        this.mBouncer.notifyKeyguardAuthenticated(z);
        if (this.mAlternateAuthInterceptor != null && isShowingAlternateAuthOrAnimating()) {
            resetAlternateAuth(false);
            executeAfterKeyguardGoneAction();
        }
    }

    public void showBouncerMessage(String str, ColorStateList colorStateList) {
        if (isShowingAlternateAuth()) {
            KeyguardMessageAreaController keyguardMessageAreaController = this.mKeyguardMessageAreaController;
            if (keyguardMessageAreaController != null) {
                keyguardMessageAreaController.setMessage((CharSequence) str);
                return;
            }
            return;
        }
        this.mBouncer.showMessage(str, colorStateList);
    }

    public ViewRootImpl getViewRootImpl() {
        return this.mNotificationShadeWindowController.getNotificationShadeView().getViewRootImpl();
    }

    public void launchPendingWakeupAction() {
        DismissWithActionRequest dismissWithActionRequest = this.mPendingWakeupAction;
        this.mPendingWakeupAction = null;
        if (dismissWithActionRequest == null) {
            return;
        }
        if (this.mShowing) {
            dismissWithAction(dismissWithActionRequest.dismissAction, dismissWithActionRequest.cancelAction, dismissWithActionRequest.afterKeyguardGone, dismissWithActionRequest.message);
            return;
        }
        ActivityStarter.OnDismissAction onDismissAction = dismissWithActionRequest.dismissAction;
        if (onDismissAction != null) {
            onDismissAction.onDismiss();
        }
    }

    public void cancelPendingWakeupAction() {
        Runnable runnable;
        DismissWithActionRequest dismissWithActionRequest = this.mPendingWakeupAction;
        this.mPendingWakeupAction = null;
        if (dismissWithActionRequest != null && (runnable = dismissWithActionRequest.cancelAction) != null) {
            runnable.run();
        }
    }

    public boolean bouncerNeedsScrimming() {
        return (this.mOccluded && !this.mDreamOverlayStateController.isOverlayActive()) || this.mBouncer.willDismissWithAction() || (this.mBouncer.isShowing() && this.mBouncer.isScrimmed()) || this.mBouncer.isFullscreenBouncer();
    }

    public void updateResources() {
        KeyguardBouncer keyguardBouncer = this.mBouncer;
        if (keyguardBouncer != null) {
            keyguardBouncer.updateResources();
        }
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("StatusBarKeyguardViewManager:");
        printWriter.println("  mShowing: " + this.mShowing);
        printWriter.println("  mOccluded: " + this.mOccluded);
        printWriter.println("  mRemoteInputActive: " + this.mRemoteInputActive);
        printWriter.println("  mDozing: " + this.mDozing);
        printWriter.println("  mAfterKeyguardGoneAction: " + this.mAfterKeyguardGoneAction);
        printWriter.println("  mAfterKeyguardGoneRunnables: " + this.mAfterKeyguardGoneRunnables);
        printWriter.println("  mPendingWakeupAction: " + this.mPendingWakeupAction);
        printWriter.println("  isBouncerShowing(): " + isBouncerShowing());
        printWriter.println("  bouncerIsOrWillBeShowing(): " + bouncerIsOrWillBeShowing());
        KeyguardBouncer keyguardBouncer = this.mBouncer;
        if (keyguardBouncer != null) {
            keyguardBouncer.dump(printWriter);
        }
        if (this.mAlternateAuthInterceptor != null) {
            printWriter.println("AltAuthInterceptor: ");
            this.mAlternateAuthInterceptor.dump(printWriter);
        }
    }

    public void onDozingChanged(boolean z) {
        setDozing(z);
    }

    public void onFoldToAodAnimationChanged() {
        FoldAodAnimationController foldAodAnimationController = this.mFoldAodAnimationController;
        if (foldAodAnimationController != null) {
            this.mScreenOffAnimationPlaying = foldAodAnimationController.shouldPlayAnimation();
        }
    }

    public float getQsExpansion() {
        return this.mQsExpansion;
    }

    public void setQsExpansion(float f) {
        this.mQsExpansion = f;
        AlternateAuthInterceptor alternateAuthInterceptor = this.mAlternateAuthInterceptor;
        if (alternateAuthInterceptor != null) {
            alternateAuthInterceptor.setQsExpansion(f);
        }
    }

    public KeyguardBouncer getBouncer() {
        return this.mBouncer;
    }

    public boolean isShowingAlternateAuth() {
        AlternateAuthInterceptor alternateAuthInterceptor = this.mAlternateAuthInterceptor;
        return alternateAuthInterceptor != null && alternateAuthInterceptor.isShowingAlternateAuthBouncer();
    }

    public boolean isShowingAlternateAuthOrAnimating() {
        AlternateAuthInterceptor alternateAuthInterceptor = this.mAlternateAuthInterceptor;
        return alternateAuthInterceptor != null && (alternateAuthInterceptor.isShowingAlternateAuthBouncer() || this.mAlternateAuthInterceptor.isAnimating());
    }

    public boolean onTouch(MotionEvent motionEvent) {
        AlternateAuthInterceptor alternateAuthInterceptor = this.mAlternateAuthInterceptor;
        if (alternateAuthInterceptor == null) {
            return false;
        }
        return alternateAuthInterceptor.onTouch(motionEvent);
    }

    public void updateKeyguardPosition(float f) {
        KeyguardBouncer keyguardBouncer = this.mBouncer;
        if (keyguardBouncer != null) {
            keyguardBouncer.updateKeyguardPosition(f);
        }
    }

    public static class DismissWithActionRequest {
        public final boolean afterKeyguardGone;
        public final Runnable cancelAction;
        public final ActivityStarter.OnDismissAction dismissAction;
        public final String message;

        public DismissWithActionRequest(ActivityStarter.OnDismissAction onDismissAction, Runnable runnable, boolean z, String str) {
            this.dismissAction = onDismissAction;
            this.cancelAction = runnable;
            this.afterKeyguardGone = z;
            this.message = str;
        }
    }

    public void requestFace(boolean z) {
        this.mKeyguardUpdateManager.requestFaceAuthOnOccludingApp(z);
    }

    public void requestFp(boolean z, int i) {
        this.mKeyguardUpdateManager.requestFingerprintAuthOnOccludingApp(z);
        AlternateAuthInterceptor alternateAuthInterceptor = this.mAlternateAuthInterceptor;
        if (alternateAuthInterceptor != null) {
            alternateAuthInterceptor.requestUdfps(z, i);
        }
    }

    public boolean isBouncerInTransit() {
        KeyguardBouncer keyguardBouncer = this.mBouncer;
        if (keyguardBouncer == null) {
            return false;
        }
        return keyguardBouncer.inTransit();
    }
}
