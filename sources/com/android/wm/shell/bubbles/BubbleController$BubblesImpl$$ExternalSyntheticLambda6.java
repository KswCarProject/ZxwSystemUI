package com.android.wm.shell.bubbles;

import com.android.wm.shell.bubbles.BubbleController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BubbleController$BubblesImpl$$ExternalSyntheticLambda6 implements Runnable {
    public final /* synthetic */ BubbleController.BubblesImpl f$0;
    public final /* synthetic */ boolean f$1;

    public /* synthetic */ BubbleController$BubblesImpl$$ExternalSyntheticLambda6(BubbleController.BubblesImpl bubblesImpl, boolean z) {
        this.f$0 = bubblesImpl;
        this.f$1 = z;
    }

    public final void run() {
        this.f$0.lambda$onStatusBarStateChanged$21(this.f$1);
    }
}
