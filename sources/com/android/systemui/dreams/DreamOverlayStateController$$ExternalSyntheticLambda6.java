package com.android.systemui.dreams;

import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DreamOverlayStateController$$ExternalSyntheticLambda6 implements Runnable {
    public final /* synthetic */ DreamOverlayStateController f$0;
    public final /* synthetic */ Consumer f$1;

    public /* synthetic */ DreamOverlayStateController$$ExternalSyntheticLambda6(DreamOverlayStateController dreamOverlayStateController, Consumer consumer) {
        this.f$0 = dreamOverlayStateController;
        this.f$1 = consumer;
    }

    public final void run() {
        this.f$0.lambda$notifyCallbacks$5(this.f$1);
    }
}
