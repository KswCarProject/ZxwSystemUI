package com.android.systemui.media;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.monet.ColorScheme;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ColorSchemeTransition.kt */
public class AnimatingColorTransition implements ValueAnimator.AnimatorUpdateListener {
    @NotNull
    public final Function1<Integer, Unit> applyColor;
    @NotNull
    public final ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    public int currentColor;
    public final int defaultColor;
    @NotNull
    public final Function1<ColorScheme, Integer> extractColor;
    public int sourceColor;
    public int targetColor;
    @NotNull
    public final ValueAnimator valueAnimator = buildAnimator();

    public AnimatingColorTransition(int i, @NotNull Function1<? super ColorScheme, Integer> function1, @NotNull Function1<? super Integer, Unit> function12) {
        this.defaultColor = i;
        this.extractColor = function1;
        this.applyColor = function12;
        this.sourceColor = i;
        this.currentColor = i;
        this.targetColor = i;
        function12.invoke(Integer.valueOf(i));
    }

    public final int getCurrentColor() {
        return this.currentColor;
    }

    public final int getTargetColor() {
        return this.targetColor;
    }

    public void onAnimationUpdate(@NotNull ValueAnimator valueAnimator2) {
        Object evaluate = this.argbEvaluator.evaluate(valueAnimator2.getAnimatedFraction(), Integer.valueOf(this.sourceColor), Integer.valueOf(this.targetColor));
        if (evaluate != null) {
            int intValue = ((Integer) evaluate).intValue();
            this.currentColor = intValue;
            this.applyColor.invoke(Integer.valueOf(intValue));
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
    }

    public void updateColorScheme(@Nullable ColorScheme colorScheme) {
        int intValue = colorScheme == null ? this.defaultColor : this.extractColor.invoke(colorScheme).intValue();
        if (intValue != this.targetColor) {
            this.sourceColor = this.currentColor;
            this.targetColor = intValue;
            this.valueAnimator.cancel();
            this.valueAnimator.start();
        }
    }

    @NotNull
    @VisibleForTesting
    public ValueAnimator buildAnimator() {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        ofFloat.setDuration(333);
        ofFloat.addUpdateListener(this);
        return ofFloat;
    }
}
