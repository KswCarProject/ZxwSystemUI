package com.android.systemui.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.drawable.GradientDrawable;
import android.view.ViewGroupOverlay;
import android.view.ViewOverlay;
import com.android.systemui.animation.LaunchAnimator;
import org.jetbrains.annotations.Nullable;

/* compiled from: LaunchAnimator.kt */
public final class LaunchAnimator$startAnimation$1 extends AnimatorListenerAdapter {
    public final /* synthetic */ LaunchAnimator.Controller $controller;
    public final /* synthetic */ boolean $isExpandingFullyAbove;
    public final /* synthetic */ ViewGroupOverlay $launchContainerOverlay;
    public final /* synthetic */ boolean $moveBackgroundLayerWhenAppIsVisible;
    public final /* synthetic */ ViewOverlay $openingWindowSyncViewOverlay;
    public final /* synthetic */ GradientDrawable $windowBackgroundLayer;

    public LaunchAnimator$startAnimation$1(LaunchAnimator.Controller controller, boolean z, ViewGroupOverlay viewGroupOverlay, GradientDrawable gradientDrawable, boolean z2, ViewOverlay viewOverlay) {
        this.$controller = controller;
        this.$isExpandingFullyAbove = z;
        this.$launchContainerOverlay = viewGroupOverlay;
        this.$windowBackgroundLayer = gradientDrawable;
        this.$moveBackgroundLayerWhenAppIsVisible = z2;
        this.$openingWindowSyncViewOverlay = viewOverlay;
    }

    public void onAnimationStart(@Nullable Animator animator, boolean z) {
        this.$controller.onLaunchAnimationStart(this.$isExpandingFullyAbove);
        this.$launchContainerOverlay.add(this.$windowBackgroundLayer);
    }

    public void onAnimationEnd(@Nullable Animator animator) {
        ViewOverlay viewOverlay;
        this.$controller.onLaunchAnimationEnd(this.$isExpandingFullyAbove);
        this.$launchContainerOverlay.remove(this.$windowBackgroundLayer);
        if (this.$moveBackgroundLayerWhenAppIsVisible && (viewOverlay = this.$openingWindowSyncViewOverlay) != null) {
            viewOverlay.remove(this.$windowBackgroundLayer);
        }
    }
}
