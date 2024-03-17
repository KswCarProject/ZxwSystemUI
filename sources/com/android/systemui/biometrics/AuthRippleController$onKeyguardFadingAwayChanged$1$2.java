package com.android.systemui.biometrics;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import com.android.systemui.statusbar.LiftReveal;
import com.android.systemui.statusbar.LightRevealScrim;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.Nullable;

/* compiled from: AuthRippleController.kt */
public final class AuthRippleController$onKeyguardFadingAwayChanged$1$2 extends AnimatorListenerAdapter {
    public final /* synthetic */ LightRevealScrim $lightRevealScrim;
    public final /* synthetic */ AuthRippleController this$0;

    public AuthRippleController$onKeyguardFadingAwayChanged$1$2(LightRevealScrim lightRevealScrim, AuthRippleController authRippleController) {
        this.$lightRevealScrim = lightRevealScrim;
        this.this$0 = authRippleController;
    }

    public void onAnimationEnd(@Nullable Animator animator) {
        if (Intrinsics.areEqual((Object) this.$lightRevealScrim.getRevealEffect(), (Object) this.this$0.circleReveal)) {
            this.$lightRevealScrim.setRevealEffect(LiftReveal.INSTANCE);
        }
        this.this$0.setLightRevealScrimAnimator((ValueAnimator) null);
    }
}
