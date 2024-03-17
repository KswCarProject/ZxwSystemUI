package com.android.wm.shell.bubbles;

import com.android.wm.shell.draganddrop.DragAndDropController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BubbleController$$ExternalSyntheticLambda15 implements DragAndDropController.DragAndDropListener {
    public final /* synthetic */ BubbleController f$0;

    public /* synthetic */ BubbleController$$ExternalSyntheticLambda15(BubbleController bubbleController) {
        this.f$0 = bubbleController;
    }

    public final void onDragStarted() {
        this.f$0.collapseStack();
    }
}
