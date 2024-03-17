package com.android.systemui.statusbar.phone;

import android.os.Vibrator;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class CentralSurfacesCommandQueueCallbacks$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ CentralSurfacesCommandQueueCallbacks f$0;

    public /* synthetic */ CentralSurfacesCommandQueueCallbacks$$ExternalSyntheticLambda0(CentralSurfacesCommandQueueCallbacks centralSurfacesCommandQueueCallbacks) {
        this.f$0 = centralSurfacesCommandQueueCallbacks;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$vibrateForCameraGesture$0((Vibrator) obj);
    }
}
