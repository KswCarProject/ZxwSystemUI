package com.android.systemui.statusbar.phone.shade.transition;

import android.animation.ValueAnimator;

/* compiled from: SplitShadeOverScroller.kt */
public final class SplitShadeOverScroller$releaseOverScroll$1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ SplitShadeOverScroller this$0;

    public SplitShadeOverScroller$releaseOverScroll$1(SplitShadeOverScroller splitShadeOverScroller) {
        this.this$0 = splitShadeOverScroller;
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
