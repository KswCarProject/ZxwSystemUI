package com.android.wm.shell.pip.phone;

import android.view.InputChannel;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class PipInputConsumer$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ PipInputConsumer f$0;
    public final /* synthetic */ InputChannel f$1;

    public /* synthetic */ PipInputConsumer$$ExternalSyntheticLambda0(PipInputConsumer pipInputConsumer, InputChannel inputChannel) {
        this.f$0 = pipInputConsumer;
        this.f$1 = inputChannel;
    }

    public final void run() {
        this.f$0.lambda$registerInputConsumer$1(this.f$1);
    }
}
