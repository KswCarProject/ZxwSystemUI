package com.android.wm.shell.common;

import android.view.InsetsSourceControl;
import android.view.InsetsState;
import com.android.wm.shell.common.DisplayInsetsController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DisplayInsetsController$PerDisplay$DisplayWindowInsetsControllerImpl$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ DisplayInsetsController.PerDisplay.DisplayWindowInsetsControllerImpl f$0;
    public final /* synthetic */ InsetsState f$1;
    public final /* synthetic */ InsetsSourceControl[] f$2;

    public /* synthetic */ DisplayInsetsController$PerDisplay$DisplayWindowInsetsControllerImpl$$ExternalSyntheticLambda3(DisplayInsetsController.PerDisplay.DisplayWindowInsetsControllerImpl displayWindowInsetsControllerImpl, InsetsState insetsState, InsetsSourceControl[] insetsSourceControlArr) {
        this.f$0 = displayWindowInsetsControllerImpl;
        this.f$1 = insetsState;
        this.f$2 = insetsSourceControlArr;
    }

    public final void run() {
        this.f$0.lambda$insetsControlChanged$2(this.f$1, this.f$2);
    }
}
