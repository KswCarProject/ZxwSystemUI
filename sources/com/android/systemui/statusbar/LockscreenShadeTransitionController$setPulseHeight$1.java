package com.android.systemui.statusbar;

import android.animation.ValueAnimator;
import org.jetbrains.annotations.NotNull;

/* compiled from: LockscreenShadeTransitionController.kt */
public final class LockscreenShadeTransitionController$setPulseHeight$1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ LockscreenShadeTransitionController this$0;

    public LockscreenShadeTransitionController$setPulseHeight$1(LockscreenShadeTransitionController lockscreenShadeTransitionController) {
        this.this$0 = lockscreenShadeTransitionController;
    }

    public final void onAnimationUpdate(@NotNull ValueAnimator valueAnimator) {
        LockscreenShadeTransitionController lockscreenShadeTransitionController = this.this$0;
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            LockscreenShadeTransitionController.setPulseHeight$default(lockscreenShadeTransitionController, ((Float) animatedValue).floatValue(), false, 2, (Object) null);
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Float");
    }
}
