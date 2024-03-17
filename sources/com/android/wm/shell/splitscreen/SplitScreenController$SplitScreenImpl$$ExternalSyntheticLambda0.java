package com.android.wm.shell.splitscreen;

import com.android.wm.shell.splitscreen.SplitScreenController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SplitScreenController$SplitScreenImpl$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ SplitScreenController.SplitScreenImpl f$0;
    public final /* synthetic */ boolean f$1;

    public /* synthetic */ SplitScreenController$SplitScreenImpl$$ExternalSyntheticLambda0(SplitScreenController.SplitScreenImpl splitScreenImpl, boolean z) {
        this.f$0 = splitScreenImpl;
        this.f$1 = z;
    }

    public final void run() {
        this.f$0.lambda$onKeyguardVisibilityChanged$3(this.f$1);
    }
}
