package com.android.systemui.statusbar.phone;

import android.view.View;
import com.android.systemui.statusbar.LightRevealScrim;
import org.jetbrains.annotations.NotNull;

/* compiled from: ScreenOffAnimationController.kt */
public interface ScreenOffAnimation {
    void animateInKeyguard(@NotNull View view, @NotNull Runnable runnable);

    void initialize(@NotNull CentralSurfaces centralSurfaces, @NotNull LightRevealScrim lightRevealScrim);

    boolean isAnimationPlaying();

    boolean isKeyguardHideDelayed();

    boolean isKeyguardShowDelayed();

    void onAlwaysOnChanged(boolean z);

    void onScrimOpaqueChanged(boolean z);

    boolean overrideNotificationsDozeAmount();

    boolean shouldAnimateAodIcons();

    boolean shouldAnimateClockChange();

    boolean shouldAnimateDozingChange();

    boolean shouldAnimateInKeyguard();

    boolean shouldDelayDisplayDozeTransition();

    boolean shouldDelayKeyguardShow();

    boolean shouldHideScrimOnWakeUp();

    boolean shouldPlayAnimation();

    boolean shouldShowAodIconsWhenShade();

    boolean startAnimation();

    /* compiled from: ScreenOffAnimationController.kt */
    public static final class DefaultImpls {
        public static boolean isKeyguardHideDelayed(@NotNull ScreenOffAnimation screenOffAnimation) {
            return false;
        }

        public static boolean isKeyguardShowDelayed(@NotNull ScreenOffAnimation screenOffAnimation) {
            return false;
        }

        public static void onAlwaysOnChanged(@NotNull ScreenOffAnimation screenOffAnimation, boolean z) {
        }

        public static void onScrimOpaqueChanged(@NotNull ScreenOffAnimation screenOffAnimation, boolean z) {
        }

        public static boolean overrideNotificationsDozeAmount(@NotNull ScreenOffAnimation screenOffAnimation) {
            return false;
        }

        public static boolean shouldAnimateClockChange(@NotNull ScreenOffAnimation screenOffAnimation) {
            return true;
        }

        public static boolean shouldAnimateDozingChange(@NotNull ScreenOffAnimation screenOffAnimation) {
            return true;
        }

        public static boolean shouldAnimateInKeyguard(@NotNull ScreenOffAnimation screenOffAnimation) {
            return false;
        }

        public static boolean shouldDelayKeyguardShow(@NotNull ScreenOffAnimation screenOffAnimation) {
            return false;
        }

        public static boolean shouldHideScrimOnWakeUp(@NotNull ScreenOffAnimation screenOffAnimation) {
            return false;
        }

        public static void animateInKeyguard(@NotNull ScreenOffAnimation screenOffAnimation, @NotNull View view, @NotNull Runnable runnable) {
            runnable.run();
        }
    }
}
