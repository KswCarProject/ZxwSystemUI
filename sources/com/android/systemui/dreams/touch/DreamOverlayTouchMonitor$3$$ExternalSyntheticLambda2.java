package com.android.systemui.dreams.touch;

import android.view.GestureDetector;
import android.view.MotionEvent;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda2 implements Consumer {
    public final /* synthetic */ MotionEvent f$0;

    public /* synthetic */ DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda2(MotionEvent motionEvent) {
        this.f$0 = motionEvent;
    }

    public final void accept(Object obj) {
        ((GestureDetector.OnGestureListener) obj).onShowPress(this.f$0);
    }
}
