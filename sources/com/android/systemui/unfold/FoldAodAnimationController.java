package com.android.systemui.unfold;

import android.content.Context;
import android.hardware.devicestate.DeviceStateManager;
import android.os.Handler;
import android.view.View;
import androidx.core.view.OneShotPreDrawListener;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.statusbar.LightRevealScrim;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.phone.ScreenOffAnimation;
import com.android.systemui.statusbar.policy.CallbackController;
import com.android.systemui.util.settings.GlobalSettings;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: FoldAodAnimationController.kt */
public final class FoldAodAnimationController implements CallbackController<FoldAodAnimationStatus>, ScreenOffAnimation, WakefulnessLifecycle.Observer {
    public boolean alwaysOnEnabled;
    @NotNull
    public final Context context;
    @NotNull
    public final DeviceStateManager deviceStateManager;
    @NotNull
    public final Executor executor;
    @NotNull
    public final GlobalSettings globalSettings;
    @NotNull
    public final Handler handler;
    public boolean isAnimationPlaying;
    public boolean isDozing;
    public boolean isFoldHandled = true;
    public boolean isFolded;
    public boolean isScrimOpaque;
    public CentralSurfaces mCentralSurfaces;
    @Nullable
    public Runnable pendingScrimReadyCallback;
    public boolean shouldPlayAnimation;
    @NotNull
    public final Runnable startAnimationRunnable = new FoldAodAnimationController$startAnimationRunnable$1(this);
    @NotNull
    public final ArrayList<FoldAodAnimationStatus> statusListeners = new ArrayList<>();
    @NotNull
    public final WakefulnessLifecycle wakefulnessLifecycle;

    /* compiled from: FoldAodAnimationController.kt */
    public interface FoldAodAnimationStatus {
        void onFoldToAodAnimationChanged();
    }

    public FoldAodAnimationController(@NotNull Handler handler2, @NotNull Executor executor2, @NotNull Context context2, @NotNull DeviceStateManager deviceStateManager2, @NotNull WakefulnessLifecycle wakefulnessLifecycle2, @NotNull GlobalSettings globalSettings2) {
        this.handler = handler2;
        this.executor = executor2;
        this.context = context2;
        this.deviceStateManager = deviceStateManager2;
        this.wakefulnessLifecycle = wakefulnessLifecycle2;
        this.globalSettings = globalSettings2;
    }

    public void animateInKeyguard(@NotNull View view, @NotNull Runnable runnable) {
        ScreenOffAnimation.DefaultImpls.animateInKeyguard(this, view, runnable);
    }

    public boolean isKeyguardShowDelayed() {
        return ScreenOffAnimation.DefaultImpls.isKeyguardShowDelayed(this);
    }

    public boolean overrideNotificationsDozeAmount() {
        return ScreenOffAnimation.DefaultImpls.overrideNotificationsDozeAmount(this);
    }

    public boolean shouldAnimateInKeyguard() {
        return ScreenOffAnimation.DefaultImpls.shouldAnimateInKeyguard(this);
    }

    public boolean shouldDelayKeyguardShow() {
        return ScreenOffAnimation.DefaultImpls.shouldDelayKeyguardShow(this);
    }

    public boolean shouldHideScrimOnWakeUp() {
        return ScreenOffAnimation.DefaultImpls.shouldHideScrimOnWakeUp(this);
    }

    public void initialize(@NotNull CentralSurfaces centralSurfaces, @NotNull LightRevealScrim lightRevealScrim) {
        this.mCentralSurfaces = centralSurfaces;
        this.deviceStateManager.registerCallback(this.executor, new FoldListener());
        this.wakefulnessLifecycle.addObserver(this);
    }

    public boolean shouldPlayAnimation() {
        return this.shouldPlayAnimation;
    }

    public boolean startAnimation() {
        if (!this.alwaysOnEnabled || this.wakefulnessLifecycle.getLastSleepReason() != 13 || Intrinsics.areEqual((Object) this.globalSettings.getString("animator_duration_scale"), (Object) "0")) {
            setAnimationState(false);
            return false;
        }
        setAnimationState(true);
        CentralSurfaces centralSurfaces = this.mCentralSurfaces;
        if (centralSurfaces == null) {
            centralSurfaces = null;
        }
        centralSurfaces.getNotificationPanelViewController().prepareFoldToAodAnimation();
        return true;
    }

    public void onStartedWakingUp() {
        if (this.isAnimationPlaying) {
            this.handler.removeCallbacks(this.startAnimationRunnable);
            CentralSurfaces centralSurfaces = this.mCentralSurfaces;
            if (centralSurfaces == null) {
                centralSurfaces = null;
            }
            centralSurfaces.getNotificationPanelViewController().cancelFoldToAodAnimation();
        }
        setAnimationState(false);
    }

    public final void setAnimationState(boolean z) {
        this.shouldPlayAnimation = z;
        this.isAnimationPlaying = z;
        for (FoldAodAnimationStatus onFoldToAodAnimationChanged : this.statusListeners) {
            onFoldToAodAnimationChanged.onFoldToAodAnimationChanged();
        }
    }

    public final void onScreenTurningOn(@NotNull Runnable runnable) {
        if (this.shouldPlayAnimation) {
            if (this.isScrimOpaque) {
                runnable.run();
            } else {
                this.pendingScrimReadyCallback = runnable;
            }
        } else if (!this.isFolded || this.isFoldHandled || !this.alwaysOnEnabled || !this.isDozing) {
            runnable.run();
        } else {
            setAnimationState(true);
            CentralSurfaces centralSurfaces = this.mCentralSurfaces;
            CentralSurfaces centralSurfaces2 = null;
            if (centralSurfaces == null) {
                centralSurfaces = null;
            }
            centralSurfaces.getNotificationPanelViewController().prepareFoldToAodAnimation();
            CentralSurfaces centralSurfaces3 = this.mCentralSurfaces;
            if (centralSurfaces3 != null) {
                centralSurfaces2 = centralSurfaces3;
            }
            OneShotPreDrawListener.add(centralSurfaces2.getNotificationPanelViewController().getView(), runnable);
        }
    }

    public void onScrimOpaqueChanged(boolean z) {
        this.isScrimOpaque = z;
        if (z) {
            Runnable runnable = this.pendingScrimReadyCallback;
            if (runnable != null) {
                runnable.run();
            }
            this.pendingScrimReadyCallback = null;
        }
    }

    public final void onScreenTurnedOn() {
        if (this.shouldPlayAnimation) {
            this.handler.removeCallbacks(this.startAnimationRunnable);
            this.handler.post(this.startAnimationRunnable);
            this.shouldPlayAnimation = false;
        }
    }

    public final void setIsDozing(boolean z) {
        this.isDozing = z;
    }

    public boolean isAnimationPlaying() {
        return this.isAnimationPlaying;
    }

    public boolean isKeyguardHideDelayed() {
        return isAnimationPlaying();
    }

    public boolean shouldShowAodIconsWhenShade() {
        return shouldPlayAnimation();
    }

    public boolean shouldAnimateAodIcons() {
        return !shouldPlayAnimation();
    }

    public boolean shouldAnimateDozingChange() {
        return !shouldPlayAnimation();
    }

    public boolean shouldAnimateClockChange() {
        return !isAnimationPlaying();
    }

    public boolean shouldDelayDisplayDozeTransition() {
        return shouldPlayAnimation();
    }

    public void onAlwaysOnChanged(boolean z) {
        this.alwaysOnEnabled = z;
    }

    public void addCallback(@NotNull FoldAodAnimationStatus foldAodAnimationStatus) {
        this.statusListeners.add(foldAodAnimationStatus);
    }

    public void removeCallback(@NotNull FoldAodAnimationStatus foldAodAnimationStatus) {
        this.statusListeners.remove(foldAodAnimationStatus);
    }

    /* compiled from: FoldAodAnimationController.kt */
    public final class FoldListener extends DeviceStateManager.FoldStateListener {
        public FoldListener() {
            super(FoldAodAnimationController.this.context, new Consumer(FoldAodAnimationController.this) {
                public final void accept(Boolean bool) {
                    if (!bool.booleanValue()) {
                        r3.isFoldHandled = false;
                    }
                    r3.isFolded = bool.booleanValue();
                }
            });
        }
    }
}
