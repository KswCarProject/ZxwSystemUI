package com.android.wm.shell.bubbles;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BubbleController$$ExternalSyntheticLambda8 implements Runnable {
    public final /* synthetic */ BubbleController f$0;
    public final /* synthetic */ BubbleEntry f$1;
    public final /* synthetic */ boolean f$2;
    public final /* synthetic */ Bubble f$3;

    public /* synthetic */ BubbleController$$ExternalSyntheticLambda8(BubbleController bubbleController, BubbleEntry bubbleEntry, boolean z, Bubble bubble) {
        this.f$0 = bubbleController;
        this.f$1 = bubbleEntry;
        this.f$2 = z;
        this.f$3 = bubble;
    }

    public final void run() {
        this.f$0.lambda$setIsBubble$12(this.f$1, this.f$2, this.f$3);
    }
}
