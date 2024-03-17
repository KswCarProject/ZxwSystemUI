package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.statusbar.notification.row.ExpandableView;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class StackStateAnimator$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ ExpandableView f$0;

    public /* synthetic */ StackStateAnimator$$ExternalSyntheticLambda0(ExpandableView expandableView) {
        this.f$0 = expandableView;
    }

    public final void run() {
        this.f$0.removeFromTransientContainer();
    }
}
