package com.android.wm.shell.back;

import com.android.wm.shell.back.BackAnimationController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BackAnimationController$BackAnimationImpl$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ BackAnimationController.BackAnimationImpl f$0;
    public final /* synthetic */ float f$1;
    public final /* synthetic */ float f$2;

    public /* synthetic */ BackAnimationController$BackAnimationImpl$$ExternalSyntheticLambda0(BackAnimationController.BackAnimationImpl backAnimationImpl, float f, float f2) {
        this.f$0 = backAnimationImpl;
        this.f$1 = f;
        this.f$2 = f2;
    }

    public final void run() {
        this.f$0.lambda$setSwipeThresholds$2(this.f$1, this.f$2);
    }
}
