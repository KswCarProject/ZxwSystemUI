package com.android.wm.shell.bubbles;

import com.android.wm.shell.bubbles.BubbleController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BubbleController$6$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ BubbleController.AnonymousClass6 f$0;
    public final /* synthetic */ BubbleEntry f$1;
    public final /* synthetic */ Bubble f$2;

    public /* synthetic */ BubbleController$6$$ExternalSyntheticLambda1(BubbleController.AnonymousClass6 r1, BubbleEntry bubbleEntry, Bubble bubble) {
        this.f$0 = r1;
        this.f$1 = bubbleEntry;
        this.f$2 = bubble;
    }

    public final void run() {
        this.f$0.lambda$applyUpdate$0(this.f$1, this.f$2);
    }
}
