package com.android.systemui.dreams.touch;

import com.android.systemui.dreams.touch.DreamOverlayTouchMonitor;
import java.util.Set;
import java.util.function.Function;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda6 implements Function {
    public final /* synthetic */ DreamOverlayTouchMonitor.Evaluator f$0;
    public final /* synthetic */ Set f$1;

    public /* synthetic */ DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda6(DreamOverlayTouchMonitor.Evaluator evaluator, Set set) {
        this.f$0 = evaluator;
        this.f$1 = set;
    }

    public final Object apply(Object obj) {
        return DreamOverlayTouchMonitor.AnonymousClass3.lambda$evaluate$2(this.f$0, this.f$1, (DreamOverlayTouchMonitor.TouchSessionImpl) obj);
    }
}
