package com.android.wm.shell.bubbles;

import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BubbleStackView$$ExternalSyntheticLambda32 implements Runnable {
    public final /* synthetic */ BubbleStackView f$0;
    public final /* synthetic */ Consumer f$1;

    public /* synthetic */ BubbleStackView$$ExternalSyntheticLambda32(BubbleStackView bubbleStackView, Consumer consumer) {
        this.f$0 = bubbleStackView;
        this.f$1 = consumer;
    }

    public final void run() {
        this.f$0.lambda$screenshotAnimatingOutBubbleIntoSurface$45(this.f$1);
    }
}
