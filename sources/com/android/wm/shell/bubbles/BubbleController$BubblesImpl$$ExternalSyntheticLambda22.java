package com.android.wm.shell.bubbles;

import com.android.wm.shell.bubbles.BubbleController;
import com.android.wm.shell.bubbles.Bubbles;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BubbleController$BubblesImpl$$ExternalSyntheticLambda22 implements Runnable {
    public final /* synthetic */ BubbleController.BubblesImpl f$0;
    public final /* synthetic */ Bubbles.BubbleExpandListener f$1;

    public /* synthetic */ BubbleController$BubblesImpl$$ExternalSyntheticLambda22(BubbleController.BubblesImpl bubblesImpl, Bubbles.BubbleExpandListener bubbleExpandListener) {
        this.f$0 = bubblesImpl;
        this.f$1 = bubbleExpandListener;
    }

    public final void run() {
        this.f$0.lambda$setExpandListener$13(this.f$1);
    }
}
