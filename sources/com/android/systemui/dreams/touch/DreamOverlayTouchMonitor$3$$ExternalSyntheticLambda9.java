package com.android.systemui.dreams.touch;

import android.view.GestureDetector;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda9 implements Consumer {
    public final /* synthetic */ Consumer f$0;

    public /* synthetic */ DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda9(Consumer consumer) {
        this.f$0 = consumer;
    }

    public final void accept(Object obj) {
        this.f$0.accept((GestureDetector.OnGestureListener) obj);
    }
}
