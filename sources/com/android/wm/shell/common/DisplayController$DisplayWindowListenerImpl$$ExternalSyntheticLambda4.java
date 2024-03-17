package com.android.wm.shell.common;

import com.android.wm.shell.common.DisplayController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DisplayController$DisplayWindowListenerImpl$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ DisplayController.DisplayWindowListenerImpl f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ DisplayController$DisplayWindowListenerImpl$$ExternalSyntheticLambda4(DisplayController.DisplayWindowListenerImpl displayWindowListenerImpl, int i) {
        this.f$0 = displayWindowListenerImpl;
        this.f$1 = i;
    }

    public final void run() {
        this.f$0.lambda$onFixedRotationFinished$4(this.f$1);
    }
}
