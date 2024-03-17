package com.android.systemui.qs.carrier;

import com.android.keyguard.CarrierTextManager;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class QSCarrierGroupController$$ExternalSyntheticLambda2 implements Consumer {
    public final /* synthetic */ QSCarrierGroupController f$0;

    public /* synthetic */ QSCarrierGroupController$$ExternalSyntheticLambda2(QSCarrierGroupController qSCarrierGroupController) {
        this.f$0 = qSCarrierGroupController;
    }

    public final void accept(Object obj) {
        this.f$0.handleUpdateCarrierInfo((CarrierTextManager.CarrierTextCallbackInfo) obj);
    }
}
