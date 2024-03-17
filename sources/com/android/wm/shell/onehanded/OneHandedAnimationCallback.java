package com.android.wm.shell.onehanded;

import android.view.SurfaceControl;
import com.android.wm.shell.onehanded.OneHandedAnimationController;

public interface OneHandedAnimationCallback {
    void onAnimationUpdate(SurfaceControl.Transaction transaction, float f, float f2) {
    }

    void onOneHandedAnimationCancel(OneHandedAnimationController.OneHandedTransitionAnimator oneHandedTransitionAnimator) {
    }

    void onOneHandedAnimationEnd(SurfaceControl.Transaction transaction, OneHandedAnimationController.OneHandedTransitionAnimator oneHandedTransitionAnimator) {
    }

    void onOneHandedAnimationStart(OneHandedAnimationController.OneHandedTransitionAnimator oneHandedTransitionAnimator) {
    }
}
