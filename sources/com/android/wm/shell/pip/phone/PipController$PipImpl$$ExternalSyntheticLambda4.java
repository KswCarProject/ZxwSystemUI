package com.android.wm.shell.pip.phone;

import com.android.wm.shell.pip.phone.PipController;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class PipController$PipImpl$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ PipController.PipImpl f$0;
    public final /* synthetic */ Consumer f$1;

    public /* synthetic */ PipController$PipImpl$$ExternalSyntheticLambda4(PipController.PipImpl pipImpl, Consumer consumer) {
        this.f$0 = pipImpl;
        this.f$1 = consumer;
    }

    public final void run() {
        this.f$0.lambda$addPipExclusionBoundsChangeListener$8(this.f$1);
    }
}
