package com.android.systemui.biometrics;

import android.animation.ValueAnimator;
import com.android.internal.graphics.ColorUtils;
import com.android.systemui.statusbar.charging.DwellRippleShader;

/* compiled from: AuthRippleView.kt */
public final class AuthRippleView$retractDwellRipple$retractAlphaAnimator$1$1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ AuthRippleView this$0;

    public AuthRippleView$retractDwellRipple$retractAlphaAnimator$1$1(AuthRippleView authRippleView) {
        this.this$0 = authRippleView;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        DwellRippleShader access$getDwellShader$p = this.this$0.dwellShader;
        int color = this.this$0.dwellShader.getColor();
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            access$getDwellShader$p.setColor(ColorUtils.setAlphaComponent(color, ((Integer) animatedValue).intValue()));
            this.this$0.invalidate();
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
    }
}
