package com.android.wm.shell.pip.phone;

import com.android.wm.shell.pip.phone.PipController;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class PipController$PipImpl$$ExternalSyntheticLambda6 implements Runnable {
    public final /* synthetic */ PipController.PipImpl f$0;
    public final /* synthetic */ Consumer f$1;

    public /* synthetic */ PipController$PipImpl$$ExternalSyntheticLambda6(PipController.PipImpl pipImpl, Consumer consumer) {
        this.f$0 = pipImpl;
        this.f$1 = consumer;
    }

    public final void run() {
        this.f$0.lambda$removePipExclusionBoundsChangeListener$9(this.f$1);
    }
}
