package com.android.wm.shell.common;

import com.android.wm.shell.common.DisplayController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DisplayController$DisplayWindowListenerImpl$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ DisplayController.DisplayWindowListenerImpl f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ DisplayController$DisplayWindowListenerImpl$$ExternalSyntheticLambda0(DisplayController.DisplayWindowListenerImpl displayWindowListenerImpl, int i, int i2) {
        this.f$0 = displayWindowListenerImpl;
        this.f$1 = i;
        this.f$2 = i2;
    }

    public final void run() {
        this.f$0.lambda$onFixedRotationStarted$3(this.f$1, this.f$2);
    }
}
