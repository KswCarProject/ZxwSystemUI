package com.android.wm.shell.bubbles;

import com.android.wm.shell.bubbles.BubbleController;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BubbleController$BubblesImpl$$ExternalSyntheticLambda21 implements Runnable {
    public final /* synthetic */ BubbleController.BubblesImpl f$0;
    public final /* synthetic */ Consumer f$1;
    public final /* synthetic */ Executor f$2;
    public final /* synthetic */ String f$3;

    public /* synthetic */ BubbleController$BubblesImpl$$ExternalSyntheticLambda21(BubbleController.BubblesImpl bubblesImpl, Consumer consumer, Executor executor, String str) {
        this.f$0 = bubblesImpl;
        this.f$1 = consumer;
        this.f$2 = executor;
        this.f$3 = str;
    }

    public final void run() {
        this.f$0.lambda$removeSuppressedSummaryIfNecessary$2(this.f$1, this.f$2, this.f$3);
    }
}
