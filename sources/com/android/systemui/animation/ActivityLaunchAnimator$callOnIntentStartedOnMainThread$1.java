package com.android.systemui.animation;

import com.android.systemui.animation.ActivityLaunchAnimator;

/* compiled from: ActivityLaunchAnimator.kt */
public final class ActivityLaunchAnimator$callOnIntentStartedOnMainThread$1 implements Runnable {
    public final /* synthetic */ ActivityLaunchAnimator.Controller $this_callOnIntentStartedOnMainThread;
    public final /* synthetic */ boolean $willAnimate;

    public ActivityLaunchAnimator$callOnIntentStartedOnMainThread$1(ActivityLaunchAnimator.Controller controller, boolean z) {
        this.$this_callOnIntentStartedOnMainThread = controller;
        this.$willAnimate = z;
    }

    public final void run() {
        this.$this_callOnIntentStartedOnMainThread.onIntentStarted(this.$willAnimate);
    }
}
