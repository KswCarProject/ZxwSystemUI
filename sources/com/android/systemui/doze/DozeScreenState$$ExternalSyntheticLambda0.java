package com.android.systemui.doze;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DozeScreenState$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ DozeScreenState f$0;

    public /* synthetic */ DozeScreenState$$ExternalSyntheticLambda0(DozeScreenState dozeScreenState) {
        this.f$0 = dozeScreenState;
    }

    public final void run() {
        this.f$0.applyPendingScreenState();
    }
}
