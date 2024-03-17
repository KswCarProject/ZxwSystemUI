package com.android.systemui.statusbar.policy;

import com.android.systemui.statusbar.policy.LocationController;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class LocationControllerImpl$H$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ boolean f$0;

    public /* synthetic */ LocationControllerImpl$H$$ExternalSyntheticLambda0(boolean z) {
        this.f$0 = z;
    }

    public final void accept(Object obj) {
        ((LocationController.LocationChangeCallback) obj).onLocationSettingsChanged(this.f$0);
    }
}
