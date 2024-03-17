package com.android.systemui.statusbar.connectivity;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class CallbackHandler$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ CallbackHandler f$0;
    public final /* synthetic */ IconState f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ CallbackHandler$$ExternalSyntheticLambda2(CallbackHandler callbackHandler, IconState iconState, int i) {
        this.f$0 = callbackHandler;
        this.f$1 = iconState;
        this.f$2 = i;
    }

    public final void run() {
        this.f$0.lambda$setCallIndicator$3(this.f$1, this.f$2);
    }
}
