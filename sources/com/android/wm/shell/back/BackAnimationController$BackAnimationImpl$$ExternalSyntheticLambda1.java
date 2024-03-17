package com.android.wm.shell.back;

import com.android.wm.shell.back.BackAnimationController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BackAnimationController$BackAnimationImpl$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ BackAnimationController.BackAnimationImpl f$0;
    public final /* synthetic */ float f$1;
    public final /* synthetic */ float f$2;
    public final /* synthetic */ int f$3;
    public final /* synthetic */ int f$4;

    public /* synthetic */ BackAnimationController$BackAnimationImpl$$ExternalSyntheticLambda1(BackAnimationController.BackAnimationImpl backAnimationImpl, float f, float f2, int i, int i2) {
        this.f$0 = backAnimationImpl;
        this.f$1 = f;
        this.f$2 = f2;
        this.f$3 = i;
        this.f$4 = i2;
    }

    public final void run() {
        this.f$0.lambda$onBackMotion$0(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
