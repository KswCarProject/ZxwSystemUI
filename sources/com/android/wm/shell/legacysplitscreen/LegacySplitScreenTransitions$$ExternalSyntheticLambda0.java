package com.android.wm.shell.legacysplitscreen;

import android.window.WindowContainerTransaction;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class LegacySplitScreenTransitions$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ LegacySplitScreenTransitions f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ WindowContainerTransaction f$2;

    public /* synthetic */ LegacySplitScreenTransitions$$ExternalSyntheticLambda0(LegacySplitScreenTransitions legacySplitScreenTransitions, boolean z, WindowContainerTransaction windowContainerTransaction) {
        this.f$0 = legacySplitScreenTransitions;
        this.f$1 = z;
        this.f$2 = windowContainerTransaction;
    }

    public final void run() {
        this.f$0.lambda$dismissSplit$6(this.f$1, this.f$2);
    }
}
