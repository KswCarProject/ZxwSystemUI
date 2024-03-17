package com.android.systemui.animation;

import android.view.View;
import android.view.ViewGroup;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.LaunchAnimator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: DelegateLaunchAnimatorController.kt */
public class DelegateLaunchAnimatorController implements ActivityLaunchAnimator.Controller {
    @NotNull
    public final ActivityLaunchAnimator.Controller delegate;

    @NotNull
    public LaunchAnimator.State createAnimatorState() {
        return this.delegate.createAnimatorState();
    }

    @NotNull
    public ViewGroup getLaunchContainer() {
        return this.delegate.getLaunchContainer();
    }

    @Nullable
    public View getOpeningWindowSyncView() {
        return this.delegate.getOpeningWindowSyncView();
    }

    public boolean isDialogLaunch() {
        return this.delegate.isDialogLaunch();
    }

    public void onIntentStarted(boolean z) {
        this.delegate.onIntentStarted(z);
    }

    public void onLaunchAnimationCancelled() {
        this.delegate.onLaunchAnimationCancelled();
    }

    public void onLaunchAnimationProgress(@NotNull LaunchAnimator.State state, float f, float f2) {
        this.delegate.onLaunchAnimationProgress(state, f, f2);
    }

    public void onLaunchAnimationStart(boolean z) {
        this.delegate.onLaunchAnimationStart(z);
    }

    public void setLaunchContainer(@NotNull ViewGroup viewGroup) {
        this.delegate.setLaunchContainer(viewGroup);
    }

    public DelegateLaunchAnimatorController(@NotNull ActivityLaunchAnimator.Controller controller) {
        this.delegate = controller;
    }

    @NotNull
    public final ActivityLaunchAnimator.Controller getDelegate() {
        return this.delegate;
    }
}
