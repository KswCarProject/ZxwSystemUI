package com.android.systemui.dreams.touch.dagger;

import android.animation.ValueAnimator;
import com.android.systemui.dreams.touch.BouncerSwipeTouchHandler;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BouncerSwipeModule$$ExternalSyntheticLambda1 implements BouncerSwipeTouchHandler.ValueAnimatorCreator {
    public final ValueAnimator create(float f, float f2) {
        return ValueAnimator.ofFloat(new float[]{f, f2});
    }
}
