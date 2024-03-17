package com.android.systemui.statusbar.phone;

import android.animation.ValueAnimator;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.View;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.statusbar.LightRevealScrim;
import com.android.systemui.statusbar.StatusBarStateControllerImpl;
import com.android.systemui.statusbar.notification.AnimatableProperty;
import com.android.systemui.statusbar.notification.PropertyAnimator;
import com.android.systemui.statusbar.notification.stack.AnimationProperties;
import com.android.systemui.statusbar.phone.ScreenOffAnimation;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.settings.GlobalSettings;
import dagger.Lazy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: UnlockedScreenOffAnimationController.kt */
public final class UnlockedScreenOffAnimationController implements WakefulnessLifecycle.Observer, ScreenOffAnimation {
    public float animatorDurationScale = 1.0f;
    @NotNull
    public final ContentObserver animatorDurationScaleObserver;
    public boolean aodUiAnimationPlaying;
    @NotNull
    public final Context context;
    @Nullable
    public Boolean decidedToAnimateGoingToSleep;
    @NotNull
    public final Lazy<DozeParameters> dozeParameters;
    @NotNull
    public final GlobalSettings globalSettings;
    @NotNull
    public final Handler handler;
    public boolean initialized;
    @NotNull
    public final InteractionJankMonitor interactionJankMonitor;
    @NotNull
    public final KeyguardStateController keyguardStateController;
    @NotNull
    public final Lazy<KeyguardViewMediator> keyguardViewMediatorLazy;
    public boolean lightRevealAnimationPlaying;
    public final ValueAnimator lightRevealAnimator;
    public LightRevealScrim lightRevealScrim;
    public CentralSurfaces mCentralSurfaces;
    @NotNull
    public final PowerManager powerManager;
    public boolean shouldAnimateInKeyguard;
    @NotNull
    public final StatusBarStateControllerImpl statusBarStateControllerImpl;
    @NotNull
    public final WakefulnessLifecycle wakefulnessLifecycle;

    public UnlockedScreenOffAnimationController(@NotNull Context context2, @NotNull WakefulnessLifecycle wakefulnessLifecycle2, @NotNull StatusBarStateControllerImpl statusBarStateControllerImpl2, @NotNull Lazy<KeyguardViewMediator> lazy, @NotNull KeyguardStateController keyguardStateController2, @NotNull Lazy<DozeParameters> lazy2, @NotNull GlobalSettings globalSettings2, @NotNull InteractionJankMonitor interactionJankMonitor2, @NotNull PowerManager powerManager2, @NotNull Handler handler2) {
        this.context = context2;
        this.wakefulnessLifecycle = wakefulnessLifecycle2;
        this.statusBarStateControllerImpl = statusBarStateControllerImpl2;
        this.keyguardViewMediatorLazy = lazy;
        this.keyguardStateController = keyguardStateController2;
        this.dozeParameters = lazy2;
        this.globalSettings = globalSettings2;
        this.interactionJankMonitor = interactionJankMonitor2;
        this.powerManager = powerManager2;
        this.handler = handler2;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{1.0f, 0.0f});
        ofFloat.setDuration(750);
        ofFloat.setInterpolator(Interpolators.LINEAR);
        ofFloat.addUpdateListener(new UnlockedScreenOffAnimationController$lightRevealAnimator$1$1(this));
        ofFloat.addListener(new UnlockedScreenOffAnimationController$lightRevealAnimator$1$2(this));
        this.lightRevealAnimator = ofFloat;
        this.animatorDurationScaleObserver = new UnlockedScreenOffAnimationController$animatorDurationScaleObserver$1(this);
    }

    public boolean isKeyguardHideDelayed() {
        return ScreenOffAnimation.DefaultImpls.isKeyguardHideDelayed(this);
    }

    public void onAlwaysOnChanged(boolean z) {
        ScreenOffAnimation.DefaultImpls.onAlwaysOnChanged(this, z);
    }

    public void onScrimOpaqueChanged(boolean z) {
        ScreenOffAnimation.DefaultImpls.onScrimOpaqueChanged(this, z);
    }

    public boolean shouldAnimateClockChange() {
        return ScreenOffAnimation.DefaultImpls.shouldAnimateClockChange(this);
    }

    public boolean shouldAnimateDozingChange() {
        return ScreenOffAnimation.DefaultImpls.shouldAnimateDozingChange(this);
    }

    public void initialize(@NotNull CentralSurfaces centralSurfaces, @NotNull LightRevealScrim lightRevealScrim2) {
        this.initialized = true;
        this.lightRevealScrim = lightRevealScrim2;
        this.mCentralSurfaces = centralSurfaces;
        updateAnimatorDurationScale();
        this.globalSettings.registerContentObserver(Settings.Global.getUriFor("animator_duration_scale"), false, this.animatorDurationScaleObserver);
        this.wakefulnessLifecycle.addObserver(this);
    }

    public final void updateAnimatorDurationScale() {
        this.animatorDurationScale = this.globalSettings.getFloat("animator_duration_scale", 1.0f);
    }

    public boolean shouldDelayKeyguardShow() {
        return shouldPlayAnimation();
    }

    public boolean isKeyguardShowDelayed() {
        return isAnimationPlaying();
    }

    public void animateInKeyguard(@NotNull View view, @NotNull Runnable runnable) {
        this.shouldAnimateInKeyguard = false;
        view.setAlpha(0.0f);
        view.setVisibility(0);
        float y = view.getY();
        view.setY(y - (((float) view.getHeight()) * 0.1f));
        AnimatableProperty animatableProperty = AnimatableProperty.Y;
        PropertyAnimator.cancelAnimation(view, animatableProperty);
        long j = (long) 500;
        PropertyAnimator.setProperty(view, animatableProperty, y, new AnimationProperties().setDuration(j), true);
        view.animate().setDuration(j).setInterpolator(Interpolators.FAST_OUT_SLOW_IN).alpha(1.0f).withEndAction(new UnlockedScreenOffAnimationController$animateInKeyguard$1(this, runnable, view)).setListener(new UnlockedScreenOffAnimationController$animateInKeyguard$2(this, view)).start();
    }

    public void onStartedWakingUp() {
        this.decidedToAnimateGoingToSleep = null;
        this.shouldAnimateInKeyguard = false;
        this.lightRevealAnimator.cancel();
        this.handler.removeCallbacksAndMessages((Object) null);
    }

    public void onFinishedWakingUp() {
        this.aodUiAnimationPlaying = false;
        if (this.dozeParameters.get().canControlUnlockedScreenOff()) {
            CentralSurfaces centralSurfaces = this.mCentralSurfaces;
            if (centralSurfaces == null) {
                centralSurfaces = null;
            }
            centralSurfaces.updateIsKeyguard(true);
        }
    }

    public boolean startAnimation() {
        if (shouldPlayUnlockedScreenOffAnimation()) {
            this.decidedToAnimateGoingToSleep = Boolean.TRUE;
            this.shouldAnimateInKeyguard = true;
            this.lightRevealAnimationPlaying = true;
            this.lightRevealAnimator.start();
            this.handler.postDelayed(new UnlockedScreenOffAnimationController$startAnimation$1(this), (long) (((float) 600) * this.animatorDurationScale));
            return true;
        }
        this.decidedToAnimateGoingToSleep = Boolean.FALSE;
        return false;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x004d, code lost:
        if (r0.getNotificationPanelViewController().isPanelExpanded() != false) goto L_0x004f;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean shouldPlayUnlockedScreenOffAnimation() {
        /*
            r3 = this;
            boolean r0 = r3.initialized
            r1 = 0
            if (r0 != 0) goto L_0x0006
            return r1
        L_0x0006:
            dagger.Lazy<com.android.systemui.statusbar.phone.DozeParameters> r0 = r3.dozeParameters
            java.lang.Object r0 = r0.get()
            com.android.systemui.statusbar.phone.DozeParameters r0 = (com.android.systemui.statusbar.phone.DozeParameters) r0
            boolean r0 = r0.canControlUnlockedScreenOff()
            if (r0 != 0) goto L_0x0015
            return r1
        L_0x0015:
            java.lang.Boolean r0 = r3.decidedToAnimateGoingToSleep
            java.lang.Boolean r2 = java.lang.Boolean.FALSE
            boolean r0 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r0, (java.lang.Object) r2)
            if (r0 == 0) goto L_0x0020
            return r1
        L_0x0020:
            android.content.Context r0 = r3.context
            android.content.ContentResolver r0 = r0.getContentResolver()
            java.lang.String r2 = "animator_duration_scale"
            java.lang.String r0 = android.provider.Settings.Global.getString(r0, r2)
            java.lang.String r2 = "0"
            boolean r0 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r0, (java.lang.Object) r2)
            if (r0 == 0) goto L_0x0035
            return r1
        L_0x0035:
            com.android.systemui.statusbar.StatusBarStateControllerImpl r0 = r3.statusBarStateControllerImpl
            int r0 = r0.getState()
            if (r0 == 0) goto L_0x003e
            return r1
        L_0x003e:
            com.android.systemui.statusbar.phone.CentralSurfaces r0 = r3.mCentralSurfaces
            if (r0 == 0) goto L_0x004f
            if (r0 != 0) goto L_0x0045
            r0 = 0
        L_0x0045:
            com.android.systemui.statusbar.phone.NotificationPanelViewController r0 = r0.getNotificationPanelViewController()
            boolean r0 = r0.isPanelExpanded()
            if (r0 == 0) goto L_0x0056
        L_0x004f:
            boolean r0 = r3.isAnimationPlaying()
            if (r0 != 0) goto L_0x0056
            return r1
        L_0x0056:
            com.android.systemui.statusbar.policy.KeyguardStateController r0 = r3.keyguardStateController
            boolean r0 = r0.isKeyguardScreenRotationAllowed()
            if (r0 != 0) goto L_0x006b
            android.content.Context r3 = r3.context
            android.view.Display r3 = r3.getDisplay()
            int r3 = r3.getRotation()
            if (r3 == 0) goto L_0x006b
            return r1
        L_0x006b:
            r3 = 1
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController.shouldPlayUnlockedScreenOffAnimation():boolean");
    }

    public boolean shouldDelayDisplayDozeTransition() {
        return shouldPlayUnlockedScreenOffAnimation();
    }

    public boolean isAnimationPlaying() {
        return this.lightRevealAnimationPlaying || this.aodUiAnimationPlaying;
    }

    public boolean shouldAnimateInKeyguard() {
        return this.shouldAnimateInKeyguard;
    }

    public boolean shouldHideScrimOnWakeUp() {
        return isScreenOffLightRevealAnimationPlaying();
    }

    public boolean overrideNotificationsDozeAmount() {
        return shouldPlayUnlockedScreenOffAnimation() && isAnimationPlaying();
    }

    public boolean shouldShowAodIconsWhenShade() {
        return isAnimationPlaying();
    }

    public boolean shouldAnimateAodIcons() {
        return shouldPlayUnlockedScreenOffAnimation();
    }

    public boolean shouldPlayAnimation() {
        return shouldPlayUnlockedScreenOffAnimation();
    }

    public final boolean isScreenOffLightRevealAnimationPlaying() {
        return this.lightRevealAnimationPlaying;
    }
}
