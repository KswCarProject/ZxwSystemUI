package com.android.wm.shell.bubbles;

import com.android.wm.shell.bubbles.Bubbles;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class Bubble$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ Bubble f$0;
    public final /* synthetic */ Bubbles.PendingIntentCanceledListener f$1;

    public /* synthetic */ Bubble$$ExternalSyntheticLambda1(Bubble bubble, Bubbles.PendingIntentCanceledListener pendingIntentCanceledListener) {
        this.f$0 = bubble;
        this.f$1 = pendingIntentCanceledListener;
    }

    public final void run() {
        this.f$0.lambda$new$0(this.f$1);
    }
}
