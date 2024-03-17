package com.android.systemui.statusbar.phone;

import android.view.View;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.statusbar.LightRevealScrim;
import com.android.systemui.unfold.FoldAodAnimationController;
import com.android.systemui.unfold.SysUIUnfoldComponent;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import kotlin.collections.CollectionsKt__CollectionsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ScreenOffAnimationController.kt */
public final class ScreenOffAnimationController implements WakefulnessLifecycle.Observer {
    @NotNull
    public final List<ScreenOffAnimation> animations;
    @Nullable
    public final FoldAodAnimationController foldToAodAnimation;
    @NotNull
    public final WakefulnessLifecycle wakefulnessLifecycle;

    public ScreenOffAnimationController(@NotNull Optional<SysUIUnfoldComponent> optional, @NotNull UnlockedScreenOffAnimationController unlockedScreenOffAnimationController, @NotNull WakefulnessLifecycle wakefulnessLifecycle2) {
        this.wakefulnessLifecycle = wakefulnessLifecycle2;
        FoldAodAnimationController foldAodAnimationController = null;
        SysUIUnfoldComponent orElse = optional.orElse((Object) null);
        foldAodAnimationController = orElse != null ? orElse.getFoldAodAnimationController() : foldAodAnimationController;
        this.foldToAodAnimation = foldAodAnimationController;
        this.animations = CollectionsKt__CollectionsKt.listOfNotNull(foldAodAnimationController, unlockedScreenOffAnimationController);
    }

    public final void initialize(@NotNull CentralSurfaces centralSurfaces, @NotNull LightRevealScrim lightRevealScrim) {
        for (ScreenOffAnimation initialize : this.animations) {
            initialize.initialize(centralSurfaces, lightRevealScrim);
        }
        this.wakefulnessLifecycle.addObserver(this);
    }

    /* JADX WARNING: Removed duplicated region for block: B:1:0x0008 A[LOOP:0: B:1:0x0008->B:4:0x0018, LOOP_START] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onStartedGoingToSleep() {
        /*
            r1 = this;
            java.util.List<com.android.systemui.statusbar.phone.ScreenOffAnimation> r1 = r1.animations
            java.lang.Iterable r1 = (java.lang.Iterable) r1
            java.util.Iterator r1 = r1.iterator()
        L_0x0008:
            boolean r0 = r1.hasNext()
            if (r0 == 0) goto L_0x001a
            java.lang.Object r0 = r1.next()
            com.android.systemui.statusbar.phone.ScreenOffAnimation r0 = (com.android.systemui.statusbar.phone.ScreenOffAnimation) r0
            boolean r0 = r0.startAnimation()
            if (r0 == 0) goto L_0x0008
        L_0x001a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.ScreenOffAnimationController.onStartedGoingToSleep():void");
    }

    public final void onScrimOpaqueChanged(boolean z) {
        for (ScreenOffAnimation onScrimOpaqueChanged : this.animations) {
            onScrimOpaqueChanged.onScrimOpaqueChanged(z);
        }
    }

    public final void onAlwaysOnChanged(boolean z) {
        for (ScreenOffAnimation onAlwaysOnChanged : this.animations) {
            onAlwaysOnChanged.onAlwaysOnChanged(z);
        }
    }

    public final boolean shouldExpandNotifications() {
        Iterable<ScreenOffAnimation> iterable = this.animations;
        if ((iterable instanceof Collection) && ((Collection) iterable).isEmpty()) {
            return false;
        }
        for (ScreenOffAnimation isAnimationPlaying : iterable) {
            if (isAnimationPlaying.isAnimationPlaying()) {
                return true;
            }
        }
        return false;
    }

    public final boolean shouldAnimateInKeyguard() {
        Iterable<ScreenOffAnimation> iterable = this.animations;
        if ((iterable instanceof Collection) && ((Collection) iterable).isEmpty()) {
            return false;
        }
        for (ScreenOffAnimation shouldAnimateInKeyguard : iterable) {
            if (shouldAnimateInKeyguard.shouldAnimateInKeyguard()) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    public final ScreenOffAnimation animateInKeyguard(@NotNull View view, @NotNull Runnable runnable) {
        Object obj;
        boolean z;
        Iterator it = this.animations.iterator();
        while (true) {
            if (!it.hasNext()) {
                obj = null;
                break;
            }
            obj = it.next();
            ScreenOffAnimation screenOffAnimation = (ScreenOffAnimation) obj;
            if (screenOffAnimation.shouldAnimateInKeyguard()) {
                screenOffAnimation.animateInKeyguard(view, runnable);
                z = true;
                continue;
            } else {
                z = false;
                continue;
            }
            if (z) {
                break;
            }
        }
        return (ScreenOffAnimation) obj;
    }

    public final boolean shouldIgnoreKeyguardTouches() {
        Iterable<ScreenOffAnimation> iterable = this.animations;
        if ((iterable instanceof Collection) && ((Collection) iterable).isEmpty()) {
            return false;
        }
        for (ScreenOffAnimation isAnimationPlaying : iterable) {
            if (isAnimationPlaying.isAnimationPlaying()) {
                return true;
            }
        }
        return false;
    }

    public final boolean allowWakeUpIfDozing() {
        Iterable<ScreenOffAnimation> iterable = this.animations;
        if ((iterable instanceof Collection) && ((Collection) iterable).isEmpty()) {
            return true;
        }
        for (ScreenOffAnimation isAnimationPlaying : iterable) {
            if (!(!isAnimationPlaying.isAnimationPlaying())) {
                return false;
            }
        }
        return true;
    }

    public final boolean shouldDelayKeyguardShow() {
        Iterable<ScreenOffAnimation> iterable = this.animations;
        if ((iterable instanceof Collection) && ((Collection) iterable).isEmpty()) {
            return false;
        }
        for (ScreenOffAnimation shouldDelayKeyguardShow : iterable) {
            if (shouldDelayKeyguardShow.shouldDelayKeyguardShow()) {
                return true;
            }
        }
        return false;
    }

    public final boolean isKeyguardShowDelayed() {
        Iterable<ScreenOffAnimation> iterable = this.animations;
        if ((iterable instanceof Collection) && ((Collection) iterable).isEmpty()) {
            return false;
        }
        for (ScreenOffAnimation isKeyguardShowDelayed : iterable) {
            if (isKeyguardShowDelayed.isKeyguardShowDelayed()) {
                return true;
            }
        }
        return false;
    }

    public final boolean isKeyguardHideDelayed() {
        Iterable<ScreenOffAnimation> iterable = this.animations;
        if ((iterable instanceof Collection) && ((Collection) iterable).isEmpty()) {
            return false;
        }
        for (ScreenOffAnimation isKeyguardHideDelayed : iterable) {
            if (isKeyguardHideDelayed.isKeyguardHideDelayed()) {
                return true;
            }
        }
        return false;
    }

    public final boolean shouldShowLightRevealScrim() {
        Iterable<ScreenOffAnimation> iterable = this.animations;
        if ((iterable instanceof Collection) && ((Collection) iterable).isEmpty()) {
            return false;
        }
        for (ScreenOffAnimation shouldPlayAnimation : iterable) {
            if (shouldPlayAnimation.shouldPlayAnimation()) {
                return true;
            }
        }
        return false;
    }

    public final boolean shouldHideLightRevealScrimOnWakeUp() {
        Iterable<ScreenOffAnimation> iterable = this.animations;
        if ((iterable instanceof Collection) && ((Collection) iterable).isEmpty()) {
            return false;
        }
        for (ScreenOffAnimation shouldHideScrimOnWakeUp : iterable) {
            if (shouldHideScrimOnWakeUp.shouldHideScrimOnWakeUp()) {
                return true;
            }
        }
        return false;
    }

    public final boolean overrideNotificationsFullyDozingOnKeyguard() {
        Iterable<ScreenOffAnimation> iterable = this.animations;
        if ((iterable instanceof Collection) && ((Collection) iterable).isEmpty()) {
            return false;
        }
        for (ScreenOffAnimation overrideNotificationsDozeAmount : iterable) {
            if (overrideNotificationsDozeAmount.overrideNotificationsDozeAmount()) {
                return true;
            }
        }
        return false;
    }

    public final boolean shouldHideNotificationsFooter() {
        Iterable<ScreenOffAnimation> iterable = this.animations;
        if ((iterable instanceof Collection) && ((Collection) iterable).isEmpty()) {
            return false;
        }
        for (ScreenOffAnimation isAnimationPlaying : iterable) {
            if (isAnimationPlaying.isAnimationPlaying()) {
                return true;
            }
        }
        return false;
    }

    public final boolean shouldClampDozeScreenBrightness() {
        Iterable<ScreenOffAnimation> iterable = this.animations;
        if ((iterable instanceof Collection) && ((Collection) iterable).isEmpty()) {
            return false;
        }
        for (ScreenOffAnimation shouldPlayAnimation : iterable) {
            if (shouldPlayAnimation.shouldPlayAnimation()) {
                return true;
            }
        }
        return false;
    }

    public final boolean shouldShowAodIconsWhenShade() {
        Iterable<ScreenOffAnimation> iterable = this.animations;
        if ((iterable instanceof Collection) && ((Collection) iterable).isEmpty()) {
            return false;
        }
        for (ScreenOffAnimation shouldShowAodIconsWhenShade : iterable) {
            if (shouldShowAodIconsWhenShade.shouldShowAodIconsWhenShade()) {
                return true;
            }
        }
        return false;
    }

    public final boolean shouldAnimateAodIcons() {
        Iterable<ScreenOffAnimation> iterable = this.animations;
        if ((iterable instanceof Collection) && ((Collection) iterable).isEmpty()) {
            return true;
        }
        for (ScreenOffAnimation shouldAnimateAodIcons : iterable) {
            if (!shouldAnimateAodIcons.shouldAnimateAodIcons()) {
                return false;
            }
        }
        return true;
    }

    public final boolean shouldAnimateDozingChange() {
        Iterable<ScreenOffAnimation> iterable = this.animations;
        if ((iterable instanceof Collection) && ((Collection) iterable).isEmpty()) {
            return true;
        }
        for (ScreenOffAnimation shouldAnimateDozingChange : iterable) {
            if (!shouldAnimateDozingChange.shouldAnimateDozingChange()) {
                return false;
            }
        }
        return true;
    }

    public final boolean shouldDelayDisplayDozeTransition() {
        Iterable<ScreenOffAnimation> iterable = this.animations;
        if ((iterable instanceof Collection) && ((Collection) iterable).isEmpty()) {
            return false;
        }
        for (ScreenOffAnimation shouldDelayDisplayDozeTransition : iterable) {
            if (shouldDelayDisplayDozeTransition.shouldDelayDisplayDozeTransition()) {
                return true;
            }
        }
        return false;
    }

    public final boolean shouldAnimateClockChange() {
        Iterable<ScreenOffAnimation> iterable = this.animations;
        if ((iterable instanceof Collection) && ((Collection) iterable).isEmpty()) {
            return true;
        }
        for (ScreenOffAnimation shouldAnimateClockChange : iterable) {
            if (!shouldAnimateClockChange.shouldAnimateClockChange()) {
                return false;
            }
        }
        return true;
    }
}
