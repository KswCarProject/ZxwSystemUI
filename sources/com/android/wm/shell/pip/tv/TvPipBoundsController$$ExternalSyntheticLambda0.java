package com.android.wm.shell.pip.tv;

import com.android.wm.shell.pip.tv.TvPipKeepClearAlgorithm;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class TvPipBoundsController$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ TvPipBoundsController f$0;
    public final /* synthetic */ TvPipKeepClearAlgorithm.Placement f$1;

    public /* synthetic */ TvPipBoundsController$$ExternalSyntheticLambda0(TvPipBoundsController tvPipBoundsController, TvPipKeepClearAlgorithm.Placement placement) {
        this.f$0 = tvPipBoundsController;
        this.f$1 = placement;
    }

    public final void run() {
        this.f$0.lambda$scheduleUnstashIfNeeded$0(this.f$1);
    }
}
