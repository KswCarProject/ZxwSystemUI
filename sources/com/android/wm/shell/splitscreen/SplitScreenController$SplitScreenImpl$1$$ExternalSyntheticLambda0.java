package com.android.wm.shell.splitscreen;

import com.android.wm.shell.splitscreen.SplitScreenController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SplitScreenController$SplitScreenImpl$1$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ SplitScreenController.SplitScreenImpl.AnonymousClass1 f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ SplitScreenController$SplitScreenImpl$1$$ExternalSyntheticLambda0(SplitScreenController.SplitScreenImpl.AnonymousClass1 r1, int i, boolean z) {
        this.f$0 = r1;
        this.f$1 = i;
        this.f$2 = z;
    }

    public final void run() {
        this.f$0.lambda$onSplitVisibilityChanged$2(this.f$1, this.f$2);
    }
}
