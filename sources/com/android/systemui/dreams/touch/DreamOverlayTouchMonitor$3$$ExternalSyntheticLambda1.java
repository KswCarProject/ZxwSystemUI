package com.android.systemui.dreams.touch;

import android.view.GestureDetector;
import android.view.MotionEvent;
import com.android.systemui.dreams.touch.DreamOverlayTouchMonitor;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda1 implements DreamOverlayTouchMonitor.Evaluator {
    public final /* synthetic */ MotionEvent f$0;
    public final /* synthetic */ MotionEvent f$1;
    public final /* synthetic */ float f$2;
    public final /* synthetic */ float f$3;

    public /* synthetic */ DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda1(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        this.f$0 = motionEvent;
        this.f$1 = motionEvent2;
        this.f$2 = f;
        this.f$3 = f2;
    }

    public final boolean evaluate(GestureDetector.OnGestureListener onGestureListener) {
        return onGestureListener.onScroll(this.f$0, this.f$1, this.f$2, this.f$3);
    }
}
