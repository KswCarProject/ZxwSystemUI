package com.android.wm.shell.bubbles.animation;

import androidx.dynamicanimation.animation.SpringAnimation;
import com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class PhysicsAnimationLayout$PhysicsPropertyAnimator$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ PhysicsAnimationLayout.PhysicsPropertyAnimator f$0;
    public final /* synthetic */ SpringAnimation f$1;
    public final /* synthetic */ SpringAnimation f$2;

    public /* synthetic */ PhysicsAnimationLayout$PhysicsPropertyAnimator$$ExternalSyntheticLambda1(PhysicsAnimationLayout.PhysicsPropertyAnimator physicsPropertyAnimator, SpringAnimation springAnimation, SpringAnimation springAnimation2) {
        this.f$0 = physicsPropertyAnimator;
        this.f$1 = springAnimation;
        this.f$2 = springAnimation2;
    }

    public final void run() {
        this.f$0.lambda$start$1(this.f$1, this.f$2);
    }
}
