package com.android.systemui.animation;

import android.view.ViewGroup;
import com.android.systemui.animation.LaunchAnimator;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import org.jetbrains.annotations.NotNull;

/* compiled from: DialogLaunchAnimator.kt */
public final class AnimatedDialog$startAnimation$controller$1 implements LaunchAnimator.Controller {
    public final /* synthetic */ LaunchAnimator.State $endState;
    public final /* synthetic */ GhostedViewLaunchAnimatorController $endViewController;
    public final /* synthetic */ Function0<Unit> $onLaunchAnimationEnd;
    public final /* synthetic */ Function0<Unit> $onLaunchAnimationStart;
    public final /* synthetic */ GhostedViewLaunchAnimatorController $startViewController;

    public AnimatedDialog$startAnimation$controller$1(GhostedViewLaunchAnimatorController ghostedViewLaunchAnimatorController, GhostedViewLaunchAnimatorController ghostedViewLaunchAnimatorController2, Function0<Unit> function0, Function0<Unit> function02, LaunchAnimator.State state) {
        this.$startViewController = ghostedViewLaunchAnimatorController;
        this.$endViewController = ghostedViewLaunchAnimatorController2;
        this.$onLaunchAnimationStart = function0;
        this.$onLaunchAnimationEnd = function02;
        this.$endState = state;
    }

    @NotNull
    public ViewGroup getLaunchContainer() {
        return this.$startViewController.getLaunchContainer();
    }

    @NotNull
    public LaunchAnimator.State createAnimatorState() {
        return this.$startViewController.createAnimatorState();
    }

    public void onLaunchAnimationStart(boolean z) {
        this.$onLaunchAnimationStart.invoke();
        this.$startViewController.onLaunchAnimationStart(z);
        this.$endViewController.onLaunchAnimationStart(z);
    }

    public void onLaunchAnimationEnd(boolean z) {
        this.$startViewController.onLaunchAnimationEnd(z);
        this.$endViewController.onLaunchAnimationEnd(z);
        this.$onLaunchAnimationEnd.invoke();
    }

    public void onLaunchAnimationProgress(@NotNull LaunchAnimator.State state, float f, float f2) {
        this.$startViewController.onLaunchAnimationProgress(state, f, f2);
        state.setVisible(!state.getVisible());
        this.$endViewController.onLaunchAnimationProgress(state, f, f2);
        this.$endViewController.fillGhostedViewState(this.$endState);
    }
}
