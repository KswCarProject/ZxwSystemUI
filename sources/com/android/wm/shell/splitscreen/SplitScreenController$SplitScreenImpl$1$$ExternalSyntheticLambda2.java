package com.android.wm.shell.splitscreen;

import com.android.wm.shell.splitscreen.SplitScreenController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SplitScreenController$SplitScreenImpl$1$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ SplitScreenController.SplitScreenImpl.AnonymousClass1 f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ int f$3;
    public final /* synthetic */ boolean f$4;

    public /* synthetic */ SplitScreenController$SplitScreenImpl$1$$ExternalSyntheticLambda2(SplitScreenController.SplitScreenImpl.AnonymousClass1 r1, int i, int i2, int i3, boolean z) {
        this.f$0 = r1;
        this.f$1 = i;
        this.f$2 = i2;
        this.f$3 = i3;
        this.f$4 = z;
    }

    public final void run() {
        this.f$0.lambda$onTaskStageChanged$1(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
