package com.android.systemui.statusbar.policy;

import com.android.systemui.statusbar.policy.LocationController;
import com.android.systemui.statusbar.policy.LocationControllerImpl;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class LocationControllerImpl$H$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ LocationControllerImpl.H f$0;

    public /* synthetic */ LocationControllerImpl$H$$ExternalSyntheticLambda1(LocationControllerImpl.H h) {
        this.f$0 = h;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$locationActiveChanged$0((LocationController.LocationChangeCallback) obj);
    }
}
