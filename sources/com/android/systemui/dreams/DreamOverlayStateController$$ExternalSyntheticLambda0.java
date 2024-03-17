package com.android.systemui.dreams;

import com.android.systemui.dreams.DreamOverlayStateController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DreamOverlayStateController$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ DreamOverlayStateController f$0;
    public final /* synthetic */ DreamOverlayStateController.Callback f$1;

    public /* synthetic */ DreamOverlayStateController$$ExternalSyntheticLambda0(DreamOverlayStateController dreamOverlayStateController, DreamOverlayStateController.Callback callback) {
        this.f$0 = dreamOverlayStateController;
        this.f$1 = callback;
    }

    public final void run() {
        this.f$0.lambda$removeCallback$7(this.f$1);
    }
}
