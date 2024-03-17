package com.android.wm.shell.bubbles;

import com.android.wm.shell.bubbles.Bubbles;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BubbleController$$ExternalSyntheticLambda1 implements Bubbles.BubbleExpandListener {
    public final /* synthetic */ Bubbles.BubbleExpandListener f$0;

    public /* synthetic */ BubbleController$$ExternalSyntheticLambda1(Bubbles.BubbleExpandListener bubbleExpandListener) {
        this.f$0 = bubbleExpandListener;
    }

    public final void onBubbleExpandChanged(boolean z, String str) {
        BubbleController.lambda$setExpandListener$7(this.f$0, z, str);
    }
}
