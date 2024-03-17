package com.android.wm.shell.bubbles;

import java.util.concurrent.Executor;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BubbleController$BubblesImpl$$ExternalSyntheticLambda23 implements Consumer {
    public final /* synthetic */ Executor f$0;
    public final /* synthetic */ Consumer f$1;

    public /* synthetic */ BubbleController$BubblesImpl$$ExternalSyntheticLambda23(Executor executor, Consumer consumer) {
        this.f$0 = executor;
        this.f$1 = consumer;
    }

    public final void accept(Object obj) {
        this.f$0.execute(new BubbleController$BubblesImpl$$ExternalSyntheticLambda24(this.f$1, (String) obj));
    }
}
