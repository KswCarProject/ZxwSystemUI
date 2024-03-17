package com.android.wm.shell.bubbles;

import java.util.concurrent.Executor;
import java.util.function.IntConsumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BubbleController$BubblesImpl$$ExternalSyntheticLambda10 implements IntConsumer {
    public final /* synthetic */ Executor f$0;
    public final /* synthetic */ IntConsumer f$1;

    public /* synthetic */ BubbleController$BubblesImpl$$ExternalSyntheticLambda10(Executor executor, IntConsumer intConsumer) {
        this.f$0 = executor;
        this.f$1 = intConsumer;
    }

    public final void accept(int i) {
        this.f$0.execute(new BubbleController$BubblesImpl$$ExternalSyntheticLambda13(this.f$1, i));
    }
}
