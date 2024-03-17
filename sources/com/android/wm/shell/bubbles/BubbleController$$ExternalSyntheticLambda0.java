package com.android.wm.shell.bubbles;

import com.android.wm.shell.bubbles.BubbleViewInfoTask;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BubbleController$$ExternalSyntheticLambda0 implements BubbleViewInfoTask.Callback {
    public final /* synthetic */ BubbleController f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ BubbleController$$ExternalSyntheticLambda0(BubbleController bubbleController, boolean z, boolean z2) {
        this.f$0 = bubbleController;
        this.f$1 = z;
        this.f$2 = z2;
    }

    public final void onBubbleViewsReady(Bubble bubble) {
        this.f$0.lambda$inflateAndAdd$11(this.f$1, this.f$2, bubble);
    }
}
