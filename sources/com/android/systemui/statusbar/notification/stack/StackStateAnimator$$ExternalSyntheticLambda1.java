package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.statusbar.notification.row.ExpandableView;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class StackStateAnimator$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ StackStateAnimator f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ ExpandableView f$2;

    public /* synthetic */ StackStateAnimator$$ExternalSyntheticLambda1(StackStateAnimator stackStateAnimator, String str, ExpandableView expandableView) {
        this.f$0 = stackStateAnimator;
        this.f$1 = str;
        this.f$2 = expandableView;
    }

    public final void run() {
        this.f$0.lambda$processAnimationEvents$0(this.f$1, this.f$2);
    }
}
