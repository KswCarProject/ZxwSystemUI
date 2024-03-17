package com.android.wm.shell.bubbles;

import com.android.wm.shell.bubbles.BubbleViewInfoTask;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BubbleController$$ExternalSyntheticLambda17 implements BubbleViewInfoTask.Callback {
    public final /* synthetic */ BubbleController f$0;
    public final /* synthetic */ Bubble f$1;

    public /* synthetic */ BubbleController$$ExternalSyntheticLambda17(BubbleController bubbleController, Bubble bubble) {
        this.f$0 = bubbleController;
        this.f$1 = bubble;
    }

    public final void onBubbleViewsReady(Bubble bubble) {
        this.f$0.lambda$loadOverflowBubblesFromDisk$8(this.f$1, bubble);
    }
}
