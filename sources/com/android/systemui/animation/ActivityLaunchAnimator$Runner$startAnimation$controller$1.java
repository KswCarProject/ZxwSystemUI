package com.android.systemui.animation;

import android.view.IRemoteAnimationFinishedCallback;
import android.view.RemoteAnimationTarget;
import android.view.View;
import android.view.ViewGroup;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.LaunchAnimator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ActivityLaunchAnimator.kt */
public final class ActivityLaunchAnimator$Runner$startAnimation$controller$1 implements LaunchAnimator.Controller {
    public final /* synthetic */ ActivityLaunchAnimator.Controller $$delegate_0;
    public final /* synthetic */ ActivityLaunchAnimator.Controller $delegate;
    public final /* synthetic */ IRemoteAnimationFinishedCallback $iCallback;
    public final /* synthetic */ RemoteAnimationTarget $navigationBar;
    public final /* synthetic */ RemoteAnimationTarget $window;
    public final /* synthetic */ ActivityLaunchAnimator this$0;
    public final /* synthetic */ ActivityLaunchAnimator.Runner this$1;

    @NotNull
    public LaunchAnimator.State createAnimatorState() {
        return this.$$delegate_0.createAnimatorState();
    }

    @NotNull
    public ViewGroup getLaunchContainer() {
        return this.$$delegate_0.getLaunchContainer();
    }

    @Nullable
    public View getOpeningWindowSyncView() {
        return this.$$delegate_0.getOpeningWindowSyncView();
    }

    public ActivityLaunchAnimator$Runner$startAnimation$controller$1(ActivityLaunchAnimator.Controller controller, ActivityLaunchAnimator activityLaunchAnimator, IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback, ActivityLaunchAnimator.Runner runner, RemoteAnimationTarget remoteAnimationTarget, RemoteAnimationTarget remoteAnimationTarget2) {
        this.$delegate = controller;
        this.this$0 = activityLaunchAnimator;
        this.$iCallback = iRemoteAnimationFinishedCallback;
        this.this$1 = runner;
        this.$window = remoteAnimationTarget;
        this.$navigationBar = remoteAnimationTarget2;
        this.$$delegate_0 = controller;
    }

    public void onLaunchAnimationStart(boolean z) {
        for (ActivityLaunchAnimator.Listener onLaunchAnimationStart : this.this$0.listeners) {
            onLaunchAnimationStart.onLaunchAnimationStart();
        }
        this.$delegate.onLaunchAnimationStart(z);
    }

    public void onLaunchAnimationEnd(boolean z) {
        for (ActivityLaunchAnimator.Listener onLaunchAnimationEnd : this.this$0.listeners) {
            onLaunchAnimationEnd.onLaunchAnimationEnd();
        }
        IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback = this.$iCallback;
        if (iRemoteAnimationFinishedCallback != null) {
            this.this$1.invoke(iRemoteAnimationFinishedCallback);
        }
        this.$delegate.onLaunchAnimationEnd(z);
    }

    public void onLaunchAnimationProgress(@NotNull LaunchAnimator.State state, float f, float f2) {
        if (!state.getVisible()) {
            this.this$1.applyStateToWindow(this.$window, state);
        }
        RemoteAnimationTarget remoteAnimationTarget = this.$navigationBar;
        if (remoteAnimationTarget != null) {
            this.this$1.applyStateToNavigationBar(remoteAnimationTarget, state, f2);
        }
        for (ActivityLaunchAnimator.Listener onLaunchAnimationProgress : this.this$0.listeners) {
            onLaunchAnimationProgress.onLaunchAnimationProgress(f2);
        }
        this.$delegate.onLaunchAnimationProgress(state, f, f2);
    }
}
