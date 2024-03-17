package com.android.systemui.controls;

import android.view.animation.AccelerateInterpolator;

/* compiled from: TooltipManager.kt */
public final class TooltipManager$hide$1 implements Runnable {
    public final /* synthetic */ boolean $animate;
    public final /* synthetic */ TooltipManager this$0;

    public TooltipManager$hide$1(boolean z, TooltipManager tooltipManager) {
        this.$animate = z;
        this.this$0 = tooltipManager;
    }

    public final void run() {
        if (this.$animate) {
            this.this$0.getLayout().animate().alpha(0.0f).withLayer().setStartDelay(0).setDuration(100).setInterpolator(new AccelerateInterpolator()).start();
            return;
        }
        this.this$0.getLayout().animate().cancel();
        this.this$0.getLayout().setAlpha(0.0f);
    }
}
