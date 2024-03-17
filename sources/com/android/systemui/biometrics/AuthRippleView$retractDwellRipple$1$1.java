package com.android.systemui.biometrics;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import org.jetbrains.annotations.Nullable;

/* compiled from: AuthRippleView.kt */
public final class AuthRippleView$retractDwellRipple$1$1 extends AnimatorListenerAdapter {
    public final /* synthetic */ AuthRippleView this$0;

    public AuthRippleView$retractDwellRipple$1$1(AuthRippleView authRippleView) {
        this.this$0 = authRippleView;
    }

    public void onAnimationStart(@Nullable Animator animator) {
        Animator access$getDwellPulseOutAnimator$p = this.this$0.dwellPulseOutAnimator;
        if (access$getDwellPulseOutAnimator$p != null) {
            access$getDwellPulseOutAnimator$p.cancel();
        }
        this.this$0.drawDwell = true;
    }

    public void onAnimationEnd(@Nullable Animator animator) {
        this.this$0.drawDwell = false;
        this.this$0.resetDwellAlpha();
    }
}
