package com.android.systemui.statusbar.phone.fragment;

import android.animation.ValueAnimator;
import android.view.View;
import org.jetbrains.annotations.NotNull;

/* compiled from: StatusBarSystemEventAnimator.kt */
public final class StatusBarSystemEventAnimator$onSystemEventAnimationBegin$1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ StatusBarSystemEventAnimator this$0;

    public StatusBarSystemEventAnimator$onSystemEventAnimationBegin$1(StatusBarSystemEventAnimator statusBarSystemEventAnimator) {
        this.this$0 = statusBarSystemEventAnimator;
    }

    public final void onAnimationUpdate(@NotNull ValueAnimator valueAnimator) {
        View animatedView = this.this$0.getAnimatedView();
        float access$getTranslationXIn$p = (float) this.this$0.translationXIn;
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            animatedView.setTranslationX(-(access$getTranslationXIn$p * ((Float) animatedValue).floatValue()));
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Float");
    }
}
