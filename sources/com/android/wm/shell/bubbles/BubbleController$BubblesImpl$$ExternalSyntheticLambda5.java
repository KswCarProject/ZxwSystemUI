package com.android.wm.shell.bubbles;

import com.android.wm.shell.bubbles.BubbleController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BubbleController$BubblesImpl$$ExternalSyntheticLambda5 implements Runnable {
    public final /* synthetic */ BubbleController.BubblesImpl f$0;
    public final /* synthetic */ BubbleEntry f$1;

    public /* synthetic */ BubbleController$BubblesImpl$$ExternalSyntheticLambda5(BubbleController.BubblesImpl bubblesImpl, BubbleEntry bubbleEntry) {
        this.f$0 = bubblesImpl;
        this.f$1 = bubbleEntry;
    }

    public final void run() {
        this.f$0.lambda$onEntryRemoved$16(this.f$1);
    }
}
