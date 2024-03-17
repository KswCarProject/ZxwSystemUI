package com.android.systemui.biometrics;

import android.animation.ValueAnimator;
import com.android.systemui.statusbar.LightRevealScrim;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: AuthRippleController.kt */
public final class AuthRippleController$onKeyguardFadingAwayChanged$1$1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ LightRevealScrim $lightRevealScrim;
    public final /* synthetic */ ValueAnimator $this_apply;
    public final /* synthetic */ AuthRippleController this$0;

    public AuthRippleController$onKeyguardFadingAwayChanged$1$1(LightRevealScrim lightRevealScrim, AuthRippleController authRippleController, ValueAnimator valueAnimator) {
        this.$lightRevealScrim = lightRevealScrim;
        this.this$0 = authRippleController;
        this.$this_apply = valueAnimator;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        if (!Intrinsics.areEqual((Object) this.$lightRevealScrim.getRevealEffect(), (Object) this.this$0.circleReveal)) {
            this.$this_apply.cancel();
            return;
        }
        LightRevealScrim lightRevealScrim = this.$lightRevealScrim;
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            lightRevealScrim.setRevealAmount(((Float) animatedValue).floatValue());
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Float");
    }
}
