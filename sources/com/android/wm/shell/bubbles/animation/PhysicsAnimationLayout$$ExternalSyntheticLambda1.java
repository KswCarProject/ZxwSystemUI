package com.android.wm.shell.bubbles.animation;

import android.view.View;
import androidx.dynamicanimation.animation.DynamicAnimation;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class PhysicsAnimationLayout$$ExternalSyntheticLambda1 implements DynamicAnimation.OnAnimationUpdateListener {
    public final /* synthetic */ PhysicsAnimationLayout f$0;
    public final /* synthetic */ View f$1;
    public final /* synthetic */ DynamicAnimation.ViewProperty f$2;

    public /* synthetic */ PhysicsAnimationLayout$$ExternalSyntheticLambda1(PhysicsAnimationLayout physicsAnimationLayout, View view, DynamicAnimation.ViewProperty viewProperty) {
        this.f$0 = physicsAnimationLayout;
        this.f$1 = view;
        this.f$2 = viewProperty;
    }

    public final void onAnimationUpdate(DynamicAnimation dynamicAnimation, float f, float f2) {
        this.f$0.lambda$setUpAnimationForChild$1(this.f$1, this.f$2, dynamicAnimation, f, f2);
    }
}
