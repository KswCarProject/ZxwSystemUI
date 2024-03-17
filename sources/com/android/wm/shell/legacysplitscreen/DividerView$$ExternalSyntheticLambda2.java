package com.android.wm.shell.legacysplitscreen;

import com.android.internal.policy.DividerSnapAlgorithm;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DividerView$$ExternalSyntheticLambda2 implements Consumer {
    public final /* synthetic */ DividerView f$0;
    public final /* synthetic */ DividerSnapAlgorithm.SnapTarget f$1;

    public /* synthetic */ DividerView$$ExternalSyntheticLambda2(DividerView dividerView, DividerSnapAlgorithm.SnapTarget snapTarget) {
        this.f$0 = dividerView;
        this.f$1 = snapTarget;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$getFlingAnimator$2(this.f$1, (Boolean) obj);
    }
}
