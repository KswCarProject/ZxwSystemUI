package com.android.systemui.statusbar.charging;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import org.jetbrains.annotations.Nullable;

/* compiled from: ChargingRippleView.kt */
public final class ChargingRippleView$startRipple$2 extends AnimatorListenerAdapter {
    public final /* synthetic */ Runnable $onAnimationEnd;
    public final /* synthetic */ ChargingRippleView this$0;

    public ChargingRippleView$startRipple$2(ChargingRippleView chargingRippleView, Runnable runnable) {
        this.this$0 = chargingRippleView;
        this.$onAnimationEnd = runnable;
    }

    public void onAnimationEnd(@Nullable Animator animator) {
        this.this$0.setRippleInProgress(false);
        Runnable runnable = this.$onAnimationEnd;
        if (runnable != null) {
            runnable.run();
        }
    }
}
