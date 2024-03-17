package com.android.wm.shell.bubbles;

import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BubbleStackView$$ExternalSyntheticLambda45 implements Runnable {
    public final /* synthetic */ Consumer f$0;

    public /* synthetic */ BubbleStackView$$ExternalSyntheticLambda45(Consumer consumer) {
        this.f$0 = consumer;
    }

    public final void run() {
        this.f$0.accept(Boolean.TRUE);
    }
}
