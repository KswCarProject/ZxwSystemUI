package com.android.systemui.statusbar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.util.IndentingPrintWriter;
import android.util.MathUtils;
import android.view.View;
import com.android.systemui.Dumpable;
import com.android.systemui.R$dimen;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.biometrics.UdfpsKeyguardViewController;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.media.MediaHierarchyManager;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QS;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.LockscreenShadeKeyguardTransitionController;
import com.android.systemui.statusbar.SingleShadeLockScreenOverScroller;
import com.android.systemui.statusbar.SplitShadeLockScreenOverScroller;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.stack.AmbientState;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.LSShadeTransitionLogger;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.LargeScreenUtils;
import java.io.PrintWriter;
import kotlin.Lazy;
import kotlin.LazyKt__LazyJVMKt;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: LockscreenShadeTransitionController.kt */
public final class LockscreenShadeTransitionController implements Dumpable {
    @NotNull
    public final AmbientState ambientState;
    @Nullable
    public Function1<? super Long, Unit> animationHandlerOnKeyguardDismiss;
    public CentralSurfaces centralSurfaces;
    @NotNull
    public final Context context;
    @NotNull
    public final NotificationShadeDepthController depthController;
    public int depthControllerTransitionDistance;
    public float dragDownAmount;
    @Nullable
    public ValueAnimator dragDownAnimator;
    @Nullable
    public NotificationEntry draggedDownEntry;
    @NotNull
    public final FalsingCollector falsingCollector;
    public boolean forceApplyAmount;
    public float fractionToShade;
    public int fullTransitionDistance;
    public int fullTransitionDistanceByTap;
    public boolean isWakingToShadeLocked;
    @NotNull
    public final KeyguardBypassController keyguardBypassController;
    @NotNull
    public final Lazy keyguardTransitionController$delegate = LazyKt__LazyJVMKt.lazy(new LockscreenShadeTransitionController$keyguardTransitionController$2(this));
    @NotNull
    public final LockscreenShadeKeyguardTransitionController.Factory keyguardTransitionControllerFactory;
    @NotNull
    public final NotificationLockscreenUserManager lockScreenUserManager;
    @NotNull
    public final LSShadeTransitionLogger logger;
    @NotNull
    public final MediaHierarchyManager mediaHierarchyManager;
    public boolean nextHideKeyguardNeedsNoAnimation;
    public NotificationPanelViewController notificationPanelController;
    public int notificationShelfTransitionDistance;
    public NotificationStackScrollLayoutController nsslController;
    @NotNull
    public final Lazy phoneShadeOverScroller$delegate = LazyKt__LazyJVMKt.lazy(new LockscreenShadeTransitionController$phoneShadeOverScroller$2(this));
    public float pulseHeight;
    @Nullable
    public ValueAnimator pulseHeightAnimator;
    public QS qS;
    public float qSDragProgress;
    public int qsTransitionDistance;
    @NotNull
    public final LockscreenShadeScrimTransitionController scrimTransitionController;
    @NotNull
    public final SingleShadeLockScreenOverScroller.Factory singleShadeOverScrollerFactory;
    @NotNull
    public final Lazy splitShadeOverScroller$delegate = LazyKt__LazyJVMKt.lazy(new LockscreenShadeTransitionController$splitShadeOverScroller$2(this));
    @NotNull
    public final SplitShadeLockScreenOverScroller.Factory splitShadeOverScrollerFactory;
    @NotNull
    public final SysuiStatusBarStateController statusBarStateController;
    public int statusBarTransitionDistance;
    @NotNull
    public final DragDownHelper touchHelper;
    @Nullable
    public UdfpsKeyguardViewController udfpsKeyguardViewController;
    public int udfpsTransitionDistance;
    public boolean useSplitShade;

    public static /* synthetic */ void getDragDownAnimator$frameworks__base__packages__SystemUI__android_common__SystemUI_core$annotations() {
    }

    public static /* synthetic */ void getPulseHeightAnimator$frameworks__base__packages__SystemUI__android_common__SystemUI_core$annotations() {
    }

    public final void goToLockedShade(@Nullable View view) {
        goToLockedShade$default(this, view, false, 2, (Object) null);
    }

    public LockscreenShadeTransitionController(@NotNull SysuiStatusBarStateController sysuiStatusBarStateController, @NotNull LSShadeTransitionLogger lSShadeTransitionLogger, @NotNull KeyguardBypassController keyguardBypassController2, @NotNull NotificationLockscreenUserManager notificationLockscreenUserManager, @NotNull FalsingCollector falsingCollector2, @NotNull AmbientState ambientState2, @NotNull MediaHierarchyManager mediaHierarchyManager2, @NotNull LockscreenShadeScrimTransitionController lockscreenShadeScrimTransitionController, @NotNull LockscreenShadeKeyguardTransitionController.Factory factory, @NotNull NotificationShadeDepthController notificationShadeDepthController, @NotNull Context context2, @NotNull SplitShadeLockScreenOverScroller.Factory factory2, @NotNull SingleShadeLockScreenOverScroller.Factory factory3, @NotNull WakefulnessLifecycle wakefulnessLifecycle, @NotNull ConfigurationController configurationController, @NotNull FalsingManager falsingManager, @NotNull DumpManager dumpManager) {
        Context context3 = context2;
        this.statusBarStateController = sysuiStatusBarStateController;
        this.logger = lSShadeTransitionLogger;
        this.keyguardBypassController = keyguardBypassController2;
        this.lockScreenUserManager = notificationLockscreenUserManager;
        this.falsingCollector = falsingCollector2;
        this.ambientState = ambientState2;
        this.mediaHierarchyManager = mediaHierarchyManager2;
        this.scrimTransitionController = lockscreenShadeScrimTransitionController;
        this.keyguardTransitionControllerFactory = factory;
        this.depthController = notificationShadeDepthController;
        this.context = context3;
        this.splitShadeOverScrollerFactory = factory2;
        this.singleShadeOverScrollerFactory = factory3;
        this.touchHelper = new DragDownHelper(falsingManager, falsingCollector2, this, context3);
        updateResources();
        configurationController.addCallback(new ConfigurationController.ConfigurationListener(this) {
            public final /* synthetic */ LockscreenShadeTransitionController this$0;

            {
                this.this$0 = r1;
            }

            public void onConfigChanged(@Nullable Configuration configuration) {
                this.this$0.updateResources();
                this.this$0.getTouchHelper().updateResources(this.this$0.context);
            }
        });
        dumpManager.registerDumpable(this);
        sysuiStatusBarStateController.addCallback(new StatusBarStateController.StateListener(this) {
            public final /* synthetic */ LockscreenShadeTransitionController this$0;

            {
                this.this$0 = r1;
            }

            public void onExpandedChanged(boolean z) {
                if (!z) {
                    boolean z2 = true;
                    if (!(this.this$0.getDragDownAmount$frameworks__base__packages__SystemUI__android_common__SystemUI_core() == 0.0f)) {
                        ValueAnimator dragDownAnimator$frameworks__base__packages__SystemUI__android_common__SystemUI_core = this.this$0.getDragDownAnimator$frameworks__base__packages__SystemUI__android_common__SystemUI_core();
                        if (!(dragDownAnimator$frameworks__base__packages__SystemUI__android_common__SystemUI_core != null && dragDownAnimator$frameworks__base__packages__SystemUI__android_common__SystemUI_core.isRunning())) {
                            this.this$0.logger.logDragDownAmountResetWhenFullyCollapsed();
                            this.this$0.setDragDownAmount$frameworks__base__packages__SystemUI__android_common__SystemUI_core(0.0f);
                        }
                    }
                    if (!(this.this$0.pulseHeight == 0.0f)) {
                        ValueAnimator pulseHeightAnimator$frameworks__base__packages__SystemUI__android_common__SystemUI_core = this.this$0.getPulseHeightAnimator$frameworks__base__packages__SystemUI__android_common__SystemUI_core();
                        if (pulseHeightAnimator$frameworks__base__packages__SystemUI__android_common__SystemUI_core == null || !pulseHeightAnimator$frameworks__base__packages__SystemUI__android_common__SystemUI_core.isRunning()) {
                            z2 = false;
                        }
                        if (!z2) {
                            this.this$0.logger.logPulseHeightNotResetWhenFullyCollapsed();
                            this.this$0.setPulseHeight(0.0f, false);
                        }
                    }
                }
            }
        });
        wakefulnessLifecycle.addObserver(new WakefulnessLifecycle.Observer(this) {
            public final /* synthetic */ LockscreenShadeTransitionController this$0;

            {
                this.this$0 = r1;
            }

            public void onPostFinishedWakingUp() {
                this.this$0.isWakingToShadeLocked = false;
            }
        });
    }

    public final float getFractionToShade() {
        return this.fractionToShade;
    }

    @NotNull
    public final NotificationPanelViewController getNotificationPanelController() {
        NotificationPanelViewController notificationPanelViewController = this.notificationPanelController;
        if (notificationPanelViewController != null) {
            return notificationPanelViewController;
        }
        return null;
    }

    public final void setNotificationPanelController(@NotNull NotificationPanelViewController notificationPanelViewController) {
        this.notificationPanelController = notificationPanelViewController;
    }

    @NotNull
    public final CentralSurfaces getCentralSurfaces() {
        CentralSurfaces centralSurfaces2 = this.centralSurfaces;
        if (centralSurfaces2 != null) {
            return centralSurfaces2;
        }
        return null;
    }

    public final void setCentralSurfaces(@NotNull CentralSurfaces centralSurfaces2) {
        this.centralSurfaces = centralSurfaces2;
    }

    @NotNull
    public final QS getQS() {
        QS qs = this.qS;
        if (qs != null) {
            return qs;
        }
        return null;
    }

    public final void setQS(@NotNull QS qs) {
        this.qS = qs;
    }

    @Nullable
    public final ValueAnimator getDragDownAnimator$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        return this.dragDownAnimator;
    }

    @Nullable
    public final ValueAnimator getPulseHeightAnimator$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        return this.pulseHeightAnimator;
    }

    public final boolean isWakingToShadeLocked() {
        return this.isWakingToShadeLocked;
    }

    public final int getDistanceUntilShowingPulsingNotifications() {
        return this.fullTransitionDistance;
    }

    @Nullable
    public final UdfpsKeyguardViewController getUdfpsKeyguardViewController() {
        return this.udfpsKeyguardViewController;
    }

    public final void setUdfpsKeyguardViewController(@Nullable UdfpsKeyguardViewController udfpsKeyguardViewController2) {
        this.udfpsKeyguardViewController = udfpsKeyguardViewController2;
    }

    @NotNull
    public final DragDownHelper getTouchHelper() {
        return this.touchHelper;
    }

    public final SplitShadeLockScreenOverScroller getSplitShadeOverScroller() {
        return (SplitShadeLockScreenOverScroller) this.splitShadeOverScroller$delegate.getValue();
    }

    public final SingleShadeLockScreenOverScroller getPhoneShadeOverScroller() {
        return (SingleShadeLockScreenOverScroller) this.phoneShadeOverScroller$delegate.getValue();
    }

    public final LockscreenShadeKeyguardTransitionController getKeyguardTransitionController() {
        return (LockscreenShadeKeyguardTransitionController) this.keyguardTransitionController$delegate.getValue();
    }

    public final LockScreenShadeOverScroller getShadeOverScroller() {
        return this.useSplitShade ? getSplitShadeOverScroller() : getPhoneShadeOverScroller();
    }

    public final void updateResources() {
        this.fullTransitionDistance = this.context.getResources().getDimensionPixelSize(R$dimen.lockscreen_shade_full_transition_distance);
        this.fullTransitionDistanceByTap = this.context.getResources().getDimensionPixelSize(R$dimen.lockscreen_shade_transition_by_tap_distance);
        this.notificationShelfTransitionDistance = this.context.getResources().getDimensionPixelSize(R$dimen.lockscreen_shade_notif_shelf_transition_distance);
        this.qsTransitionDistance = this.context.getResources().getDimensionPixelSize(R$dimen.lockscreen_shade_qs_transition_distance);
        this.depthControllerTransitionDistance = this.context.getResources().getDimensionPixelSize(R$dimen.lockscreen_shade_depth_controller_transition_distance);
        this.udfpsTransitionDistance = this.context.getResources().getDimensionPixelSize(R$dimen.lockscreen_shade_udfps_keyguard_transition_distance);
        this.statusBarTransitionDistance = this.context.getResources().getDimensionPixelSize(R$dimen.lockscreen_shade_status_bar_transition_distance);
        this.useSplitShade = LargeScreenUtils.shouldUseSplitNotificationShade(this.context.getResources());
    }

    public final void setStackScroller(@NotNull NotificationStackScrollLayoutController notificationStackScrollLayoutController) {
        this.nsslController = notificationStackScrollLayoutController;
        this.touchHelper.setHost(notificationStackScrollLayoutController.getView());
        this.touchHelper.setExpandCallback(notificationStackScrollLayoutController.getExpandHelperCallback());
    }

    public final void bindController(@NotNull NotificationShelfController notificationShelfController) {
        notificationShelfController.setOnClickListener(new LockscreenShadeTransitionController$bindController$1(this));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0012, code lost:
        if (r0.isInLockedDownShade() != false) goto L_0x0014;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean canDragDown$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        /*
            r2 = this;
            com.android.systemui.statusbar.SysuiStatusBarStateController r0 = r2.statusBarStateController
            int r0 = r0.getState()
            r1 = 1
            if (r0 == r1) goto L_0x0014
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController r0 = r2.nsslController
            if (r0 != 0) goto L_0x000e
            r0 = 0
        L_0x000e:
            boolean r0 = r0.isInLockedDownShade()
            if (r0 == 0) goto L_0x0023
        L_0x0014:
            com.android.systemui.plugins.qs.QS r0 = r2.getQS()
            boolean r0 = r0.isFullyCollapsed()
            if (r0 != 0) goto L_0x0024
            boolean r2 = r2.useSplitShade
            if (r2 == 0) goto L_0x0023
            goto L_0x0024
        L_0x0023:
            r1 = 0
        L_0x0024:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.LockscreenShadeTransitionController.canDragDown$frameworks__base__packages__SystemUI__android_common__SystemUI_core():boolean");
    }

    public final void onDraggedDown$frameworks__base__packages__SystemUI__android_common__SystemUI_core(@Nullable View view, int i) {
        if (canDragDown$frameworks__base__packages__SystemUI__android_common__SystemUI_core()) {
            LockscreenShadeTransitionController$onDraggedDown$cancelRunnable$1 lockscreenShadeTransitionController$onDraggedDown$cancelRunnable$1 = new LockscreenShadeTransitionController$onDraggedDown$cancelRunnable$1(this);
            NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.nsslController;
            if (notificationStackScrollLayoutController == null) {
                notificationStackScrollLayoutController = null;
            }
            if (notificationStackScrollLayoutController.isInLockedDownShade()) {
                this.logger.logDraggedDownLockDownShade(view);
                this.statusBarStateController.setLeaveOpenOnKeyguardHide(true);
                getCentralSurfaces().dismissKeyguardThenExecute(new LockscreenShadeTransitionController$onDraggedDown$1(this), lockscreenShadeTransitionController$onDraggedDown$cancelRunnable$1, false);
                return;
            }
            this.logger.logDraggedDown(view, i);
            if (!this.ambientState.isDozing() || view != null) {
                goToLockedShadeInternal(view, new LockscreenShadeTransitionController$onDraggedDown$animationHandler$1(view, this), lockscreenShadeTransitionController$onDraggedDown$cancelRunnable$1);
                return;
            }
            return;
        }
        this.logger.logUnSuccessfulDragDown(view);
        setDragDownAmountAnimated$default(this, 0.0f, 0, (Function0) null, 6, (Object) null);
    }

    public final void onDragDownReset$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        this.logger.logDragDownAborted();
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.nsslController;
        NotificationStackScrollLayoutController notificationStackScrollLayoutController2 = null;
        if (notificationStackScrollLayoutController == null) {
            notificationStackScrollLayoutController = null;
        }
        notificationStackScrollLayoutController.setDimmed(true, true);
        NotificationStackScrollLayoutController notificationStackScrollLayoutController3 = this.nsslController;
        if (notificationStackScrollLayoutController3 == null) {
            notificationStackScrollLayoutController3 = null;
        }
        notificationStackScrollLayoutController3.resetScrollPosition();
        NotificationStackScrollLayoutController notificationStackScrollLayoutController4 = this.nsslController;
        if (notificationStackScrollLayoutController4 != null) {
            notificationStackScrollLayoutController2 = notificationStackScrollLayoutController4;
        }
        notificationStackScrollLayoutController2.resetCheckSnoozeLeavebehind();
        setDragDownAmountAnimated$default(this, 0.0f, 0, (Function0) null, 6, (Object) null);
    }

    public final void onCrossedThreshold$frameworks__base__packages__SystemUI__android_common__SystemUI_core(boolean z) {
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.nsslController;
        if (notificationStackScrollLayoutController == null) {
            notificationStackScrollLayoutController = null;
        }
        notificationStackScrollLayoutController.setDimmed(!z, true);
    }

    public final void onDragDownStarted$frameworks__base__packages__SystemUI__android_common__SystemUI_core(@Nullable ExpandableView expandableView) {
        this.logger.logDragDownStarted(expandableView);
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.nsslController;
        NotificationStackScrollLayoutController notificationStackScrollLayoutController2 = null;
        if (notificationStackScrollLayoutController == null) {
            notificationStackScrollLayoutController = null;
        }
        notificationStackScrollLayoutController.cancelLongPress();
        NotificationStackScrollLayoutController notificationStackScrollLayoutController3 = this.nsslController;
        if (notificationStackScrollLayoutController3 != null) {
            notificationStackScrollLayoutController2 = notificationStackScrollLayoutController3;
        }
        notificationStackScrollLayoutController2.checkSnoozeLeavebehind();
        ValueAnimator valueAnimator = this.dragDownAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.logger.logAnimationCancelled(false);
            valueAnimator.cancel();
        }
    }

    public final boolean isFalsingCheckNeeded$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        return this.statusBarStateController.getState() == 1;
    }

    public final boolean isDragDownEnabledForView$frameworks__base__packages__SystemUI__android_common__SystemUI_core(@Nullable ExpandableView expandableView) {
        if (isDragDownAnywhereEnabled$frameworks__base__packages__SystemUI__android_common__SystemUI_core()) {
            return true;
        }
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.nsslController;
        if (notificationStackScrollLayoutController == null) {
            notificationStackScrollLayoutController = null;
        }
        if (!notificationStackScrollLayoutController.isInLockedDownShade()) {
            return false;
        }
        if (expandableView == null) {
            return true;
        }
        if (expandableView instanceof ExpandableNotificationRow) {
            return ((ExpandableNotificationRow) expandableView).getEntry().isSensitive();
        }
        return false;
    }

    public final boolean isDragDownAnywhereEnabled$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        if (this.statusBarStateController.getState() != 1 || this.keyguardBypassController.getBypassEnabled() || (!getQS().isFullyCollapsed() && !this.useSplitShade)) {
            return false;
        }
        return true;
    }

    public final float getDragDownAmount$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        return this.dragDownAmount;
    }

    public final void setDragDownAmount$frameworks__base__packages__SystemUI__android_common__SystemUI_core(float f) {
        boolean z = true;
        if (!(this.dragDownAmount == f) || this.forceApplyAmount) {
            this.dragDownAmount = f;
            NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.nsslController;
            NotificationStackScrollLayoutController notificationStackScrollLayoutController2 = null;
            if (notificationStackScrollLayoutController == null) {
                notificationStackScrollLayoutController = null;
            }
            if (notificationStackScrollLayoutController.isInLockedDownShade()) {
                if (this.dragDownAmount != 0.0f) {
                    z = false;
                }
                if (!z && !this.forceApplyAmount) {
                    return;
                }
            }
            this.fractionToShade = MathUtils.saturate(this.dragDownAmount / ((float) this.notificationShelfTransitionDistance));
            NotificationStackScrollLayoutController notificationStackScrollLayoutController3 = this.nsslController;
            if (notificationStackScrollLayoutController3 != null) {
                notificationStackScrollLayoutController2 = notificationStackScrollLayoutController3;
            }
            notificationStackScrollLayoutController2.setTransitionToFullShadeAmount(this.fractionToShade);
            this.qSDragProgress = MathUtils.saturate(this.dragDownAmount / ((float) this.qsTransitionDistance));
            getQS().setTransitionToFullShadeAmount(this.dragDownAmount, this.qSDragProgress);
            getNotificationPanelController().setTransitionToFullShadeAmount(this.dragDownAmount, false, 0);
            this.mediaHierarchyManager.setTransitionToFullShadeAmount(this.dragDownAmount);
            this.scrimTransitionController.setDragDownAmount(f);
            transitionToShadeAmountCommon(this.dragDownAmount);
            getKeyguardTransitionController().setDragDownAmount(f);
            getShadeOverScroller().setExpansionDragDownAmount(this.dragDownAmount);
        }
    }

    public final float getQSDragProgress() {
        return this.qSDragProgress;
    }

    public final void transitionToShadeAmountCommon(float f) {
        int i = this.depthControllerTransitionDistance;
        if (i == 0) {
            this.depthController.setTransitionToFullShadeProgress(0.0f);
        } else {
            this.depthController.setTransitionToFullShadeProgress(MathUtils.saturate(f / ((float) i)));
        }
        float saturate = MathUtils.saturate(f / ((float) this.udfpsTransitionDistance));
        UdfpsKeyguardViewController udfpsKeyguardViewController2 = this.udfpsKeyguardViewController;
        if (udfpsKeyguardViewController2 != null) {
            udfpsKeyguardViewController2.setTransitionToFullShadeProgress(saturate);
        }
        getCentralSurfaces().setTransitionToFullShadeProgress(MathUtils.saturate(f / ((float) this.statusBarTransitionDistance)));
    }

    public static /* synthetic */ void setDragDownAmountAnimated$default(LockscreenShadeTransitionController lockscreenShadeTransitionController, float f, long j, Function0 function0, int i, Object obj) {
        if ((i & 2) != 0) {
            j = 0;
        }
        if ((i & 4) != 0) {
            function0 = null;
        }
        lockscreenShadeTransitionController.setDragDownAmountAnimated(f, j, function0);
    }

    public final void setDragDownAmountAnimated(float f, long j, Function0<Unit> function0) {
        this.logger.logDragDownAnimation(f);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{this.dragDownAmount, f});
        ofFloat.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
        ofFloat.setDuration(375);
        ofFloat.addUpdateListener(new LockscreenShadeTransitionController$setDragDownAmountAnimated$1(this));
        if (j > 0) {
            ofFloat.setStartDelay(j);
        }
        if (function0 != null) {
            ofFloat.addListener(new LockscreenShadeTransitionController$setDragDownAmountAnimated$2(function0));
        }
        ofFloat.start();
        this.dragDownAnimator = ofFloat;
    }

    public final void animateAppear(long j) {
        this.forceApplyAmount = true;
        setDragDownAmount$frameworks__base__packages__SystemUI__android_common__SystemUI_core(1.0f);
        setDragDownAmountAnimated((float) this.fullTransitionDistanceByTap, j, new LockscreenShadeTransitionController$animateAppear$1(this));
    }

    public static /* synthetic */ void goToLockedShade$default(LockscreenShadeTransitionController lockscreenShadeTransitionController, View view, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = true;
        }
        lockscreenShadeTransitionController.goToLockedShade(view, z);
    }

    public final void goToLockedShade(@Nullable View view, boolean z) {
        LockscreenShadeTransitionController$goToLockedShade$1 lockscreenShadeTransitionController$goToLockedShade$1;
        boolean z2 = true;
        if (this.statusBarStateController.getState() != 1) {
            z2 = false;
        }
        this.logger.logTryGoToLockedShade(z2);
        if (z2) {
            if (z) {
                lockscreenShadeTransitionController$goToLockedShade$1 = null;
            } else {
                lockscreenShadeTransitionController$goToLockedShade$1 = new LockscreenShadeTransitionController$goToLockedShade$1(this);
            }
            goToLockedShadeInternal(view, lockscreenShadeTransitionController$goToLockedShade$1, (Runnable) null);
        }
    }

    public final void goToLockedShadeInternal(View view, Function1<? super Long, Unit> function1, Runnable runnable) {
        NotificationEntry notificationEntry;
        if (getCentralSurfaces().isShadeDisabled()) {
            if (runnable != null) {
                runnable.run();
            }
            this.logger.logShadeDisabledOnGoToLockedShade();
            return;
        }
        int currentUserId = this.lockScreenUserManager.getCurrentUserId();
        LockscreenShadeTransitionController$goToLockedShadeInternal$1 lockscreenShadeTransitionController$goToLockedShadeInternal$1 = null;
        if (view instanceof ExpandableNotificationRow) {
            notificationEntry = ((ExpandableNotificationRow) view).getEntry();
            notificationEntry.setUserExpanded(true, true);
            notificationEntry.setGroupExpansionChanging(true);
            currentUserId = notificationEntry.getSbn().getUserId();
        } else {
            notificationEntry = null;
        }
        NotificationLockscreenUserManager notificationLockscreenUserManager = this.lockScreenUserManager;
        boolean z = false;
        boolean z2 = !notificationLockscreenUserManager.userAllowsPrivateNotificationsInPublic(notificationLockscreenUserManager.getCurrentUserId()) || !this.lockScreenUserManager.shouldShowLockscreenNotifications() || this.falsingCollector.shouldEnforceBouncer();
        if (this.keyguardBypassController.getBypassEnabled()) {
            z2 = false;
        }
        if (!this.lockScreenUserManager.isLockscreenPublicMode(currentUserId) || !z2) {
            LSShadeTransitionLogger lSShadeTransitionLogger = this.logger;
            if (function1 != null) {
                z = true;
            }
            lSShadeTransitionLogger.logGoingToLockedShade(z);
            if (this.statusBarStateController.isDozing()) {
                this.isWakingToShadeLocked = true;
            }
            this.statusBarStateController.setState(2);
            if (function1 != null) {
                function1.invoke(0L);
            } else {
                performDefaultGoToFullShadeAnimation(0);
            }
        } else {
            this.statusBarStateController.setLeaveOpenOnKeyguardHide(true);
            if (function1 != null) {
                lockscreenShadeTransitionController$goToLockedShadeInternal$1 = new LockscreenShadeTransitionController$goToLockedShadeInternal$1(this, function1);
            }
            LockscreenShadeTransitionController$goToLockedShadeInternal$cancelHandler$1 lockscreenShadeTransitionController$goToLockedShadeInternal$cancelHandler$1 = new LockscreenShadeTransitionController$goToLockedShadeInternal$cancelHandler$1(this, runnable);
            this.logger.logShowBouncerOnGoToLockedShade();
            getCentralSurfaces().showBouncerWithDimissAndCancelIfKeyguard(lockscreenShadeTransitionController$goToLockedShadeInternal$1, lockscreenShadeTransitionController$goToLockedShadeInternal$cancelHandler$1);
            this.draggedDownEntry = notificationEntry;
        }
    }

    public final void onHideKeyguard(long j, int i) {
        this.logger.logOnHideKeyguard();
        Function1<? super Long, Unit> function1 = this.animationHandlerOnKeyguardDismiss;
        if (function1 != null) {
            Intrinsics.checkNotNull(function1);
            function1.invoke(Long.valueOf(j));
            this.animationHandlerOnKeyguardDismiss = null;
        } else if (this.nextHideKeyguardNeedsNoAnimation) {
            this.nextHideKeyguardNeedsNoAnimation = false;
        } else if (i != 2) {
            performDefaultGoToFullShadeAnimation(j);
        }
        NotificationEntry notificationEntry = this.draggedDownEntry;
        if (notificationEntry != null) {
            notificationEntry.setUserLocked(false);
            this.draggedDownEntry = null;
        }
    }

    public final void performDefaultGoToFullShadeAnimation(long j) {
        this.logger.logDefaultGoToFullShadeAnimation(j);
        getNotificationPanelController().animateToFullShade(j);
        animateAppear(j);
    }

    public static /* synthetic */ void setPulseHeight$default(LockscreenShadeTransitionController lockscreenShadeTransitionController, float f, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        lockscreenShadeTransitionController.setPulseHeight(f, z);
    }

    public final void setPulseHeight(float f, boolean z) {
        if (z) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{this.pulseHeight, f});
            ofFloat.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
            ofFloat.setDuration(375);
            ofFloat.addUpdateListener(new LockscreenShadeTransitionController$setPulseHeight$1(this));
            ofFloat.start();
            this.pulseHeightAnimator = ofFloat;
            return;
        }
        this.pulseHeight = f;
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.nsslController;
        if (notificationStackScrollLayoutController == null) {
            notificationStackScrollLayoutController = null;
        }
        getNotificationPanelController().setOverStrechAmount(notificationStackScrollLayoutController.setPulseHeight(f));
        if (!this.keyguardBypassController.getBypassEnabled()) {
            f = 0.0f;
        }
        transitionToShadeAmountCommon(f);
    }

    public final void finishPulseAnimation(boolean z) {
        this.logger.logPulseExpansionFinished(z);
        if (z) {
            setPulseHeight(0.0f, true);
            return;
        }
        getNotificationPanelController().onPulseExpansionFinished();
        setPulseHeight(0.0f, false);
    }

    public final void onPulseExpansionStarted() {
        this.logger.logPulseExpansionStarted();
        ValueAnimator valueAnimator = this.pulseHeightAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.logger.logAnimationCancelled(true);
            valueAnimator.cancel();
        }
    }

    public void dump(@NotNull PrintWriter printWriter, @NotNull String[] strArr) {
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        indentingPrintWriter.println("LSShadeTransitionController:");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println(Intrinsics.stringPlus("pulseHeight: ", Float.valueOf(this.pulseHeight)));
        indentingPrintWriter.println(Intrinsics.stringPlus("useSplitShade: ", Boolean.valueOf(this.useSplitShade)));
        indentingPrintWriter.println(Intrinsics.stringPlus("dragDownAmount: ", Float.valueOf(getDragDownAmount$frameworks__base__packages__SystemUI__android_common__SystemUI_core())));
        indentingPrintWriter.println(Intrinsics.stringPlus("qSDragProgress: ", Float.valueOf(getQSDragProgress())));
        indentingPrintWriter.println(Intrinsics.stringPlus("isDragDownAnywhereEnabled: ", Boolean.valueOf(isDragDownAnywhereEnabled$frameworks__base__packages__SystemUI__android_common__SystemUI_core())));
        indentingPrintWriter.println(Intrinsics.stringPlus("isFalsingCheckNeeded: ", Boolean.valueOf(isFalsingCheckNeeded$frameworks__base__packages__SystemUI__android_common__SystemUI_core())));
        indentingPrintWriter.println(Intrinsics.stringPlus("isWakingToShadeLocked: ", Boolean.valueOf(isWakingToShadeLocked())));
        indentingPrintWriter.println(Intrinsics.stringPlus("hasPendingHandlerOnKeyguardDismiss: ", Boolean.valueOf(this.animationHandlerOnKeyguardDismiss != null)));
    }
}
