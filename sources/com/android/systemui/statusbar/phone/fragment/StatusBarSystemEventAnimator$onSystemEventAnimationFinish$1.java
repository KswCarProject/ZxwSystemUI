package com.android.systemui.statusbar.phone.fragment;

import android.animation.ValueAnimator;
import android.view.View;
import org.jetbrains.annotations.NotNull;

/* compiled from: StatusBarSystemEventAnimator.kt */
public final class StatusBarSystemEventAnimator$onSystemEventAnimationFinish$1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ StatusBarSystemEventAnimator this$0;

    public StatusBarSystemEventAnimator$onSystemEventAnimationFinish$1(StatusBarSystemEventAnimator statusBarSystemEventAnimator) {
        this.this$0 = statusBarSystemEventAnimator;
    }

    public final void onAnimationUpdate(@NotNull ValueAnimator valueAnimator) {
        View animatedView = this.this$0.getAnimatedView();
        float access$getTranslationXOut$p = (float) this.this$0.translationXOut;
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            animatedView.setTranslationX(access$getTranslationXOut$p * ((Float) animatedValue).floatValue());
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Float");
    }
}
