package com.android.systemui.animation;

import android.animation.ValueAnimator;
import com.android.systemui.animation.LaunchAnimator;
import kotlin.jvm.internal.Ref$BooleanRef;

/* compiled from: LaunchAnimator.kt */
public final class LaunchAnimator$startAnimation$3 implements LaunchAnimator.Animation {
    public final /* synthetic */ ValueAnimator $animator;
    public final /* synthetic */ Ref$BooleanRef $cancelled;

    public LaunchAnimator$startAnimation$3(Ref$BooleanRef ref$BooleanRef, ValueAnimator valueAnimator) {
        this.$cancelled = ref$BooleanRef;
        this.$animator = valueAnimator;
    }

    public void cancel() {
        this.$cancelled.element = true;
        this.$animator.cancel();
    }
}
