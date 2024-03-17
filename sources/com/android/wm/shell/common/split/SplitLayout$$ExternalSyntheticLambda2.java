package com.android.wm.shell.common.split;

import com.android.internal.policy.DividerSnapAlgorithm;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SplitLayout$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ SplitLayout f$0;
    public final /* synthetic */ DividerSnapAlgorithm.SnapTarget f$1;

    public /* synthetic */ SplitLayout$$ExternalSyntheticLambda2(SplitLayout splitLayout, DividerSnapAlgorithm.SnapTarget snapTarget) {
        this.f$0 = splitLayout;
        this.f$1 = snapTarget;
    }

    public final void run() {
        this.f$0.lambda$snapToTarget$2(this.f$1);
    }
}
