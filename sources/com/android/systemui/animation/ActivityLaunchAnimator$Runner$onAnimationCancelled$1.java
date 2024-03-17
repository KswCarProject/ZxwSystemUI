package com.android.systemui.animation;

import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.LaunchAnimator;

/* compiled from: ActivityLaunchAnimator.kt */
public final class ActivityLaunchAnimator$Runner$onAnimationCancelled$1 implements Runnable {
    public final /* synthetic */ ActivityLaunchAnimator.Runner this$0;

    public ActivityLaunchAnimator$Runner$onAnimationCancelled$1(ActivityLaunchAnimator.Runner runner) {
        this.this$0 = runner;
    }

    public final void run() {
        LaunchAnimator.Animation access$getAnimation$p = this.this$0.animation;
        if (access$getAnimation$p != null) {
            access$getAnimation$p.cancel();
        }
        this.this$0.controller.onLaunchAnimationCancelled();
    }
}
