package com.android.systemui.dreams.touch;

import com.android.systemui.dreams.touch.DreamOverlayTouchMonitor;
import java.util.Set;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DreamOverlayTouchMonitor$$ExternalSyntheticLambda0 implements Predicate {
    public final /* synthetic */ Set f$0;

    public /* synthetic */ DreamOverlayTouchMonitor$$ExternalSyntheticLambda0(Set set) {
        this.f$0 = set;
    }

    public final boolean test(Object obj) {
        return DreamOverlayTouchMonitor.lambda$isolate$4(this.f$0, (DreamOverlayTouchMonitor.TouchSessionImpl) obj);
    }
}
