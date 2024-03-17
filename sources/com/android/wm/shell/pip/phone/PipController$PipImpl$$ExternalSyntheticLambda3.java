package com.android.wm.shell.pip.phone;

import com.android.wm.shell.pip.phone.PipController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class PipController$PipImpl$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ PipController.PipImpl f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ PipController$PipImpl$$ExternalSyntheticLambda3(PipController.PipImpl pipImpl, boolean z, boolean z2) {
        this.f$0 = pipImpl;
        this.f$1 = z;
        this.f$2 = z2;
    }

    public final void run() {
        this.f$0.lambda$onKeyguardVisibilityChanged$11(this.f$1, this.f$2);
    }
}
