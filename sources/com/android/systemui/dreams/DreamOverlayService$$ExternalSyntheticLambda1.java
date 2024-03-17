package com.android.systemui.dreams;

import androidx.lifecycle.Lifecycle;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DreamOverlayService$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ DreamOverlayService f$0;
    public final /* synthetic */ Lifecycle.State f$1;

    public /* synthetic */ DreamOverlayService$$ExternalSyntheticLambda1(DreamOverlayService dreamOverlayService, Lifecycle.State state) {
        this.f$0 = dreamOverlayService;
        this.f$1 = state;
    }

    public final void run() {
        this.f$0.lambda$setCurrentState$0(this.f$1);
    }
}
