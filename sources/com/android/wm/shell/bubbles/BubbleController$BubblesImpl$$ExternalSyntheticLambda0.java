package com.android.wm.shell.bubbles;

import com.android.wm.shell.bubbles.BubbleController;
import java.io.PrintWriter;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BubbleController$BubblesImpl$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ BubbleController.BubblesImpl f$0;
    public final /* synthetic */ PrintWriter f$1;
    public final /* synthetic */ String[] f$2;

    public /* synthetic */ BubbleController$BubblesImpl$$ExternalSyntheticLambda0(BubbleController.BubblesImpl bubblesImpl, PrintWriter printWriter, String[] strArr) {
        this.f$0 = bubblesImpl;
        this.f$1 = printWriter;
        this.f$2 = strArr;
    }

    public final void run() {
        this.f$0.lambda$dump$26(this.f$1, this.f$2);
    }
}
