package com.android.systemui.shared.navigationbar;

import android.view.CompositionSamplingListener;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class RegionSamplingHelper$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ CompositionSamplingListener f$0;

    public /* synthetic */ RegionSamplingHelper$$ExternalSyntheticLambda0(CompositionSamplingListener compositionSamplingListener) {
        this.f$0 = compositionSamplingListener;
    }

    public final void run() {
        this.f$0.destroy();
    }
}
