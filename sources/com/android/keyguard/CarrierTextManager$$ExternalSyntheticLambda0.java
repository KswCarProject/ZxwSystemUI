package com.android.keyguard;

import com.android.keyguard.CarrierTextManager;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class CarrierTextManager$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ CarrierTextManager f$0;
    public final /* synthetic */ CarrierTextManager.CarrierTextCallback f$1;

    public /* synthetic */ CarrierTextManager$$ExternalSyntheticLambda0(CarrierTextManager carrierTextManager, CarrierTextManager.CarrierTextCallback carrierTextCallback) {
        this.f$0 = carrierTextManager;
        this.f$1 = carrierTextCallback;
    }

    public final void run() {
        this.f$0.lambda$setListening$4(this.f$1);
    }
}
