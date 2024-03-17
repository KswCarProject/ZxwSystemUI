package com.android.systemui.util.leak;

import com.android.systemui.util.concurrency.MessageRouter;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class GarbageMonitor$$ExternalSyntheticLambda0 implements MessageRouter.SimpleMessageListener {
    public final /* synthetic */ GarbageMonitor f$0;

    public /* synthetic */ GarbageMonitor$$ExternalSyntheticLambda0(GarbageMonitor garbageMonitor) {
        this.f$0 = garbageMonitor;
    }

    public final void onMessage(int i) {
        this.f$0.doGarbageInspection(i);
    }
}
