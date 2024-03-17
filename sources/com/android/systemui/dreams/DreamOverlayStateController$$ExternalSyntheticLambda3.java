package com.android.systemui.dreams;

import com.android.systemui.dreams.DreamOverlayStateController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DreamOverlayStateController$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ DreamOverlayStateController f$0;
    public final /* synthetic */ DreamOverlayStateController.Callback f$1;

    public /* synthetic */ DreamOverlayStateController$$ExternalSyntheticLambda3(DreamOverlayStateController dreamOverlayStateController, DreamOverlayStateController.Callback callback) {
        this.f$0 = dreamOverlayStateController;
        this.f$1 = callback;
    }

    public final void run() {
        this.f$0.lambda$addCallback$6(this.f$1);
    }
}
