package com.android.systemui.biometrics;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class UdfpsEnrollView$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ UdfpsEnrollProgressBarDrawable f$0;

    public /* synthetic */ UdfpsEnrollView$$ExternalSyntheticLambda2(UdfpsEnrollProgressBarDrawable udfpsEnrollProgressBarDrawable) {
        this.f$0 = udfpsEnrollProgressBarDrawable;
    }

    public final void run() {
        this.f$0.onLastStepAcquired();
    }
}
