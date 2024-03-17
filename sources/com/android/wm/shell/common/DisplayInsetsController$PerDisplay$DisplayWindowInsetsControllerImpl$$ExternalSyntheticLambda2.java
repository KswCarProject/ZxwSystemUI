package com.android.wm.shell.common;

import android.view.InsetsVisibilities;
import com.android.wm.shell.common.DisplayInsetsController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DisplayInsetsController$PerDisplay$DisplayWindowInsetsControllerImpl$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ DisplayInsetsController.PerDisplay.DisplayWindowInsetsControllerImpl f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ InsetsVisibilities f$2;

    public /* synthetic */ DisplayInsetsController$PerDisplay$DisplayWindowInsetsControllerImpl$$ExternalSyntheticLambda2(DisplayInsetsController.PerDisplay.DisplayWindowInsetsControllerImpl displayWindowInsetsControllerImpl, String str, InsetsVisibilities insetsVisibilities) {
        this.f$0 = displayWindowInsetsControllerImpl;
        this.f$1 = str;
        this.f$2 = insetsVisibilities;
    }

    public final void run() {
        this.f$0.lambda$topFocusedWindowChanged$0(this.f$1, this.f$2);
    }
}
