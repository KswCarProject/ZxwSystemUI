package com.android.wm.shell.bubbles;

import android.util.SparseArray;
import com.android.wm.shell.bubbles.BubbleController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BubbleController$BubblesImpl$$ExternalSyntheticLambda15 implements Runnable {
    public final /* synthetic */ BubbleController.BubblesImpl f$0;
    public final /* synthetic */ SparseArray f$1;

    public /* synthetic */ BubbleController$BubblesImpl$$ExternalSyntheticLambda15(BubbleController.BubblesImpl bubblesImpl, SparseArray sparseArray) {
        this.f$0 = bubblesImpl;
        this.f$1 = sparseArray;
    }

    public final void run() {
        this.f$0.lambda$onCurrentProfilesChanged$23(this.f$1);
    }
}
