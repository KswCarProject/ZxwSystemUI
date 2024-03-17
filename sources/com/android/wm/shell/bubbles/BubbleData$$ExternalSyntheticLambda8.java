package com.android.wm.shell.bubbles;

import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BubbleData$$ExternalSyntheticLambda8 implements Predicate {
    public final /* synthetic */ int f$0;

    public /* synthetic */ BubbleData$$ExternalSyntheticLambda8(int i) {
        this.f$0 = i;
    }

    public final boolean test(Object obj) {
        return BubbleData.lambda$removeBubblesForUser$5(this.f$0, (Bubble) obj);
    }
}
