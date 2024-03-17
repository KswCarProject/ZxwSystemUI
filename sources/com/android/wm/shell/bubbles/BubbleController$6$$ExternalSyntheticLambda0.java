package com.android.wm.shell.bubbles;

import com.android.wm.shell.bubbles.BubbleController;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BubbleController$6$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ BubbleController.AnonymousClass6 f$0;
    public final /* synthetic */ Bubble f$1;

    public /* synthetic */ BubbleController$6$$ExternalSyntheticLambda0(BubbleController.AnonymousClass6 r1, Bubble bubble) {
        this.f$0 = r1;
        this.f$1 = bubble;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$applyUpdate$1(this.f$1, (BubbleEntry) obj);
    }
}
