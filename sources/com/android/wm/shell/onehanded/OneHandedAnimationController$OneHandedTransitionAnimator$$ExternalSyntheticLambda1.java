package com.android.wm.shell.onehanded;

import android.view.SurfaceControl;
import com.android.wm.shell.onehanded.OneHandedAnimationController;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class OneHandedAnimationController$OneHandedTransitionAnimator$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ OneHandedAnimationController.OneHandedTransitionAnimator f$0;
    public final /* synthetic */ SurfaceControl.Transaction f$1;

    public /* synthetic */ OneHandedAnimationController$OneHandedTransitionAnimator$$ExternalSyntheticLambda1(OneHandedAnimationController.OneHandedTransitionAnimator oneHandedTransitionAnimator, SurfaceControl.Transaction transaction) {
        this.f$0 = oneHandedTransitionAnimator;
        this.f$1 = transaction;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$onAnimationEnd$1(this.f$1, (OneHandedAnimationCallback) obj);
    }
}
