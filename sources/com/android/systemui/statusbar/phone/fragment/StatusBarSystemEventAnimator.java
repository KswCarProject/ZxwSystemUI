package com.android.systemui.statusbar.phone.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.view.View;
import com.android.systemui.R$dimen;
import com.android.systemui.statusbar.events.SystemStatusAnimationCallback;
import com.android.systemui.statusbar.events.SystemStatusAnimationSchedulerKt;
import com.android.systemui.util.animation.AnimationUtil;
import org.jetbrains.annotations.NotNull;

/* compiled from: StatusBarSystemEventAnimator.kt */
public final class StatusBarSystemEventAnimator implements SystemStatusAnimationCallback {
    @NotNull
    public final View animatedView;
    public final int translationXIn;
    public final int translationXOut;

    public StatusBarSystemEventAnimator(@NotNull View view, @NotNull Resources resources) {
        this.animatedView = view;
        this.translationXIn = resources.getDimensionPixelSize(R$dimen.ongoing_appops_chip_animation_in_status_bar_translation_x);
        this.translationXOut = resources.getDimensionPixelSize(R$dimen.ongoing_appops_chip_animation_out_status_bar_translation_x);
    }

    @NotNull
    public final View getAnimatedView() {
        return this.animatedView;
    }

    @NotNull
    public Animator onSystemEventAnimationBegin() {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        AnimationUtil.Companion companion = AnimationUtil.Companion;
        ValueAnimator duration = ofFloat.setDuration(companion.getFrames(23));
        duration.setInterpolator(SystemStatusAnimationSchedulerKt.STATUS_BAR_X_MOVE_OUT);
        duration.addUpdateListener(new StatusBarSystemEventAnimator$onSystemEventAnimationBegin$1(this));
        ValueAnimator duration2 = ValueAnimator.ofFloat(new float[]{1.0f, 0.0f}).setDuration(companion.getFrames(8));
        duration2.setInterpolator((TimeInterpolator) null);
        duration2.addUpdateListener(new StatusBarSystemEventAnimator$onSystemEventAnimationBegin$2(this));
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(new Animator[]{duration, duration2});
        return animatorSet;
    }

    @NotNull
    public Animator onSystemEventAnimationFinish(boolean z) {
        this.animatedView.setTranslationX((float) this.translationXOut);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{1.0f, 0.0f});
        AnimationUtil.Companion companion = AnimationUtil.Companion;
        ValueAnimator duration = ofFloat.setDuration(companion.getFrames(28));
        duration.setStartDelay(companion.getFrames(2));
        duration.setInterpolator(SystemStatusAnimationSchedulerKt.STATUS_BAR_X_MOVE_IN);
        duration.addUpdateListener(new StatusBarSystemEventAnimator$onSystemEventAnimationFinish$1(this));
        ValueAnimator duration2 = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f}).setDuration(companion.getFrames(10));
        duration2.setStartDelay(companion.getFrames(4));
        duration2.setInterpolator((TimeInterpolator) null);
        duration2.addUpdateListener(new StatusBarSystemEventAnimator$onSystemEventAnimationFinish$2(this));
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(new Animator[]{duration, duration2});
        return animatorSet;
    }
}
