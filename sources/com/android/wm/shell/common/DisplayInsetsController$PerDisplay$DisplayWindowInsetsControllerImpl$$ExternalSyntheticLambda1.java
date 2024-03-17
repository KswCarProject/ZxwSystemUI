package com.android.wm.shell.common;

import android.view.InsetsState;
import com.android.wm.shell.common.DisplayInsetsController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DisplayInsetsController$PerDisplay$DisplayWindowInsetsControllerImpl$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ DisplayInsetsController.PerDisplay.DisplayWindowInsetsControllerImpl f$0;
    public final /* synthetic */ InsetsState f$1;

    public /* synthetic */ DisplayInsetsController$PerDisplay$DisplayWindowInsetsControllerImpl$$ExternalSyntheticLambda1(DisplayInsetsController.PerDisplay.DisplayWindowInsetsControllerImpl displayWindowInsetsControllerImpl, InsetsState insetsState) {
        this.f$0 = displayWindowInsetsControllerImpl;
        this.f$1 = insetsState;
    }

    public final void run() {
        this.f$0.lambda$insetsChanged$1(this.f$1);
    }
}
