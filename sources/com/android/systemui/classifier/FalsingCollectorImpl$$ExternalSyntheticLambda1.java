package com.android.systemui.classifier;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class FalsingCollectorImpl$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ FalsingDataProvider f$0;

    public /* synthetic */ FalsingCollectorImpl$$ExternalSyntheticLambda1(FalsingDataProvider falsingDataProvider) {
        this.f$0 = falsingDataProvider;
    }

    public final void run() {
        this.f$0.onMotionEventComplete();
    }
}
