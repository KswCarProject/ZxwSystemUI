package com.android.systemui.dreams.touch;

import com.android.systemui.dreams.touch.DreamTouchHandler;
import com.google.common.util.concurrent.ListenableFuture;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class HideComplicationTouchHandler$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ HideComplicationTouchHandler f$0;
    public final /* synthetic */ ListenableFuture f$1;
    public final /* synthetic */ DreamTouchHandler.TouchSession f$2;

    public /* synthetic */ HideComplicationTouchHandler$$ExternalSyntheticLambda1(HideComplicationTouchHandler hideComplicationTouchHandler, ListenableFuture listenableFuture, DreamTouchHandler.TouchSession touchSession) {
        this.f$0 = hideComplicationTouchHandler;
        this.f$1 = listenableFuture;
        this.f$2 = touchSession;
    }

    public final void run() {
        this.f$0.lambda$onSessionStart$0(this.f$1, this.f$2);
    }
}
