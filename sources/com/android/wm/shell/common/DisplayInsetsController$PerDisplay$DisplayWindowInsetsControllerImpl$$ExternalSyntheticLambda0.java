package com.android.wm.shell.common;

import com.android.wm.shell.common.DisplayInsetsController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DisplayInsetsController$PerDisplay$DisplayWindowInsetsControllerImpl$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ DisplayInsetsController.PerDisplay.DisplayWindowInsetsControllerImpl f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ DisplayInsetsController$PerDisplay$DisplayWindowInsetsControllerImpl$$ExternalSyntheticLambda0(DisplayInsetsController.PerDisplay.DisplayWindowInsetsControllerImpl displayWindowInsetsControllerImpl, int i, boolean z) {
        this.f$0 = displayWindowInsetsControllerImpl;
        this.f$1 = i;
        this.f$2 = z;
    }

    public final void run() {
        this.f$0.lambda$showInsets$3(this.f$1, this.f$2);
    }
}
