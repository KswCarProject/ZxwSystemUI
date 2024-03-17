package com.android.systemui.statusbar.phone;

import android.animation.ValueAnimator;
import com.android.systemui.statusbar.CircleReveal;
import com.android.systemui.statusbar.LightRevealScrim;

/* compiled from: UnlockedScreenOffAnimationController.kt */
public final class UnlockedScreenOffAnimationController$lightRevealAnimator$1$1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ UnlockedScreenOffAnimationController this$0;

    public UnlockedScreenOffAnimationController$lightRevealAnimator$1$1(UnlockedScreenOffAnimationController unlockedScreenOffAnimationController) {
        this.this$0 = unlockedScreenOffAnimationController;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        LightRevealScrim access$getLightRevealScrim$p = this.this$0.lightRevealScrim;
        LightRevealScrim lightRevealScrim = null;
        if (access$getLightRevealScrim$p == null) {
            access$getLightRevealScrim$p = null;
        }
        if (!(access$getLightRevealScrim$p.getRevealEffect() instanceof CircleReveal)) {
            LightRevealScrim access$getLightRevealScrim$p2 = this.this$0.lightRevealScrim;
            if (access$getLightRevealScrim$p2 == null) {
                access$getLightRevealScrim$p2 = null;
            }
            Object animatedValue = valueAnimator.getAnimatedValue();
            if (animatedValue != null) {
                access$getLightRevealScrim$p2.setRevealAmount(((Float) animatedValue).floatValue());
            } else {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.Float");
            }
        }
        LightRevealScrim access$getLightRevealScrim$p3 = this.this$0.lightRevealScrim;
        if (access$getLightRevealScrim$p3 != null) {
            lightRevealScrim = access$getLightRevealScrim$p3;
        }
        if (lightRevealScrim.isScrimAlmostOccludes() && this.this$0.interactionJankMonitor.isInstrumenting(40)) {
            this.this$0.interactionJankMonitor.end(40);
        }
    }
}
