package com.android.systemui.biometrics;

import android.animation.ValueAnimator;
import com.android.systemui.statusbar.charging.DwellRippleShader;

/* compiled from: AuthRippleView.kt */
public final class AuthRippleView$retractDwellRipple$retractDwellRippleAnimator$1$1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ AuthRippleView this$0;

    public AuthRippleView$retractDwellRipple$retractDwellRippleAnimator$1$1(AuthRippleView authRippleView) {
        this.this$0 = authRippleView;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        long currentPlayTime = valueAnimator.getCurrentPlayTime();
        DwellRippleShader access$getDwellShader$p = this.this$0.dwellShader;
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            access$getDwellShader$p.setProgress(((Float) animatedValue).floatValue());
            this.this$0.dwellShader.setTime((float) currentPlayTime);
            this.this$0.invalidate();
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Float");
    }
}
