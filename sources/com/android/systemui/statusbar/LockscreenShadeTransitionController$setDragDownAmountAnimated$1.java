package com.android.systemui.statusbar;

import android.animation.ValueAnimator;
import org.jetbrains.annotations.NotNull;

/* compiled from: LockscreenShadeTransitionController.kt */
public final class LockscreenShadeTransitionController$setDragDownAmountAnimated$1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ LockscreenShadeTransitionController this$0;

    public LockscreenShadeTransitionController$setDragDownAmountAnimated$1(LockscreenShadeTransitionController lockscreenShadeTransitionController) {
        this.this$0 = lockscreenShadeTransitionController;
    }

    public final void onAnimationUpdate(@NotNull ValueAnimator valueAnimator) {
        LockscreenShadeTransitionController lockscreenShadeTransitionController = this.this$0;
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            lockscreenShadeTransitionController.setDragDownAmount$frameworks__base__packages__SystemUI__android_common__SystemUI_core(((Float) animatedValue).floatValue());
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Float");
    }
}
