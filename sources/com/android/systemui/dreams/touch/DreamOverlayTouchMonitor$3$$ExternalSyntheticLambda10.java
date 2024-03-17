package com.android.systemui.dreams.touch;

import android.view.GestureDetector;
import com.android.systemui.dreams.touch.DreamOverlayTouchMonitor;
import java.util.function.Function;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda10 implements Function {
    public final /* synthetic */ DreamOverlayTouchMonitor.Evaluator f$0;

    public /* synthetic */ DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda10(DreamOverlayTouchMonitor.Evaluator evaluator) {
        this.f$0 = evaluator;
    }

    public final Object apply(Object obj) {
        return Boolean.valueOf(this.f$0.evaluate((GestureDetector.OnGestureListener) obj));
    }
}
