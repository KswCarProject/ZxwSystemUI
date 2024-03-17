package com.android.wm.shell.bubbles;

import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BubbleData$$ExternalSyntheticLambda12 implements Consumer {
    public final /* synthetic */ BubbleData f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ BubbleData$$ExternalSyntheticLambda12(BubbleData bubbleData, int i) {
        this.f$0 = bubbleData;
        this.f$1 = i;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$removeBubblesWithPackageName$4(this.f$1, (Bubble) obj);
    }
}
