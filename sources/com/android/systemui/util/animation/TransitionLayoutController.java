package com.android.systemui.util.animation;

import android.animation.ValueAnimator;
import android.util.MathUtils;
import com.android.systemui.animation.Interpolators;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: TransitionLayoutController.kt */
public class TransitionLayoutController {
    @Nullable
    public TransitionViewState animationStartState;
    @NotNull
    public ValueAnimator animator;
    public int currentHeight;
    @NotNull
    public TransitionViewState currentState = new TransitionViewState();
    public int currentWidth;
    @Nullable
    public Function2<? super Integer, ? super Integer, Unit> sizeChangedListener;
    @NotNull
    public TransitionViewState state = new TransitionViewState();
    @Nullable
    public TransitionLayout transitionLayout;

    public TransitionLayoutController() {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        this.animator = ofFloat;
        ofFloat.addUpdateListener(new TransitionLayoutController$1$1(this));
        ofFloat.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
    }

    public final void setSizeChangedListener(@Nullable Function2<? super Integer, ? super Integer, Unit> function2) {
        this.sizeChangedListener = function2;
    }

    public final void updateStateFromAnimation() {
        if (this.animationStartState != null && this.animator.isRunning()) {
            TransitionViewState transitionViewState = this.animationStartState;
            Intrinsics.checkNotNull(transitionViewState);
            TransitionViewState interpolatedState = getInterpolatedState(transitionViewState, this.state, this.animator.getAnimatedFraction(), this.currentState);
            this.currentState = interpolatedState;
            applyStateToLayout(interpolatedState);
        }
    }

    public final void applyStateToLayout(TransitionViewState transitionViewState) {
        TransitionLayout transitionLayout2 = this.transitionLayout;
        if (transitionLayout2 != null) {
            transitionLayout2.setState(transitionViewState);
        }
        if (this.currentHeight != transitionViewState.getHeight() || this.currentWidth != transitionViewState.getWidth()) {
            this.currentHeight = transitionViewState.getHeight();
            int width = transitionViewState.getWidth();
            this.currentWidth = width;
            Function2<? super Integer, ? super Integer, Unit> function2 = this.sizeChangedListener;
            if (function2 != null) {
                function2.invoke(Integer.valueOf(width), Integer.valueOf(this.currentHeight));
            }
        }
    }

    @NotNull
    public final TransitionViewState getGoneState(@NotNull TransitionViewState transitionViewState, @NotNull DisappearParameters disappearParameters, float f, @Nullable TransitionViewState transitionViewState2) {
        float constrain = MathUtils.constrain(MathUtils.map(disappearParameters.getDisappearStart(), disappearParameters.getDisappearEnd(), 0.0f, 1.0f, f), 0.0f, 1.0f);
        TransitionViewState copy = transitionViewState.copy(transitionViewState2);
        copy.setWidth((int) MathUtils.lerp((float) transitionViewState.getWidth(), ((float) transitionViewState.getWidth()) * disappearParameters.getDisappearSize().x, constrain));
        copy.setHeight((int) MathUtils.lerp((float) transitionViewState.getHeight(), ((float) transitionViewState.getHeight()) * disappearParameters.getDisappearSize().y, constrain));
        copy.getTranslation().x = ((float) (transitionViewState.getWidth() - copy.getWidth())) * disappearParameters.getGonePivot().x;
        copy.getTranslation().y = ((float) (transitionViewState.getHeight() - copy.getHeight())) * disappearParameters.getGonePivot().y;
        copy.getContentTranslation().x = (disappearParameters.getContentTranslationFraction().x - 1.0f) * copy.getTranslation().x;
        copy.getContentTranslation().y = (disappearParameters.getContentTranslationFraction().y - 1.0f) * copy.getTranslation().y;
        copy.setAlpha(MathUtils.constrain(MathUtils.map(disappearParameters.getFadeStartPosition(), 1.0f, 1.0f, 0.0f, constrain), 0.0f, 1.0f));
        return copy;
    }

    public static /* synthetic */ TransitionViewState getInterpolatedState$default(TransitionLayoutController transitionLayoutController, TransitionViewState transitionViewState, TransitionViewState transitionViewState2, float f, TransitionViewState transitionViewState3, int i, Object obj) {
        if (obj == null) {
            if ((i & 8) != 0) {
                transitionViewState3 = null;
            }
            return transitionLayoutController.getInterpolatedState(transitionViewState, transitionViewState2, f, transitionViewState3);
        }
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: getInterpolatedState");
    }

    @NotNull
    public final TransitionViewState getInterpolatedState(@NotNull TransitionViewState transitionViewState, @NotNull TransitionViewState transitionViewState2, float f, @Nullable TransitionViewState transitionViewState3) {
        TransitionLayoutController transitionLayoutController;
        TransitionViewState transitionViewState4;
        TransitionLayout transitionLayout2;
        WidgetState widgetState;
        int i;
        float f2;
        float f3;
        float f4;
        float f5;
        int i2;
        float f6;
        float f7 = f;
        if (transitionViewState3 == null) {
            transitionViewState4 = new TransitionViewState();
            transitionLayoutController = this;
        } else {
            transitionLayoutController = this;
            transitionViewState4 = transitionViewState3;
        }
        TransitionLayout transitionLayout3 = transitionLayoutController.transitionLayout;
        if (transitionLayout3 == null) {
            return transitionViewState4;
        }
        int childCount = transitionLayout3.getChildCount();
        int i3 = 0;
        while (i3 < childCount) {
            int i4 = i3 + 1;
            int id = transitionLayout3.getChildAt(i3).getId();
            WidgetState widgetState2 = transitionViewState4.getWidgetStates().get(Integer.valueOf(id));
            if (widgetState2 == null) {
                widgetState2 = new WidgetState(0.0f, 0.0f, 0, 0, 0, 0, 0.0f, 0.0f, false, 511, (DefaultConstructorMarker) null);
            }
            WidgetState widgetState3 = transitionViewState.getWidgetStates().get(Integer.valueOf(id));
            if (widgetState3 == null || (widgetState = transitionViewState2.getWidgetStates().get(Integer.valueOf(id))) == null) {
                transitionLayout2 = transitionLayout3;
            } else {
                if (widgetState3.getGone() != widgetState.getGone()) {
                    boolean z = true;
                    if (widgetState3.getGone()) {
                        f5 = MathUtils.map(0.8f, 1.0f, 0.0f, 1.0f, f7);
                        if (f7 >= 0.8f) {
                            z = false;
                        }
                        float scale = widgetState.getScale();
                        f3 = MathUtils.lerp(0.8f * scale, scale, f7);
                        i = widgetState.getMeasureWidth();
                        i2 = widgetState.getMeasureHeight();
                        f4 = MathUtils.lerp(widgetState3.getX() - (((float) i) / 2.0f), widgetState.getX(), f7);
                        transitionLayout2 = transitionLayout3;
                        f6 = MathUtils.lerp(widgetState3.getY() - (((float) i2) / 2.0f), widgetState.getY(), f7);
                        f2 = 1.0f;
                    } else {
                        transitionLayout2 = transitionLayout3;
                        f5 = MathUtils.map(0.0f, 0.19999999f, 0.0f, 1.0f, f7);
                        if (f7 <= 0.19999999f) {
                            z = false;
                        }
                        float scale2 = widgetState3.getScale();
                        f3 = MathUtils.lerp(scale2, 0.8f * scale2, f7);
                        int measureWidth = widgetState3.getMeasureWidth();
                        i2 = widgetState3.getMeasureHeight();
                        f4 = MathUtils.lerp(widgetState3.getX(), widgetState.getX() - (((float) measureWidth) / 2.0f), f7);
                        int i5 = measureWidth;
                        f6 = MathUtils.lerp(widgetState3.getY(), widgetState.getY() - (((float) i2) / 2.0f), f7);
                        i = i5;
                        f2 = 0.0f;
                    }
                    widgetState2.setGone(z);
                } else {
                    transitionLayout2 = transitionLayout3;
                    widgetState2.setGone(widgetState3.getGone());
                    i = widgetState.getMeasureWidth();
                    i2 = widgetState.getMeasureHeight();
                    f3 = MathUtils.lerp(widgetState3.getScale(), widgetState.getScale(), f7);
                    f4 = MathUtils.lerp(widgetState3.getX(), widgetState.getX(), f7);
                    f6 = MathUtils.lerp(widgetState3.getY(), widgetState.getY(), f7);
                    f5 = f7;
                    f2 = f5;
                }
                widgetState2.setX(f4);
                widgetState2.setY(f6);
                widgetState2.setAlpha(MathUtils.lerp(widgetState3.getAlpha(), widgetState.getAlpha(), f5));
                widgetState2.setWidth((int) MathUtils.lerp((float) widgetState3.getWidth(), (float) widgetState.getWidth(), f2));
                widgetState2.setHeight((int) MathUtils.lerp((float) widgetState3.getHeight(), (float) widgetState.getHeight(), f2));
                widgetState2.setScale(f3);
                widgetState2.setMeasureWidth(i);
                widgetState2.setMeasureHeight(i2);
                transitionViewState4.getWidgetStates().put(Integer.valueOf(id), widgetState2);
            }
            i3 = i4;
            transitionLayout3 = transitionLayout2;
        }
        transitionViewState4.setWidth((int) MathUtils.lerp((float) transitionViewState.getWidth(), (float) transitionViewState2.getWidth(), f7));
        transitionViewState4.setHeight((int) MathUtils.lerp((float) transitionViewState.getHeight(), (float) transitionViewState2.getHeight(), f7));
        transitionViewState4.getTranslation().x = MathUtils.lerp(transitionViewState.getTranslation().x, transitionViewState2.getTranslation().x, f7);
        transitionViewState4.getTranslation().y = MathUtils.lerp(transitionViewState.getTranslation().y, transitionViewState2.getTranslation().y, f7);
        transitionViewState4.setAlpha(MathUtils.lerp(transitionViewState.getAlpha(), transitionViewState2.getAlpha(), f7));
        transitionViewState4.getContentTranslation().x = MathUtils.lerp(transitionViewState.getContentTranslation().x, transitionViewState2.getContentTranslation().x, f7);
        transitionViewState4.getContentTranslation().y = MathUtils.lerp(transitionViewState.getContentTranslation().y, transitionViewState2.getContentTranslation().y, f7);
        return transitionViewState4;
    }

    public final void attach(@NotNull TransitionLayout transitionLayout2) {
        this.transitionLayout = transitionLayout2;
    }

    public final void setState(@NotNull TransitionViewState transitionViewState, boolean z, boolean z2, long j, long j2) {
        boolean z3 = z2 && this.currentState.getWidth() != 0;
        this.state = TransitionViewState.copy$default(transitionViewState, (TransitionViewState) null, 1, (Object) null);
        if (z || this.transitionLayout == null) {
            this.animator.cancel();
            applyStateToLayout(this.state);
            this.currentState = transitionViewState.copy(this.currentState);
        } else if (z3) {
            this.animationStartState = TransitionViewState.copy$default(this.currentState, (TransitionViewState) null, 1, (Object) null);
            this.animator.setDuration(j);
            this.animator.setStartDelay(j2);
            this.animator.start();
        } else if (!this.animator.isRunning()) {
            applyStateToLayout(this.state);
            this.currentState = transitionViewState.copy(this.currentState);
        }
    }

    public final void setMeasureState(@NotNull TransitionViewState transitionViewState) {
        TransitionLayout transitionLayout2 = this.transitionLayout;
        if (transitionLayout2 != null) {
            transitionLayout2.setMeasureState(transitionViewState);
        }
    }
}
