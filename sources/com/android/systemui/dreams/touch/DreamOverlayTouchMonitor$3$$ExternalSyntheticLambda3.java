package com.android.systemui.dreams.touch;

import android.view.GestureDetector;
import android.view.MotionEvent;
import com.android.systemui.dreams.touch.DreamOverlayTouchMonitor;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda3 implements DreamOverlayTouchMonitor.Evaluator {
    public final /* synthetic */ MotionEvent f$0;

    public /* synthetic */ DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda3(MotionEvent motionEvent) {
        this.f$0 = motionEvent;
    }

    public final boolean evaluate(GestureDetector.OnGestureListener onGestureListener) {
        return onGestureListener.onDown(this.f$0);
    }
}
