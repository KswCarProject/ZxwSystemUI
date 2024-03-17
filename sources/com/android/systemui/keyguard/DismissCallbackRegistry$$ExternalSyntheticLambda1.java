package com.android.systemui.keyguard;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DismissCallbackRegistry$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ DismissCallbackWrapper f$0;

    public /* synthetic */ DismissCallbackRegistry$$ExternalSyntheticLambda1(DismissCallbackWrapper dismissCallbackWrapper) {
        this.f$0 = dismissCallbackWrapper;
    }

    public final void run() {
        this.f$0.notifyDismissSucceeded();
    }
}
