package com.android.systemui.keyguard;

import com.android.systemui.keyguard.WakefulnessLifecycle;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class WakefulnessLifecycle$$ExternalSyntheticLambda1 implements Consumer {
    public final void accept(Object obj) {
        ((WakefulnessLifecycle.Observer) obj).onFinishedGoingToSleep();
    }
}
