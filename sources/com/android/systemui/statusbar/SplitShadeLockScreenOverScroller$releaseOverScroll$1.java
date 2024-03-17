package com.android.systemui.statusbar;

import android.animation.ValueAnimator;

/* compiled from: SplitShadeLockScreenOverScroller.kt */
public final class SplitShadeLockScreenOverScroller$releaseOverScroll$1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ SplitShadeLockScreenOverScroller this$0;

    public SplitShadeLockScreenOverScroller$releaseOverScroll$1(SplitShadeLockScreenOverScroller splitShadeLockScreenOverScroller) {
        this.this$0 = splitShadeLockScreenOverScroller;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            int intValue = ((Integer) animatedValue).intValue();
            this.this$0.qS.setOverScrollAmount(intValue);
            this.this$0.scrimController.setNotificationsOverScrollAmount(intValue);
            this.this$0.nsslController.setOverScrollAmount(intValue);
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
    }
}
