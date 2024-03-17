package com.android.systemui.unfold.progress;

import android.util.Log;
import android.util.MathUtils;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import com.android.systemui.unfold.UnfoldTransitionProgressProvider;
import com.android.systemui.unfold.updates.FoldStateProvider;
import java.util.ArrayList;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: PhysicsBasedUnfoldTransitionProgressProvider.kt */
public final class PhysicsBasedUnfoldTransitionProgressProvider implements UnfoldTransitionProgressProvider, FoldStateProvider.FoldUpdatesListener, DynamicAnimation.OnAnimationEndListener {
    @NotNull
    public final FoldStateProvider foldStateProvider;
    public boolean isAnimatedCancelRunning;
    public boolean isTransitionRunning;
    @NotNull
    public final List<UnfoldTransitionProgressProvider.TransitionProgressListener> listeners = new ArrayList();
    @NotNull
    public final SpringAnimation springAnimation;
    public float transitionProgress;

    public PhysicsBasedUnfoldTransitionProgressProvider(@NotNull FoldStateProvider foldStateProvider2) {
        this.foldStateProvider = foldStateProvider2;
        SpringAnimation springAnimation2 = new SpringAnimation(this, AnimationProgressProperty.INSTANCE);
        springAnimation2.addEndListener(this);
        this.springAnimation = springAnimation2;
        foldStateProvider2.addCallback(this);
        foldStateProvider2.start();
    }

    public final void setTransitionProgress(float f) {
        if (this.isTransitionRunning) {
            for (UnfoldTransitionProgressProvider.TransitionProgressListener onTransitionProgress : this.listeners) {
                onTransitionProgress.onTransitionProgress(f);
            }
        }
        this.transitionProgress = f;
    }

    public void onHingeAngleUpdate(float f) {
        if (this.isTransitionRunning && !this.isAnimatedCancelRunning) {
            this.springAnimation.animateToFinalPosition(MathUtils.saturate(f / 165.0f));
        }
    }

    public void onFoldUpdate(int i) {
        if (i != 1) {
            if (i == 2) {
                startTransition(0.0f);
                if (this.foldStateProvider.isFinishedOpening()) {
                    cancelTransition(1.0f, true);
                }
            } else if (i == 3 || i == 4) {
                if (this.isTransitionRunning) {
                    cancelTransition(1.0f, true);
                }
            } else if (i == 5) {
                cancelTransition(0.0f, false);
            }
        } else if (!this.isTransitionRunning) {
            startTransition(1.0f);
        } else if (this.isAnimatedCancelRunning) {
            this.isAnimatedCancelRunning = false;
        }
        Log.d("PhysicsBasedUnfoldTransitionProgressProvider", Intrinsics.stringPlus("onFoldUpdate = ", Integer.valueOf(i)));
    }

    public final void cancelTransition(float f, boolean z) {
        if (!this.isTransitionRunning || !z) {
            setTransitionProgress(f);
            this.isAnimatedCancelRunning = false;
            this.isTransitionRunning = false;
            this.springAnimation.cancel();
            for (UnfoldTransitionProgressProvider.TransitionProgressListener onTransitionFinished : this.listeners) {
                onTransitionFinished.onTransitionFinished();
            }
            Log.d("PhysicsBasedUnfoldTransitionProgressProvider", "onTransitionFinished");
            return;
        }
        this.isAnimatedCancelRunning = true;
        this.springAnimation.animateToFinalPosition(f);
    }

    public void onAnimationEnd(@NotNull DynamicAnimation<? extends DynamicAnimation<?>> dynamicAnimation, boolean z, float f, float f2) {
        if (this.isAnimatedCancelRunning) {
            cancelTransition(f, false);
        }
    }

    public final void onStartTransition() {
        for (UnfoldTransitionProgressProvider.TransitionProgressListener onTransitionStarted : this.listeners) {
            onTransitionStarted.onTransitionStarted();
        }
        this.isTransitionRunning = true;
        Log.d("PhysicsBasedUnfoldTransitionProgressProvider", "onTransitionStarted");
    }

    public final void startTransition(float f) {
        if (!this.isTransitionRunning) {
            onStartTransition();
        }
        SpringAnimation springAnimation2 = this.springAnimation;
        SpringForce springForce = new SpringForce();
        springForce.setFinalPosition(f);
        springForce.setDampingRatio(1.0f);
        springForce.setStiffness(200.0f);
        springAnimation2.setSpring(springForce);
        springAnimation2.setMinimumVisibleChange(0.001f);
        springAnimation2.setStartValue(f);
        springAnimation2.setMinValue(0.0f);
        springAnimation2.setMaxValue(1.0f);
        this.springAnimation.start();
    }

    public void addCallback(@NotNull UnfoldTransitionProgressProvider.TransitionProgressListener transitionProgressListener) {
        this.listeners.add(transitionProgressListener);
    }

    public void removeCallback(@NotNull UnfoldTransitionProgressProvider.TransitionProgressListener transitionProgressListener) {
        this.listeners.remove(transitionProgressListener);
    }

    /* compiled from: PhysicsBasedUnfoldTransitionProgressProvider.kt */
    public static final class AnimationProgressProperty extends FloatPropertyCompat<PhysicsBasedUnfoldTransitionProgressProvider> {
        @NotNull
        public static final AnimationProgressProperty INSTANCE = new AnimationProgressProperty();

        public AnimationProgressProperty() {
            super("animation_progress");
        }

        public void setValue(@NotNull PhysicsBasedUnfoldTransitionProgressProvider physicsBasedUnfoldTransitionProgressProvider, float f) {
            physicsBasedUnfoldTransitionProgressProvider.setTransitionProgress(f);
        }

        public float getValue(@NotNull PhysicsBasedUnfoldTransitionProgressProvider physicsBasedUnfoldTransitionProgressProvider) {
            return physicsBasedUnfoldTransitionProgressProvider.transitionProgress;
        }
    }
}
