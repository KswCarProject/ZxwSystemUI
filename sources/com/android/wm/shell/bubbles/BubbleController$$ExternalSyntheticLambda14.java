package com.android.wm.shell.bubbles;

import com.android.wm.shell.onehanded.OneHandedController;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BubbleController$$ExternalSyntheticLambda14 implements Consumer {
    public final /* synthetic */ BubbleController f$0;

    public /* synthetic */ BubbleController$$ExternalSyntheticLambda14(BubbleController bubbleController) {
        this.f$0 = bubbleController;
    }

    public final void accept(Object obj) {
        this.f$0.registerOneHandedState((OneHandedController) obj);
    }
}
