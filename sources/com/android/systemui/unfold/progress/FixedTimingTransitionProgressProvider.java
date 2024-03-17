package com.android.systemui.unfold.progress;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.util.FloatProperty;
import com.android.systemui.unfold.UnfoldTransitionProgressProvider;
import com.android.systemui.unfold.updates.FoldStateProvider;
import java.util.ArrayList;
import java.util.List;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;

/* compiled from: FixedTimingTransitionProgressProvider.kt */
public final class FixedTimingTransitionProgressProvider implements UnfoldTransitionProgressProvider, FoldStateProvider.FoldUpdatesListener {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public final ObjectAnimator animator;
    @NotNull
    public final AnimatorListener animatorListener;
    @NotNull
    public final FoldStateProvider foldStateProvider;
    @NotNull
    public final List<UnfoldTransitionProgressProvider.TransitionProgressListener> listeners = new ArrayList();
    public float transitionProgress;

    public void onHingeAngleUpdate(float f) {
    }

    public FixedTimingTransitionProgressProvider(@NotNull FoldStateProvider foldStateProvider2) {
        this.foldStateProvider = foldStateProvider2;
        AnimatorListener animatorListener2 = new AnimatorListener();
        this.animatorListener = animatorListener2;
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, AnimationProgressProperty.INSTANCE, new float[]{0.0f, 1.0f});
        ofFloat.setDuration(400);
        ofFloat.addListener(animatorListener2);
        this.animator = ofFloat;
        foldStateProvider2.addCallback(this);
        foldStateProvider2.start();
    }

    public final void setTransitionProgress(float f) {
        for (UnfoldTransitionProgressProvider.TransitionProgressListener onTransitionProgress : this.listeners) {
            onTransitionProgress.onTransitionProgress(f);
        }
        this.transitionProgress = f;
    }

    public void onFoldUpdate(int i) {
        if (i == 2) {
            this.animator.start();
        } else if (i == 5) {
            this.animator.cancel();
        }
    }

    public void addCallback(@NotNull UnfoldTransitionProgressProvider.TransitionProgressListener transitionProgressListener) {
        this.listeners.add(transitionProgressListener);
    }

    public void removeCallback(@NotNull UnfoldTransitionProgressProvider.TransitionProgressListener transitionProgressListener) {
        this.listeners.remove(transitionProgressListener);
    }

    /* compiled from: FixedTimingTransitionProgressProvider.kt */
    public static final class AnimationProgressProperty extends FloatProperty<FixedTimingTransitionProgressProvider> {
        @NotNull
        public static final AnimationProgressProperty INSTANCE = new AnimationProgressProperty();

        public AnimationProgressProperty() {
            super("animation_progress");
        }

        public void setValue(@NotNull FixedTimingTransitionProgressProvider fixedTimingTransitionProgressProvider, float f) {
            fixedTimingTransitionProgressProvider.setTransitionProgress(f);
        }

        @NotNull
        public Float get(@NotNull FixedTimingTransitionProgressProvider fixedTimingTransitionProgressProvider) {
            return Float.valueOf(fixedTimingTransitionProgressProvider.transitionProgress);
        }
    }

    /* compiled from: FixedTimingTransitionProgressProvider.kt */
    public final class AnimatorListener implements Animator.AnimatorListener {
        public void onAnimationCancel(@NotNull Animator animator) {
        }

        public void onAnimationRepeat(@NotNull Animator animator) {
        }

        public AnimatorListener() {
        }

        public void onAnimationStart(@NotNull Animator animator) {
            for (UnfoldTransitionProgressProvider.TransitionProgressListener onTransitionStarted : FixedTimingTransitionProgressProvider.this.listeners) {
                onTransitionStarted.onTransitionStarted();
            }
        }

        public void onAnimationEnd(@NotNull Animator animator) {
            for (UnfoldTransitionProgressProvider.TransitionProgressListener onTransitionFinished : FixedTimingTransitionProgressProvider.this.listeners) {
                onTransitionFinished.onTransitionFinished();
            }
        }
    }

    /* compiled from: FixedTimingTransitionProgressProvider.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}
