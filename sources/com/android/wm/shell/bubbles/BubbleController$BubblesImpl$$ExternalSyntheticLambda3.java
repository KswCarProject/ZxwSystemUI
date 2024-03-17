package com.android.wm.shell.bubbles;

import android.content.res.Configuration;
import com.android.wm.shell.bubbles.BubbleController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BubbleController$BubblesImpl$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ BubbleController.BubblesImpl f$0;
    public final /* synthetic */ Configuration f$1;

    public /* synthetic */ BubbleController$BubblesImpl$$ExternalSyntheticLambda3(BubbleController.BubblesImpl bubblesImpl, Configuration configuration) {
        this.f$0 = bubblesImpl;
        this.f$1 = configuration;
    }

    public final void run() {
        this.f$0.lambda$onConfigChanged$25(this.f$1);
    }
}
