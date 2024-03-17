package com.android.wm.shell.bubbles;

import java.util.ArrayList;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BubbleData$$ExternalSyntheticLambda6 implements Consumer {
    public final /* synthetic */ ArrayList f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ BubbleData$$ExternalSyntheticLambda6(ArrayList arrayList, int i) {
        this.f$0 = arrayList;
        this.f$1 = i;
    }

    public final void accept(Object obj) {
        BubbleData.lambda$trim$7(this.f$0, this.f$1, (Bubble) obj);
    }
}
