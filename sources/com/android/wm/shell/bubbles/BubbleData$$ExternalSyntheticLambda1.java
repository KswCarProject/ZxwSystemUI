package com.android.wm.shell.bubbles;

import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BubbleData$$ExternalSyntheticLambda1 implements Predicate {
    public final /* synthetic */ String f$0;

    public /* synthetic */ BubbleData$$ExternalSyntheticLambda1(String str) {
        this.f$0 = str;
    }

    public final boolean test(Object obj) {
        return ((Bubble) obj).getKey().equals(this.f$0);
    }
}
