package com.android.wm.shell.pip;

import com.android.wm.shell.pip.PipAnimationController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class PipTaskOrganizer$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ PipAnimationController.PipTransitionAnimator f$0;

    public /* synthetic */ PipTaskOrganizer$$ExternalSyntheticLambda4(PipAnimationController.PipTransitionAnimator pipTransitionAnimator) {
        this.f$0 = pipTransitionAnimator;
    }

    public final void run() {
        this.f$0.clearContentOverlay();
    }
}
