package com.android.systemui.media;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Trace;
import android.util.MathUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import com.android.keyguard.KeyguardViewController;
import com.android.systemui.R$dimen;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.dreams.DreamOverlayStateController;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.CrossFadeHelper;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.LargeScreenUtils;
import com.android.systemui.util.animation.UniqueObjectHostView;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: MediaHierarchyManager.kt */
public final class MediaHierarchyManager {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public float animationCrossFadeProgress;
    public boolean animationPending;
    public float animationStartAlpha;
    @NotNull
    public Rect animationStartBounds = new Rect();
    @NotNull
    public Rect animationStartClipping = new Rect();
    public float animationStartCrossFadeProgress;
    public ValueAnimator animator;
    @NotNull
    public final KeyguardBypassController bypassController;
    public float carouselAlpha;
    public boolean collapsingShadeFromQS;
    @NotNull
    public final Context context;
    public int crossFadeAnimationEndLocation = -1;
    public int crossFadeAnimationStartLocation = -1;
    public int currentAttachmentLocation;
    @NotNull
    public Rect currentBounds = new Rect();
    @NotNull
    public Rect currentClipping = new Rect();
    public int desiredLocation;
    public int distanceForFullShadeTransition;
    public boolean dozeAnimationRunning;
    public boolean dreamOverlayActive;
    @NotNull
    public final DreamOverlayStateController dreamOverlayStateController;
    public float fullShadeTransitionProgress;
    public boolean fullyAwake;
    public boolean goingToSleep;
    public boolean inSplitShade;
    public boolean isCrossFadeAnimatorRunning;
    @NotNull
    public final KeyguardStateController keyguardStateController;
    @NotNull
    public final KeyguardViewController keyguardViewController;
    @NotNull
    public final MediaCarouselController mediaCarouselController;
    @NotNull
    public final MediaHost[] mediaHosts;
    @NotNull
    public final NotificationLockscreenUserManager notifLockscreenUserManager;
    public int previousLocation;
    public boolean qsExpanded;
    public float qsExpansion;
    @Nullable
    public ViewGroupOverlay rootOverlay;
    @Nullable
    public View rootView;
    @NotNull
    public final Runnable startAnimation;
    @NotNull
    public final SysuiStatusBarStateController statusBarStateController;
    public int statusbarState;
    @NotNull
    public Rect targetBounds = new Rect();
    @NotNull
    public Rect targetClipping = new Rect();

    public final float calculateAlphaFromCrossFade(float f, boolean z) {
        if (f <= 0.5f) {
            return 1.0f - (f / 0.5f);
        }
        if (z) {
            return 1.0f;
        }
        return (f - 0.5f) / 0.5f;
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x002b A[Catch:{ all -> 0x006e }] */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x002d A[Catch:{ all -> 0x006e }] */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0031 A[Catch:{ all -> 0x006e }] */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0032 A[Catch:{ all -> 0x006e }] */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0048 A[Catch:{ all -> 0x006e }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void applyState(android.graphics.Rect r2, float r3, boolean r4, android.graphics.Rect r5) {
        /*
            r1 = this;
            java.lang.String r0 = "MediaHierarchyManager#applyState"
            android.os.Trace.beginSection(r0)
            android.graphics.Rect r0 = r1.currentBounds     // Catch:{ all -> 0x006e }
            r0.set(r2)     // Catch:{ all -> 0x006e }
            r1.currentClipping = r5     // Catch:{ all -> 0x006e }
            boolean r2 = r1.isCurrentlyFading()     // Catch:{ all -> 0x006e }
            r5 = 1065353216(0x3f800000, float:1.0)
            if (r2 == 0) goto L_0x0015
            goto L_0x0016
        L_0x0015:
            r3 = r5
        L_0x0016:
            r1.setCarouselAlpha(r3)     // Catch:{ all -> 0x006e }
            boolean r2 = r1.isCurrentlyInGuidedTransformation()     // Catch:{ all -> 0x006e }
            if (r2 == 0) goto L_0x0028
            boolean r2 = r1.isCurrentlyFading()     // Catch:{ all -> 0x006e }
            if (r2 == 0) goto L_0x0026
            goto L_0x0028
        L_0x0026:
            r2 = 0
            goto L_0x0029
        L_0x0028:
            r2 = 1
        L_0x0029:
            if (r2 == 0) goto L_0x002d
            r3 = -1
            goto L_0x002f
        L_0x002d:
            int r3 = r1.previousLocation     // Catch:{ all -> 0x006e }
        L_0x002f:
            if (r2 == 0) goto L_0x0032
            goto L_0x0036
        L_0x0032:
            float r5 = r1.getTransformationProgress()     // Catch:{ all -> 0x006e }
        L_0x0036:
            int r2 = r1.resolveLocationForFading()     // Catch:{ all -> 0x006e }
            com.android.systemui.media.MediaCarouselController r0 = r1.mediaCarouselController     // Catch:{ all -> 0x006e }
            r0.setCurrentState(r3, r2, r5, r4)     // Catch:{ all -> 0x006e }
            r1.updateHostAttachment()     // Catch:{ all -> 0x006e }
            int r2 = r1.currentAttachmentLocation     // Catch:{ all -> 0x006e }
            r3 = -1000(0xfffffffffffffc18, float:NaN)
            if (r2 != r3) goto L_0x0068
            android.graphics.Rect r2 = r1.currentClipping     // Catch:{ all -> 0x006e }
            boolean r2 = r2.isEmpty()     // Catch:{ all -> 0x006e }
            if (r2 != 0) goto L_0x0057
            android.graphics.Rect r2 = r1.currentBounds     // Catch:{ all -> 0x006e }
            android.graphics.Rect r3 = r1.currentClipping     // Catch:{ all -> 0x006e }
            r2.intersect(r3)     // Catch:{ all -> 0x006e }
        L_0x0057:
            android.view.ViewGroup r2 = r1.getMediaFrame()     // Catch:{ all -> 0x006e }
            android.graphics.Rect r1 = r1.currentBounds     // Catch:{ all -> 0x006e }
            int r3 = r1.left     // Catch:{ all -> 0x006e }
            int r4 = r1.top     // Catch:{ all -> 0x006e }
            int r5 = r1.right     // Catch:{ all -> 0x006e }
            int r1 = r1.bottom     // Catch:{ all -> 0x006e }
            r2.setLeftTopRightBottom(r3, r4, r5, r1)     // Catch:{ all -> 0x006e }
        L_0x0068:
            kotlin.Unit r1 = kotlin.Unit.INSTANCE     // Catch:{ all -> 0x006e }
            android.os.Trace.endSection()
            return
        L_0x006e:
            r1 = move-exception
            android.os.Trace.endSection()
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.MediaHierarchyManager.applyState(android.graphics.Rect, float, boolean, android.graphics.Rect):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x0071 A[Catch:{ all -> 0x00d6 }] */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0073 A[Catch:{ all -> 0x00d6 }] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x007b A[Catch:{ all -> 0x00d6 }] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0091 A[Catch:{ all -> 0x00d6 }] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00b2 A[Catch:{ all -> 0x00d6 }] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:49:0x00c8=Splitter:B:49:0x00c8, B:53:0x00cf=Splitter:B:53:0x00cf} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void performTransitionToNewLocation(boolean r7, boolean r8) {
        /*
            r6 = this;
            java.lang.String r0 = "MediaHierarchyManager#performTransitionToNewLocation"
            android.os.Trace.beginSection(r0)
            int r0 = r6.previousLocation     // Catch:{ all -> 0x00d6 }
            if (r0 < 0) goto L_0x00cf
            if (r7 == 0) goto L_0x000d
            goto L_0x00cf
        L_0x000d:
            int r7 = r6.desiredLocation     // Catch:{ all -> 0x00d6 }
            com.android.systemui.media.MediaHost r7 = r6.getHost(r7)     // Catch:{ all -> 0x00d6 }
            int r0 = r6.previousLocation     // Catch:{ all -> 0x00d6 }
            com.android.systemui.media.MediaHost r0 = r6.getHost(r0)     // Catch:{ all -> 0x00d6 }
            if (r7 == 0) goto L_0x00c8
            if (r0 != 0) goto L_0x001f
            goto L_0x00c8
        L_0x001f:
            r6.updateTargetState()     // Catch:{ all -> 0x00d6 }
            boolean r7 = r6.isCurrentlyInGuidedTransformation()     // Catch:{ all -> 0x00d6 }
            if (r7 == 0) goto L_0x002d
            r6.applyTargetStateIfNotAnimating()     // Catch:{ all -> 0x00d6 }
            goto L_0x00c2
        L_0x002d:
            if (r8 == 0) goto L_0x00bf
            boolean r7 = r6.isCrossFadeAnimatorRunning     // Catch:{ all -> 0x00d6 }
            float r8 = r6.animationCrossFadeProgress     // Catch:{ all -> 0x00d6 }
            android.animation.ValueAnimator r1 = r6.animator     // Catch:{ all -> 0x00d6 }
            r1.cancel()     // Catch:{ all -> 0x00d6 }
            int r1 = r6.currentAttachmentLocation     // Catch:{ all -> 0x00d6 }
            int r2 = r6.previousLocation     // Catch:{ all -> 0x00d6 }
            if (r1 != r2) goto L_0x005c
            com.android.systemui.util.animation.UniqueObjectHostView r1 = r0.getHostView()     // Catch:{ all -> 0x00d6 }
            boolean r1 = r1.isAttachedToWindow()     // Catch:{ all -> 0x00d6 }
            if (r1 != 0) goto L_0x0049
            goto L_0x005c
        L_0x0049:
            android.graphics.Rect r1 = r6.animationStartBounds     // Catch:{ all -> 0x00d6 }
            android.graphics.Rect r2 = r0.getCurrentBounds()     // Catch:{ all -> 0x00d6 }
            r1.set(r2)     // Catch:{ all -> 0x00d6 }
            android.graphics.Rect r1 = r6.animationStartClipping     // Catch:{ all -> 0x00d6 }
            android.graphics.Rect r0 = r0.getCurrentClipping()     // Catch:{ all -> 0x00d6 }
            r1.set(r0)     // Catch:{ all -> 0x00d6 }
            goto L_0x006a
        L_0x005c:
            android.graphics.Rect r0 = r6.animationStartBounds     // Catch:{ all -> 0x00d6 }
            android.graphics.Rect r1 = r6.currentBounds     // Catch:{ all -> 0x00d6 }
            r0.set(r1)     // Catch:{ all -> 0x00d6 }
            android.graphics.Rect r0 = r6.animationStartClipping     // Catch:{ all -> 0x00d6 }
            android.graphics.Rect r1 = r6.currentClipping     // Catch:{ all -> 0x00d6 }
            r0.set(r1)     // Catch:{ all -> 0x00d6 }
        L_0x006a:
            int r0 = r6.calculateTransformationType()     // Catch:{ all -> 0x00d6 }
            r1 = 1
            if (r0 != r1) goto L_0x0073
            r0 = r1
            goto L_0x0074
        L_0x0073:
            r0 = 0
        L_0x0074:
            r2 = 0
            int r3 = r6.previousLocation     // Catch:{ all -> 0x00d6 }
            r4 = 1065353216(0x3f800000, float:1.0)
            if (r7 == 0) goto L_0x0091
            int r7 = r6.currentAttachmentLocation     // Catch:{ all -> 0x00d6 }
            int r5 = r6.crossFadeAnimationEndLocation     // Catch:{ all -> 0x00d6 }
            if (r7 != r5) goto L_0x0086
            if (r0 == 0) goto L_0x009b
            float r8 = r4 - r8
            goto L_0x009c
        L_0x0086:
            int r7 = r6.crossFadeAnimationStartLocation     // Catch:{ all -> 0x00d6 }
            int r2 = r6.desiredLocation     // Catch:{ all -> 0x00d6 }
            if (r7 != r2) goto L_0x008f
            float r8 = r4 - r8
            goto L_0x009d
        L_0x008f:
            r0 = r1
            goto L_0x009d
        L_0x0091:
            if (r0 == 0) goto L_0x009b
            float r7 = r6.carouselAlpha     // Catch:{ all -> 0x00d6 }
            float r4 = r4 - r7
            r7 = 1073741824(0x40000000, float:2.0)
            float r8 = r4 / r7
            goto L_0x009c
        L_0x009b:
            r8 = r2
        L_0x009c:
            r7 = r3
        L_0x009d:
            r6.isCrossFadeAnimatorRunning = r0     // Catch:{ all -> 0x00d6 }
            r6.crossFadeAnimationStartLocation = r7     // Catch:{ all -> 0x00d6 }
            int r7 = r6.desiredLocation     // Catch:{ all -> 0x00d6 }
            r6.crossFadeAnimationEndLocation = r7     // Catch:{ all -> 0x00d6 }
            float r0 = r6.carouselAlpha     // Catch:{ all -> 0x00d6 }
            r6.animationStartAlpha = r0     // Catch:{ all -> 0x00d6 }
            r6.animationStartCrossFadeProgress = r8     // Catch:{ all -> 0x00d6 }
            r6.adjustAnimatorForTransition(r7, r3)     // Catch:{ all -> 0x00d6 }
            boolean r7 = r6.animationPending     // Catch:{ all -> 0x00d6 }
            if (r7 != 0) goto L_0x00c2
            android.view.View r7 = r6.rootView     // Catch:{ all -> 0x00d6 }
            if (r7 != 0) goto L_0x00b7
            goto L_0x00c2
        L_0x00b7:
            r6.animationPending = r1     // Catch:{ all -> 0x00d6 }
            java.lang.Runnable r6 = r6.startAnimation     // Catch:{ all -> 0x00d6 }
            r7.postOnAnimation(r6)     // Catch:{ all -> 0x00d6 }
            goto L_0x00c2
        L_0x00bf:
            r6.cancelAnimationAndApplyDesiredState()     // Catch:{ all -> 0x00d6 }
        L_0x00c2:
            kotlin.Unit r6 = kotlin.Unit.INSTANCE     // Catch:{ all -> 0x00d6 }
            android.os.Trace.endSection()
            return
        L_0x00c8:
            r6.cancelAnimationAndApplyDesiredState()     // Catch:{ all -> 0x00d6 }
            android.os.Trace.endSection()
            return
        L_0x00cf:
            r6.cancelAnimationAndApplyDesiredState()     // Catch:{ all -> 0x00d6 }
            android.os.Trace.endSection()
            return
        L_0x00d6:
            r6 = move-exception
            android.os.Trace.endSection()
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.MediaHierarchyManager.performTransitionToNewLocation(boolean, boolean):void");
    }

    public final void updateDesiredLocation(boolean z, boolean z2) {
        Trace.beginSection("MediaHierarchyManager#updateDesiredLocation");
        try {
            int calculateLocation = calculateLocation();
            int i = this.desiredLocation;
            if (calculateLocation != i || z2) {
                boolean z3 = false;
                if (i >= 0 && calculateLocation != i) {
                    this.previousLocation = i;
                } else if (z2) {
                    boolean z4 = !this.bypassController.getBypassEnabled() && this.statusbarState == 1;
                    if (calculateLocation == 0 && this.previousLocation == 2 && !z4) {
                        this.previousLocation = 1;
                    }
                }
                boolean z5 = this.desiredLocation == -1;
                this.desiredLocation = calculateLocation;
                boolean z6 = !z && shouldAnimateTransition(calculateLocation, this.previousLocation);
                Pair<Long, Long> animationParams = getAnimationParams(this.previousLocation, calculateLocation);
                long longValue = animationParams.component1().longValue();
                long longValue2 = animationParams.component2().longValue();
                MediaHost host = getHost(calculateLocation);
                if (calculateTransformationType() == 1) {
                    z3 = true;
                }
                if (!z3 || isCurrentlyInGuidedTransformation() || !z6) {
                    this.mediaCarouselController.onDesiredLocationChanged(calculateLocation, host, z6, longValue, longValue2);
                }
                performTransitionToNewLocation(z5, z6);
            }
            Unit unit = Unit.INSTANCE;
        } finally {
            Trace.endSection();
        }
    }

    public final void updateHostAttachment() {
        boolean z;
        Trace.beginSection("MediaHierarchyManager#updateHostAttachment");
        try {
            int resolveLocationForFading = resolveLocationForFading();
            boolean z2 = true;
            boolean z3 = !isCurrentlyFading();
            if (this.isCrossFadeAnimatorRunning) {
                MediaHost host = getHost(resolveLocationForFading);
                if (host != null && host.getVisible()) {
                    MediaHost host2 = getHost(resolveLocationForFading);
                    if (host2 != null) {
                        UniqueObjectHostView hostView = host2.getHostView();
                        if (hostView != null) {
                            if (!hostView.isShown()) {
                                z = true;
                                if (z && resolveLocationForFading != this.desiredLocation) {
                                    z3 = true;
                                }
                            }
                        }
                    }
                    z = false;
                    z3 = true;
                }
            }
            if (!isTransitionRunning() || this.rootOverlay == null || !z3) {
                z2 = false;
            }
            if (z2) {
                resolveLocationForFading = -1000;
            }
            int i = resolveLocationForFading;
            if (this.currentAttachmentLocation != i) {
                this.currentAttachmentLocation = i;
                ViewGroup viewGroup = (ViewGroup) getMediaFrame().getParent();
                if (viewGroup != null) {
                    viewGroup.removeView(getMediaFrame());
                }
                if (z2) {
                    ViewGroupOverlay viewGroupOverlay = this.rootOverlay;
                    Intrinsics.checkNotNull(viewGroupOverlay);
                    viewGroupOverlay.add(getMediaFrame());
                } else {
                    MediaHost host3 = getHost(i);
                    Intrinsics.checkNotNull(host3);
                    UniqueObjectHostView hostView2 = host3.getHostView();
                    hostView2.addView(getMediaFrame());
                    int paddingLeft = hostView2.getPaddingLeft();
                    int paddingTop = hostView2.getPaddingTop();
                    getMediaFrame().setLeftTopRightBottom(paddingLeft, paddingTop, this.currentBounds.width() + paddingLeft, this.currentBounds.height() + paddingTop);
                }
                if (this.isCrossFadeAnimatorRunning) {
                    MediaCarouselController.onDesiredLocationChanged$default(this.mediaCarouselController, i, getHost(i), false, 0, 0, 24, (Object) null);
                }
            }
            Unit unit = Unit.INSTANCE;
        } finally {
            Trace.endSection();
        }
    }

    public MediaHierarchyManager(@NotNull Context context2, @NotNull SysuiStatusBarStateController sysuiStatusBarStateController, @NotNull KeyguardStateController keyguardStateController2, @NotNull KeyguardBypassController keyguardBypassController, @NotNull MediaCarouselController mediaCarouselController2, @NotNull NotificationLockscreenUserManager notificationLockscreenUserManager, @NotNull ConfigurationController configurationController, @NotNull WakefulnessLifecycle wakefulnessLifecycle, @NotNull KeyguardViewController keyguardViewController2, @NotNull DreamOverlayStateController dreamOverlayStateController2) {
        this.context = context2;
        this.statusBarStateController = sysuiStatusBarStateController;
        this.keyguardStateController = keyguardStateController2;
        this.bypassController = keyguardBypassController;
        this.mediaCarouselController = mediaCarouselController2;
        this.notifLockscreenUserManager = notificationLockscreenUserManager;
        this.keyguardViewController = keyguardViewController2;
        this.dreamOverlayStateController = dreamOverlayStateController2;
        this.statusbarState = sysuiStatusBarStateController.getState();
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        ofFloat.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
        ofFloat.addUpdateListener(new MediaHierarchyManager$animator$1$1(this, ofFloat));
        ofFloat.addListener(new MediaHierarchyManager$animator$1$2(this));
        this.animator = ofFloat;
        this.mediaHosts = new MediaHost[4];
        this.previousLocation = -1;
        this.desiredLocation = -1;
        this.currentAttachmentLocation = -1;
        this.startAnimation = new MediaHierarchyManager$startAnimation$1(this);
        this.animationCrossFadeProgress = 1.0f;
        this.carouselAlpha = 1.0f;
        updateConfiguration();
        configurationController.addCallback(new ConfigurationController.ConfigurationListener(this) {
            public final /* synthetic */ MediaHierarchyManager this$0;

            {
                this.this$0 = r1;
            }

            public void onConfigChanged(@Nullable Configuration configuration) {
                this.this$0.updateConfiguration();
                this.this$0.updateDesiredLocation(true, true);
            }
        });
        sysuiStatusBarStateController.addCallback(new StatusBarStateController.StateListener(this) {
            public final /* synthetic */ MediaHierarchyManager this$0;

            {
                this.this$0 = r1;
            }

            public void onStatePreChange(int i, int i2) {
                this.this$0.statusbarState = i2;
                MediaHierarchyManager.updateDesiredLocation$default(this.this$0, false, false, 3, (Object) null);
            }

            public void onStateChanged(int i) {
                this.this$0.updateTargetState();
                if (i == 2 && this.this$0.isLockScreenShadeVisibleToUser()) {
                    this.this$0.mediaCarouselController.logSmartspaceImpression(this.this$0.getQsExpanded());
                }
                this.this$0.mediaCarouselController.getMediaCarouselScrollHandler().setVisibleToUser(this.this$0.isVisibleToUser());
            }

            /* JADX WARNING: Code restructure failed: missing block: B:9:0x0017, code lost:
                if ((r3 == 1.0f) == false) goto L_0x001b;
             */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void onDozeAmountChanged(float r3, float r4) {
                /*
                    r2 = this;
                    com.android.systemui.media.MediaHierarchyManager r2 = r2.this$0
                    r4 = 0
                    int r4 = (r3 > r4 ? 1 : (r3 == r4 ? 0 : -1))
                    r0 = 1
                    r1 = 0
                    if (r4 != 0) goto L_0x000b
                    r4 = r0
                    goto L_0x000c
                L_0x000b:
                    r4 = r1
                L_0x000c:
                    if (r4 != 0) goto L_0x001a
                    r4 = 1065353216(0x3f800000, float:1.0)
                    int r3 = (r3 > r4 ? 1 : (r3 == r4 ? 0 : -1))
                    if (r3 != 0) goto L_0x0016
                    r3 = r0
                    goto L_0x0017
                L_0x0016:
                    r3 = r1
                L_0x0017:
                    if (r3 != 0) goto L_0x001a
                    goto L_0x001b
                L_0x001a:
                    r0 = r1
                L_0x001b:
                    r2.setDozeAnimationRunning(r0)
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.MediaHierarchyManager.AnonymousClass2.onDozeAmountChanged(float, float):void");
            }

            public void onDozingChanged(boolean z) {
                if (!z) {
                    this.this$0.setDozeAnimationRunning(false);
                    if (this.this$0.isLockScreenVisibleToUser()) {
                        this.this$0.mediaCarouselController.logSmartspaceImpression(this.this$0.getQsExpanded());
                    }
                } else {
                    MediaHierarchyManager.updateDesiredLocation$default(this.this$0, false, false, 3, (Object) null);
                    this.this$0.setQsExpanded(false);
                    this.this$0.closeGuts();
                }
                this.this$0.mediaCarouselController.getMediaCarouselScrollHandler().setVisibleToUser(this.this$0.isVisibleToUser());
            }

            public void onExpandedChanged(boolean z) {
                if (this.this$0.isHomeScreenShadeVisibleToUser()) {
                    this.this$0.mediaCarouselController.logSmartspaceImpression(this.this$0.getQsExpanded());
                }
                this.this$0.mediaCarouselController.getMediaCarouselScrollHandler().setVisibleToUser(this.this$0.isVisibleToUser());
            }
        });
        dreamOverlayStateController2.addCallback((DreamOverlayStateController.Callback) new DreamOverlayStateController.Callback(this) {
            public final /* synthetic */ MediaHierarchyManager this$0;

            {
                this.this$0 = r1;
            }

            public void onStateChanged() {
                this.this$0.setDreamOverlayActive(this.this$0.dreamOverlayStateController.isOverlayActive());
            }
        });
        wakefulnessLifecycle.addObserver(new WakefulnessLifecycle.Observer(this) {
            public final /* synthetic */ MediaHierarchyManager this$0;

            {
                this.this$0 = r1;
            }

            public void onFinishedGoingToSleep() {
                this.this$0.setGoingToSleep(false);
            }

            public void onStartedGoingToSleep() {
                this.this$0.setGoingToSleep(true);
                this.this$0.setFullyAwake(false);
            }

            public void onFinishedWakingUp() {
                this.this$0.setGoingToSleep(false);
                this.this$0.setFullyAwake(true);
            }

            public void onStartedWakingUp() {
                this.this$0.setGoingToSleep(false);
            }
        });
        mediaCarouselController2.setUpdateUserVisibility(new Function0<Unit>(this) {
            public final /* synthetic */ MediaHierarchyManager this$0;

            {
                this.this$0 = r1;
            }

            public final void invoke() {
                this.this$0.mediaCarouselController.getMediaCarouselScrollHandler().setVisibleToUser(this.this$0.isVisibleToUser());
            }
        });
    }

    public final ViewGroup getMediaFrame() {
        return this.mediaCarouselController.getMediaFrame();
    }

    public final void resolveClipping(Rect rect) {
        if (this.animationStartClipping.isEmpty()) {
            rect.set(this.targetClipping);
        } else if (this.targetClipping.isEmpty()) {
            rect.set(this.animationStartClipping);
        } else {
            rect.setIntersect(this.animationStartClipping, this.targetClipping);
        }
    }

    public final boolean getHasActiveMedia() {
        MediaHost mediaHost = this.mediaHosts[1];
        return mediaHost != null && mediaHost.getVisible();
    }

    public final void setQsExpansion(float f) {
        if (!(this.qsExpansion == f)) {
            this.qsExpansion = f;
            updateDesiredLocation$default(this, false, false, 3, (Object) null);
            if (getQSTransformationProgress() >= 0.0f) {
                updateTargetState();
                applyTargetStateIfNotAnimating();
            }
        }
    }

    public final boolean getQsExpanded() {
        return this.qsExpanded;
    }

    public final void setQsExpanded(boolean z) {
        if (this.qsExpanded != z) {
            this.qsExpanded = z;
            this.mediaCarouselController.getMediaCarouselScrollHandler().setQsExpanded(z);
        }
        if (z && (isLockScreenShadeVisibleToUser() || isHomeScreenShadeVisibleToUser())) {
            this.mediaCarouselController.logSmartspaceImpression(z);
        }
        this.mediaCarouselController.getMediaCarouselScrollHandler().setVisibleToUser(isVisibleToUser());
    }

    public final void setFullShadeTransitionProgress(float f) {
        if (!(this.fullShadeTransitionProgress == f)) {
            this.fullShadeTransitionProgress = f;
            if (!this.bypassController.getBypassEnabled() && this.statusbarState == 1) {
                updateDesiredLocation$default(this, isCurrentlyFading(), false, 2, (Object) null);
                if (f >= 0.0f) {
                    updateTargetState();
                    setCarouselAlpha(calculateAlphaFromCrossFade(this.fullShadeTransitionProgress, true));
                    applyTargetStateIfNotAnimating();
                }
            }
        }
    }

    public final boolean isTransitioningToFullShade() {
        if ((this.fullShadeTransitionProgress == 0.0f) || this.bypassController.getBypassEnabled() || this.statusbarState != 1) {
            return false;
        }
        return true;
    }

    public final void setTransitionToFullShadeAmount(float f) {
        setFullShadeTransitionProgress(MathUtils.saturate(f / ((float) this.distanceForFullShadeTransition)));
    }

    public final int getGuidedTransformationTranslationY() {
        if (!isCurrentlyInGuidedTransformation()) {
            return -1;
        }
        MediaHost host = getHost(this.previousLocation);
        if (host == null) {
            return 0;
        }
        return this.targetBounds.top - host.getCurrentBounds().top;
    }

    public final void setCollapsingShadeFromQS(boolean z) {
        if (this.collapsingShadeFromQS != z) {
            this.collapsingShadeFromQS = z;
            updateDesiredLocation$default(this, true, false, 2, (Object) null);
        }
    }

    public final boolean getBlockLocationChanges() {
        return this.goingToSleep || this.dozeAnimationRunning;
    }

    public final void setGoingToSleep(boolean z) {
        if (this.goingToSleep != z) {
            this.goingToSleep = z;
            if (!z) {
                updateDesiredLocation$default(this, false, false, 3, (Object) null);
            }
        }
    }

    public final void setFullyAwake(boolean z) {
        if (this.fullyAwake != z) {
            this.fullyAwake = z;
            if (z) {
                updateDesiredLocation$default(this, true, false, 2, (Object) null);
            }
        }
    }

    public final void setDozeAnimationRunning(boolean z) {
        if (this.dozeAnimationRunning != z) {
            this.dozeAnimationRunning = z;
            if (!z) {
                updateDesiredLocation$default(this, false, false, 3, (Object) null);
            }
        }
    }

    public final void setDreamOverlayActive(boolean z) {
        if (this.dreamOverlayActive != z) {
            this.dreamOverlayActive = z;
            updateDesiredLocation$default(this, true, false, 2, (Object) null);
        }
    }

    public final void setCarouselAlpha(float f) {
        if (!(this.carouselAlpha == f)) {
            this.carouselAlpha = f;
            CrossFadeHelper.fadeIn(getMediaFrame(), f);
        }
    }

    public final void updateConfiguration() {
        this.distanceForFullShadeTransition = this.context.getResources().getDimensionPixelSize(R$dimen.lockscreen_shade_media_transition_distance);
        this.inSplitShade = LargeScreenUtils.shouldUseSplitNotificationShade(this.context.getResources());
    }

    @NotNull
    public final UniqueObjectHostView register(@NotNull MediaHost mediaHost) {
        UniqueObjectHostView createUniqueObjectHost = createUniqueObjectHost();
        mediaHost.setHostView(createUniqueObjectHost);
        mediaHost.addVisibilityChangeListener(new MediaHierarchyManager$register$1(mediaHost, this));
        this.mediaHosts[mediaHost.getLocation()] = mediaHost;
        if (mediaHost.getLocation() == this.desiredLocation) {
            this.desiredLocation = -1;
        }
        if (mediaHost.getLocation() == this.currentAttachmentLocation) {
            this.currentAttachmentLocation = -1;
        }
        updateDesiredLocation$default(this, false, false, 3, (Object) null);
        return createUniqueObjectHost;
    }

    public final void closeGuts() {
        MediaCarouselController.closeGuts$default(this.mediaCarouselController, false, 1, (Object) null);
    }

    public final UniqueObjectHostView createUniqueObjectHost() {
        UniqueObjectHostView uniqueObjectHostView = new UniqueObjectHostView(this.context);
        uniqueObjectHostView.addOnAttachStateChangeListener(new MediaHierarchyManager$createUniqueObjectHost$1(this, uniqueObjectHostView));
        return uniqueObjectHostView;
    }

    public static /* synthetic */ void updateDesiredLocation$default(MediaHierarchyManager mediaHierarchyManager, boolean z, boolean z2, int i, Object obj) {
        if ((i & 1) != 0) {
            z = false;
        }
        if ((i & 2) != 0) {
            z2 = false;
        }
        mediaHierarchyManager.updateDesiredLocation(z, z2);
    }

    public final boolean shouldAnimateTransition(int i, int i2) {
        if (isCurrentlyInGuidedTransformation()) {
            return false;
        }
        if (i2 == 2 && this.desiredLocation == 1 && this.statusbarState == 0) {
            return false;
        }
        if (i == 1 && i2 == 2 && (this.statusBarStateController.leaveOpenOnKeyguardHide() || this.statusbarState == 2)) {
            return true;
        }
        if (this.statusbarState == 1 && (i == 2 || i2 == 2)) {
            return false;
        }
        if (MediaHierarchyManagerKt.isShownNotFaded(getMediaFrame()) || this.animator.isRunning() || this.animationPending) {
            return true;
        }
        return false;
    }

    public final void adjustAnimatorForTransition(int i, int i2) {
        Pair<Long, Long> animationParams = getAnimationParams(i2, i);
        long longValue = animationParams.component1().longValue();
        long longValue2 = animationParams.component2().longValue();
        ValueAnimator valueAnimator = this.animator;
        valueAnimator.setDuration(longValue);
        valueAnimator.setStartDelay(longValue2);
    }

    public final Pair<Long, Long> getAnimationParams(int i, int i2) {
        long j;
        long j2 = 0;
        if (i == 2 && i2 == 1) {
            if (this.statusbarState == 0 && this.keyguardStateController.isKeyguardFadingAway()) {
                j2 = this.keyguardStateController.getKeyguardFadingAwayDelay();
            }
            j = 224;
        } else {
            j = (i == 1 && i2 == 2) ? 464 : 200;
        }
        return TuplesKt.to(Long.valueOf(j), Long.valueOf(j2));
    }

    public final void applyTargetStateIfNotAnimating() {
        if (!this.animator.isRunning()) {
            applyState$default(this, this.targetBounds, this.carouselAlpha, false, this.targetClipping, 4, (Object) null);
        }
    }

    public final void updateTargetState() {
        MediaHost host = getHost(this.previousLocation);
        MediaHost host2 = getHost(this.desiredLocation);
        if (isCurrentlyInGuidedTransformation() && !isCurrentlyFading() && host != null && host2 != null) {
            float transformationProgress = getTransformationProgress();
            if (!host2.getVisible()) {
                host2 = host;
            } else if (!host.getVisible()) {
                host = host2;
            } else {
                MediaHost mediaHost = host2;
                host2 = host;
                host = mediaHost;
            }
            this.targetBounds = interpolateBounds$default(this, host2.getCurrentBounds(), host.getCurrentBounds(), transformationProgress, (Rect) null, 8, (Object) null);
            this.targetClipping = host.getCurrentClipping();
        } else if (host2 != null) {
            this.targetBounds.set(host2.getCurrentBounds());
            this.targetClipping = host2.getCurrentClipping();
        }
    }

    public static /* synthetic */ Rect interpolateBounds$default(MediaHierarchyManager mediaHierarchyManager, Rect rect, Rect rect2, float f, Rect rect3, int i, Object obj) {
        if ((i & 8) != 0) {
            rect3 = null;
        }
        return mediaHierarchyManager.interpolateBounds(rect, rect2, f, rect3);
    }

    public final Rect interpolateBounds(Rect rect, Rect rect2, float f, Rect rect3) {
        int lerp = (int) MathUtils.lerp((float) rect.left, (float) rect2.left, f);
        int lerp2 = (int) MathUtils.lerp((float) rect.top, (float) rect2.top, f);
        int lerp3 = (int) MathUtils.lerp((float) rect.right, (float) rect2.right, f);
        int lerp4 = (int) MathUtils.lerp((float) rect.bottom, (float) rect2.bottom, f);
        if (rect3 == null) {
            rect3 = new Rect();
        }
        rect3.set(lerp, lerp2, lerp3, lerp4);
        return rect3;
    }

    public final boolean isCurrentlyInGuidedTransformation() {
        return hasValidStartAndEndLocations() && getTransformationProgress() >= 0.0f && areGuidedTransitionHostsVisible();
    }

    public final boolean hasValidStartAndEndLocations() {
        return (this.previousLocation == -1 || this.desiredLocation == -1) ? false : true;
    }

    public final int calculateTransformationType() {
        if (isTransitioningToFullShade()) {
            return (!this.inSplitShade || !areGuidedTransitionHostsVisible()) ? 1 : 0;
        }
        int i = this.previousLocation;
        if ((i == 2 && this.desiredLocation == 0) || (i == 0 && this.desiredLocation == 2)) {
            return 1;
        }
        return (i == 2 && this.desiredLocation == 1) ? 1 : 0;
    }

    public final boolean areGuidedTransitionHostsVisible() {
        MediaHost host = getHost(this.previousLocation);
        if (!(host != null && host.getVisible())) {
            return false;
        }
        MediaHost host2 = getHost(this.desiredLocation);
        if (host2 != null && host2.getVisible()) {
            return true;
        }
        return false;
    }

    public final float getTransformationProgress() {
        float qSTransformationProgress = getQSTransformationProgress();
        if (this.statusbarState != 1 && qSTransformationProgress >= 0.0f) {
            return qSTransformationProgress;
        }
        if (isTransitioningToFullShade()) {
            return this.fullShadeTransitionProgress;
        }
        return -1.0f;
    }

    public final float getQSTransformationProgress() {
        MediaHost host = getHost(this.desiredLocation);
        MediaHost host2 = getHost(this.previousLocation);
        if (!getHasActiveMedia()) {
            return -1.0f;
        }
        boolean z = false;
        if (!(host != null && host.getLocation() == 0) || this.inSplitShade) {
            return -1.0f;
        }
        if (host2 != null && host2.getLocation() == 1) {
            z = true;
        }
        if (!z) {
            return -1.0f;
        }
        if (host2.getVisible() || this.statusbarState != 1) {
            return this.qsExpansion;
        }
        return -1.0f;
    }

    public final MediaHost getHost(int i) {
        if (i < 0) {
            return null;
        }
        return this.mediaHosts[i];
    }

    public final void cancelAnimationAndApplyDesiredState() {
        this.animator.cancel();
        MediaHost host = getHost(this.desiredLocation);
        if (host != null) {
            applyState$default(this, host.getCurrentBounds(), 1.0f, true, (Rect) null, 8, (Object) null);
        }
    }

    public static /* synthetic */ void applyState$default(MediaHierarchyManager mediaHierarchyManager, Rect rect, float f, boolean z, Rect rect2, int i, Object obj) {
        if ((i & 4) != 0) {
            z = false;
        }
        if ((i & 8) != 0) {
            rect2 = MediaHierarchyManagerKt.EMPTY_RECT;
        }
        mediaHierarchyManager.applyState(rect, f, z, rect2);
    }

    public final int resolveLocationForFading() {
        if (!this.isCrossFadeAnimatorRunning) {
            return this.desiredLocation;
        }
        if (((double) this.animationCrossFadeProgress) > 0.5d || this.previousLocation == -1) {
            return this.crossFadeAnimationEndLocation;
        }
        return this.crossFadeAnimationStartLocation;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0015, code lost:
        if ((getTransformationProgress() == 1.0f) != false) goto L_0x0017;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean isTransitionRunning() {
        /*
            r4 = this;
            boolean r0 = r4.isCurrentlyInGuidedTransformation()
            r1 = 0
            r2 = 1
            if (r0 == 0) goto L_0x0017
            float r0 = r4.getTransformationProgress()
            r3 = 1065353216(0x3f800000, float:1.0)
            int r0 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1))
            if (r0 != 0) goto L_0x0014
            r0 = r2
            goto L_0x0015
        L_0x0014:
            r0 = r1
        L_0x0015:
            if (r0 == 0) goto L_0x0023
        L_0x0017:
            android.animation.ValueAnimator r0 = r4.animator
            boolean r0 = r0.isRunning()
            if (r0 != 0) goto L_0x0023
            boolean r4 = r4.animationPending
            if (r4 == 0) goto L_0x0024
        L_0x0023:
            r1 = r2
        L_0x0024:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.MediaHierarchyManager.isTransitionRunning():boolean");
    }

    public final int calculateLocation() {
        int i;
        if (getBlockLocationChanges()) {
            return this.desiredLocation;
        }
        boolean z = true;
        boolean z2 = !this.bypassController.getBypassEnabled() && this.statusbarState == 1;
        boolean shouldShowLockscreenNotifications = this.notifLockscreenUserManager.shouldShowLockscreenNotifications();
        if (this.dreamOverlayActive) {
            i = 3;
        } else {
            float f = this.qsExpansion;
            if (((f > 0.0f || this.inSplitShade) && !z2) || ((f > 0.4f && z2) || !getHasActiveMedia() || (z2 && isSplitShadeExpanding()))) {
                i = 0;
            } else {
                i = ((!z2 || !isTransformingToFullShadeAndInQQS()) && z2 && shouldShowLockscreenNotifications) ? 2 : 1;
            }
        }
        if (i == 2) {
            MediaHost host = getHost(i);
            if (host == null || !host.getVisible()) {
                z = false;
            }
            if (!z && !this.statusBarStateController.isDozing()) {
                return 0;
            }
        }
        if (i == 2 && this.desiredLocation == 0 && this.collapsingShadeFromQS) {
            return 0;
        }
        if (i == 2 || this.desiredLocation != 2 || this.fullyAwake) {
            return i;
        }
        return 2;
    }

    public final boolean isSplitShadeExpanding() {
        return this.inSplitShade && isTransitioningToFullShade();
    }

    public final boolean isTransformingToFullShadeAndInQQS() {
        if (isTransitioningToFullShade() && !this.inSplitShade && this.fullShadeTransitionProgress > 0.5f) {
            return true;
        }
        return false;
    }

    public final boolean isCurrentlyFading() {
        if (isSplitShadeExpanding()) {
            return false;
        }
        if (isTransitioningToFullShade()) {
            return true;
        }
        return this.isCrossFadeAnimatorRunning;
    }

    public final boolean isVisibleToUser() {
        return isLockScreenVisibleToUser() || isLockScreenShadeVisibleToUser() || isHomeScreenShadeVisibleToUser();
    }

    public final boolean isLockScreenVisibleToUser() {
        if (this.statusBarStateController.isDozing() || this.keyguardViewController.isBouncerShowing() || this.statusBarStateController.getState() != 1 || !this.notifLockscreenUserManager.shouldShowLockscreenNotifications() || !this.statusBarStateController.isExpanded() || this.qsExpanded) {
            return false;
        }
        return true;
    }

    public final boolean isLockScreenShadeVisibleToUser() {
        if (!this.statusBarStateController.isDozing() && !this.keyguardViewController.isBouncerShowing()) {
            if (this.statusBarStateController.getState() == 2) {
                return true;
            }
            if (this.statusBarStateController.getState() != 1 || !this.qsExpanded) {
                return false;
            }
            return true;
        }
        return false;
    }

    public final boolean isHomeScreenShadeVisibleToUser() {
        return !this.statusBarStateController.isDozing() && this.statusBarStateController.getState() == 0 && this.statusBarStateController.isExpanded();
    }

    /* compiled from: MediaHierarchyManager.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}
