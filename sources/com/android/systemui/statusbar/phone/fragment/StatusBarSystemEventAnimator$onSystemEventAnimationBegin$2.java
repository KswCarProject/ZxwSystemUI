package com.android.systemui.statusbar.phone.fragment;

import android.animation.ValueAnimator;
import android.view.View;
import org.jetbrains.annotations.NotNull;

/* compiled from: StatusBarSystemEventAnimator.kt */
public final class StatusBarSystemEventAnimator$onSystemEventAnimationBegin$2 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ StatusBarSystemEventAnimator this$0;

    public StatusBarSystemEventAnimator$onSystemEventAnimationBegin$2(StatusBarSystemEventAnimator statusBarSystemEventAnimator) {
        this.this$0 = statusBarSystemEventAnimator;
    }

    public final void onAnimationUpdate(@NotNull ValueAnimator valueAnimator) {
        View animatedView = this.this$0.getAnimatedView();
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            animatedView.setAlpha(((Float) animatedValue).floatValue());
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Float");
    }
}
