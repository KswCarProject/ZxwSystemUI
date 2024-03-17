package com.android.keyguard;

import com.android.keyguard.AnimatableClockView;

/* compiled from: AnimatableClockView.kt */
public final class AnimatableClockView$animateCharge$startAnimPhase2$1 implements Runnable {
    public final /* synthetic */ AnimatableClockView.DozeStateGetter $dozeStateGetter;
    public final /* synthetic */ AnimatableClockView this$0;

    public AnimatableClockView$animateCharge$startAnimPhase2$1(AnimatableClockView animatableClockView, AnimatableClockView.DozeStateGetter dozeStateGetter) {
        this.this$0 = animatableClockView;
        this.$dozeStateGetter = dozeStateGetter;
    }

    public final void run() {
        AnimatableClockView animatableClockView = this.this$0;
        boolean isDozing = this.$dozeStateGetter.isDozing();
        AnimatableClockView animatableClockView2 = this.this$0;
        animatableClockView.setTextStyle(isDozing ? animatableClockView2.getDozingWeight() : animatableClockView2.getLockScreenWeight(), -1.0f, (Integer) null, true, 1000, 0, (Runnable) null);
    }
}
