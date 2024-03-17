package com.android.wm.shell.bubbles;

import android.view.ViewTreeObserver;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BubbleStackView$$ExternalSyntheticLambda11 implements ViewTreeObserver.OnDrawListener {
    public final /* synthetic */ BubbleStackView f$0;

    public /* synthetic */ BubbleStackView$$ExternalSyntheticLambda11(BubbleStackView bubbleStackView) {
        this.f$0 = bubbleStackView;
    }

    public final void onDraw() {
        this.f$0.updateSystemGestureExcludeRects();
    }
}
