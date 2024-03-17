package com.android.wm.shell.bubbles;

import android.util.ArrayMap;
import com.android.wm.shell.animation.PhysicsAnimator;
import com.android.wm.shell.bubbles.animation.AnimatableScaleMatrix;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BubbleStackView$$ExternalSyntheticLambda28 implements PhysicsAnimator.UpdateListener {
    public final /* synthetic */ BubbleStackView f$0;

    public /* synthetic */ BubbleStackView$$ExternalSyntheticLambda28(BubbleStackView bubbleStackView) {
        this.f$0 = bubbleStackView;
    }

    public final void onAnimationUpdateForProperty(Object obj, ArrayMap arrayMap) {
        this.f$0.lambda$hideExpandedViewIfNeeded$20((AnimatableScaleMatrix) obj, arrayMap);
    }
}
