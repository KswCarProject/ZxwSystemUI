package com.android.systemui.statusbar.phone;

import android.view.View;
import android.view.ViewGroup;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.LaunchAnimator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: StatusBarLaunchAnimatorController.kt */
public final class StatusBarLaunchAnimatorController implements ActivityLaunchAnimator.Controller {
    @NotNull
    public final CentralSurfaces centralSurfaces;
    @NotNull
    public final ActivityLaunchAnimator.Controller delegate;
    public final boolean isLaunchForActivity;

    @NotNull
    public LaunchAnimator.State createAnimatorState() {
        return this.delegate.createAnimatorState();
    }

    @NotNull
    public ViewGroup getLaunchContainer() {
        return this.delegate.getLaunchContainer();
    }

    public boolean isDialogLaunch() {
        return this.delegate.isDialogLaunch();
    }

    public void setLaunchContainer(@NotNull ViewGroup viewGroup) {
        this.delegate.setLaunchContainer(viewGroup);
    }

    public StatusBarLaunchAnimatorController(@NotNull ActivityLaunchAnimator.Controller controller, @NotNull CentralSurfaces centralSurfaces2, boolean z) {
        this.delegate = controller;
        this.centralSurfaces = centralSurfaces2;
        this.isLaunchForActivity = z;
    }

    @Nullable
    public View getOpeningWindowSyncView() {
        return this.centralSurfaces.getNotificationShadeWindowView();
    }

    public void onIntentStarted(boolean z) {
        this.delegate.onIntentStarted(z);
        if (z) {
            this.centralSurfaces.getNotificationPanelViewController().setIsLaunchAnimationRunning(true);
        } else {
            this.centralSurfaces.collapsePanelOnMainThread();
        }
    }

    public void onLaunchAnimationStart(boolean z) {
        this.delegate.onLaunchAnimationStart(z);
        this.centralSurfaces.getNotificationPanelViewController().setIsLaunchAnimationRunning(true);
        if (!z) {
            this.centralSurfaces.collapsePanelWithDuration((int) ActivityLaunchAnimator.TIMINGS.getTotalDuration());
        }
    }

    public void onLaunchAnimationEnd(boolean z) {
        this.delegate.onLaunchAnimationEnd(z);
        this.centralSurfaces.getNotificationPanelViewController().setIsLaunchAnimationRunning(false);
        this.centralSurfaces.onLaunchAnimationEnd(z);
    }

    public void onLaunchAnimationProgress(@NotNull LaunchAnimator.State state, float f, float f2) {
        this.delegate.onLaunchAnimationProgress(state, f, f2);
        this.centralSurfaces.getNotificationPanelViewController().applyLaunchAnimationProgress(f2);
    }

    public void onLaunchAnimationCancelled() {
        this.delegate.onLaunchAnimationCancelled();
        this.centralSurfaces.getNotificationPanelViewController().setIsLaunchAnimationRunning(false);
        this.centralSurfaces.onLaunchAnimationCancelled(this.isLaunchForActivity);
    }
}
