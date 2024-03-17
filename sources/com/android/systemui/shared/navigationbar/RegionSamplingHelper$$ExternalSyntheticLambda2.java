package com.android.systemui.shared.navigationbar;

import android.view.SurfaceControl;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class RegionSamplingHelper$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ RegionSamplingHelper f$0;
    public final /* synthetic */ SurfaceControl f$1;

    public /* synthetic */ RegionSamplingHelper$$ExternalSyntheticLambda2(RegionSamplingHelper regionSamplingHelper, SurfaceControl surfaceControl) {
        this.f$0 = regionSamplingHelper;
        this.f$1 = surfaceControl;
    }

    public final void run() {
        this.f$0.lambda$unregisterSamplingListener$1(this.f$1);
    }
}
