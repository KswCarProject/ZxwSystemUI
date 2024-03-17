package com.android.wm.shell.bubbles;

import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BubbleController$$ExternalSyntheticLambda6 implements Consumer {
    public final /* synthetic */ BubbleController f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ Bubble f$2;

    public /* synthetic */ BubbleController$$ExternalSyntheticLambda6(BubbleController bubbleController, boolean z, Bubble bubble) {
        this.f$0 = bubbleController;
        this.f$1 = z;
        this.f$2 = bubble;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$setIsBubble$13(this.f$1, this.f$2, (BubbleEntry) obj);
    }
}
