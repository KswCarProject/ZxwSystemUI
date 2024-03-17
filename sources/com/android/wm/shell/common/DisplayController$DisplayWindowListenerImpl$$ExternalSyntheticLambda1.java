package com.android.wm.shell.common;

import com.android.wm.shell.common.DisplayController;
import java.util.List;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DisplayController$DisplayWindowListenerImpl$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ DisplayController.DisplayWindowListenerImpl f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ List f$2;
    public final /* synthetic */ List f$3;

    public /* synthetic */ DisplayController$DisplayWindowListenerImpl$$ExternalSyntheticLambda1(DisplayController.DisplayWindowListenerImpl displayWindowListenerImpl, int i, List list, List list2) {
        this.f$0 = displayWindowListenerImpl;
        this.f$1 = i;
        this.f$2 = list;
        this.f$3 = list2;
    }

    public final void run() {
        this.f$0.lambda$onKeepClearAreasChanged$5(this.f$1, this.f$2, this.f$3);
    }
}
