package com.android.systemui.statusbar.notification.stack;

import android.animation.ValueAnimator;
import com.android.systemui.statusbar.notification.row.ExpandableView;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ExpandableViewState$$ExternalSyntheticLambda0 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ boolean f$0;
    public final /* synthetic */ ExpandableView f$1;

    public /* synthetic */ ExpandableViewState$$ExternalSyntheticLambda0(boolean z, ExpandableView expandableView) {
        this.f$0 = z;
        this.f$1 = expandableView;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        ExpandableViewState.lambda$startClipAnimation$0(this.f$0, this.f$1, valueAnimator);
    }
}
