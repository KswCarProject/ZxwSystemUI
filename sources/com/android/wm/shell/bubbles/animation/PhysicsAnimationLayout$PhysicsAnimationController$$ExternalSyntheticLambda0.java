package com.android.wm.shell.bubbles.animation;

import com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout;
import java.util.List;
import java.util.Set;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class PhysicsAnimationLayout$PhysicsAnimationController$$ExternalSyntheticLambda0 implements PhysicsAnimationLayout.PhysicsAnimationController.MultiAnimationStarter {
    public final /* synthetic */ PhysicsAnimationLayout.PhysicsAnimationController f$0;
    public final /* synthetic */ Set f$1;
    public final /* synthetic */ List f$2;

    public /* synthetic */ PhysicsAnimationLayout$PhysicsAnimationController$$ExternalSyntheticLambda0(PhysicsAnimationLayout.PhysicsAnimationController physicsAnimationController, Set set, List list) {
        this.f$0 = physicsAnimationController;
        this.f$1 = set;
        this.f$2 = list;
    }

    public final void startAll(Runnable[] runnableArr) {
        this.f$0.lambda$animationsForChildrenFromIndex$1(this.f$1, this.f$2, runnableArr);
    }
}
