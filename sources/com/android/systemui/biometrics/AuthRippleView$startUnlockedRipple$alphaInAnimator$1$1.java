package com.android.systemui.biometrics;

import android.animation.ValueAnimator;
import com.android.internal.graphics.ColorUtils;
import com.android.systemui.statusbar.charging.RippleShader;

/* compiled from: AuthRippleView.kt */
public final class AuthRippleView$startUnlockedRipple$alphaInAnimator$1$1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ AuthRippleView this$0;

    public AuthRippleView$startUnlockedRipple$alphaInAnimator$1$1(AuthRippleView authRippleView) {
        this.this$0 = authRippleView;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        RippleShader access$getRippleShader$p = this.this$0.rippleShader;
        int color = this.this$0.rippleShader.getColor();
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            access$getRippleShader$p.setColor(ColorUtils.setAlphaComponent(color, ((Integer) animatedValue).intValue()));
            this.this$0.invalidate();
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
    }
}
