package com.android.wm.shell.bubbles;

import android.graphics.PointF;
import com.android.wm.shell.bubbles.Bubble;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BubbleFlyoutView$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ BubbleFlyoutView f$0;
    public final /* synthetic */ Bubble.FlyoutMessage f$1;
    public final /* synthetic */ PointF f$2;
    public final /* synthetic */ boolean f$3;

    public /* synthetic */ BubbleFlyoutView$$ExternalSyntheticLambda0(BubbleFlyoutView bubbleFlyoutView, Bubble.FlyoutMessage flyoutMessage, PointF pointF, boolean z) {
        this.f$0 = bubbleFlyoutView;
        this.f$1 = flyoutMessage;
        this.f$2 = pointF;
        this.f$3 = z;
    }

    public final void run() {
        this.f$0.lambda$animateUpdate$2(this.f$1, this.f$2, this.f$3);
    }
}
