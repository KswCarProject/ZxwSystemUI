package com.android.systemui.statusbar.notification.row;

import android.animation.ValueAnimator;
import android.view.SurfaceControl;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ExpandableNotificationRowDragController$$ExternalSyntheticLambda1 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ SurfaceControl.Transaction f$0;
    public final /* synthetic */ SurfaceControl f$1;

    public /* synthetic */ ExpandableNotificationRowDragController$$ExternalSyntheticLambda1(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl) {
        this.f$0 = transaction;
        this.f$1 = surfaceControl;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        ExpandableNotificationRowDragController.lambda$fadeOutAndRemoveDragSurface$1(this.f$0, this.f$1, valueAnimator);
    }
}
