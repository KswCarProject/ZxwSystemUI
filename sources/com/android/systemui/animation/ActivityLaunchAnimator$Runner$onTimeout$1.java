package com.android.systemui.animation;

import com.android.systemui.animation.ActivityLaunchAnimator;

/* compiled from: ActivityLaunchAnimator.kt */
public final class ActivityLaunchAnimator$Runner$onTimeout$1 implements Runnable {
    public final /* synthetic */ ActivityLaunchAnimator.Runner this$0;

    public ActivityLaunchAnimator$Runner$onTimeout$1(ActivityLaunchAnimator.Runner runner) {
        this.this$0 = runner;
    }

    public final void run() {
        this.this$0.onAnimationTimedOut();
    }
}
