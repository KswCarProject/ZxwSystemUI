package com.android.wm.shell.bubbles;

import com.android.wm.shell.bubbles.BubbleController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BubbleController$BubblesImpl$$ExternalSyntheticLambda18 implements Runnable {
    public final /* synthetic */ BubbleController.BubblesImpl f$0;
    public final /* synthetic */ BubbleEntry f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ BubbleController$BubblesImpl$$ExternalSyntheticLambda18(BubbleController.BubblesImpl bubblesImpl, BubbleEntry bubbleEntry, boolean z) {
        this.f$0 = bubblesImpl;
        this.f$1 = bubbleEntry;
        this.f$2 = z;
    }

    public final void run() {
        this.f$0.lambda$onEntryUpdated$15(this.f$1, this.f$2);
    }
}
