package com.android.systemui.statusbar.connectivity;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class CallbackHandler$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ CallbackHandler f$0;
    public final /* synthetic */ MobileDataIndicators f$1;

    public /* synthetic */ CallbackHandler$$ExternalSyntheticLambda3(CallbackHandler callbackHandler, MobileDataIndicators mobileDataIndicators) {
        this.f$0 = callbackHandler;
        this.f$1 = mobileDataIndicators;
    }

    public final void run() {
        this.f$0.lambda$setMobileDataIndicators$1(this.f$1);
    }
}
