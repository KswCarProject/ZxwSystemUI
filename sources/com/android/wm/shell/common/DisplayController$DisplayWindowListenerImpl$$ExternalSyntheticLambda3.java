package com.android.wm.shell.common;

import com.android.wm.shell.common.DisplayController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DisplayController$DisplayWindowListenerImpl$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ DisplayController.DisplayWindowListenerImpl f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ DisplayController$DisplayWindowListenerImpl$$ExternalSyntheticLambda3(DisplayController.DisplayWindowListenerImpl displayWindowListenerImpl, int i) {
        this.f$0 = displayWindowListenerImpl;
        this.f$1 = i;
    }

    public final void run() {
        this.f$0.lambda$onDisplayRemoved$2(this.f$1);
    }
}
