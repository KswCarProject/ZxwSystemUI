package com.android.systemui.dreams.touch;

import androidx.concurrent.futures.CallbackToFutureAdapter;
import com.android.systemui.dreams.touch.DreamOverlayTouchMonitor;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DreamOverlayTouchMonitor$$ExternalSyntheticLambda2 implements CallbackToFutureAdapter.Resolver {
    public final /* synthetic */ DreamOverlayTouchMonitor f$0;
    public final /* synthetic */ DreamOverlayTouchMonitor.TouchSessionImpl f$1;

    public /* synthetic */ DreamOverlayTouchMonitor$$ExternalSyntheticLambda2(DreamOverlayTouchMonitor dreamOverlayTouchMonitor, DreamOverlayTouchMonitor.TouchSessionImpl touchSessionImpl) {
        this.f$0 = dreamOverlayTouchMonitor;
        this.f$1 = touchSessionImpl;
    }

    public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) {
        return this.f$0.lambda$pop$3(this.f$1, completer);
    }
}
