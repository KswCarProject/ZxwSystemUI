package com.android.systemui.controls.management;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Interpolator;
import androidx.lifecycle.LifecycleObserver;
import com.android.systemui.animation.Interpolators;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ControlsAnimations.kt */
public final class ControlsAnimations {
    @NotNull
    public static final ControlsAnimations INSTANCE = new ControlsAnimations();
    public static float translationY = -1.0f;

    @NotNull
    public final LifecycleObserver observerForAnimations(@NotNull ViewGroup viewGroup, @NotNull Window window, @NotNull Intent intent) {
        return new ControlsAnimations$observerForAnimations$1(intent, viewGroup, window);
    }

    @NotNull
    public final Animator enterAnimation(@NotNull View view) {
        Log.d("ControlsUiController", Intrinsics.stringPlus("Enter animation for ", view));
        view.setTransitionAlpha(0.0f);
        view.setAlpha(1.0f);
        view.setTranslationY(translationY);
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, "transitionAlpha", new float[]{0.0f, 1.0f});
        Interpolator interpolator = Interpolators.DECELERATE_QUINT;
        ofFloat.setInterpolator(interpolator);
        ofFloat.setStartDelay(183);
        ofFloat.setDuration(167);
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(view, "translationY", new float[]{0.0f});
        ofFloat2.setInterpolator(interpolator);
        ofFloat2.setStartDelay(217);
        ofFloat2.setDuration(217);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(new Animator[]{ofFloat, ofFloat2});
        return animatorSet;
    }

    public static /* synthetic */ Animator exitAnimation$default(View view, Runnable runnable, int i, Object obj) {
        if ((i & 2) != 0) {
            runnable = null;
        }
        return exitAnimation(view, runnable);
    }

    @NotNull
    public static final Animator exitAnimation(@NotNull View view, @Nullable Runnable runnable) {
        Log.d("ControlsUiController", Intrinsics.stringPlus("Exit animation for ", view));
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, "transitionAlpha", new float[]{0.0f});
        Interpolator interpolator = Interpolators.ACCELERATE;
        ofFloat.setInterpolator(interpolator);
        ofFloat.setDuration(183);
        view.setTranslationY(0.0f);
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(view, "translationY", new float[]{-translationY});
        ofFloat2.setInterpolator(interpolator);
        ofFloat2.setDuration(183);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(new Animator[]{ofFloat, ofFloat2});
        if (runnable != null) {
            animatorSet.addListener(new ControlsAnimations$exitAnimation$1$1$1(runnable));
        }
        return animatorSet;
    }

    @NotNull
    public final WindowTransition enterWindowTransition(int i) {
        WindowTransition windowTransition = new WindowTransition(ControlsAnimations$enterWindowTransition$1.INSTANCE);
        windowTransition.addTarget(i);
        return windowTransition;
    }

    @NotNull
    public final WindowTransition exitWindowTransition(int i) {
        WindowTransition windowTransition = new WindowTransition(ControlsAnimations$exitWindowTransition$1.INSTANCE);
        windowTransition.addTarget(i);
        return windowTransition;
    }
}
