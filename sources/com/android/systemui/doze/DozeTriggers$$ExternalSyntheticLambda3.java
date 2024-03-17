package com.android.systemui.doze;

import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DozeTriggers$$ExternalSyntheticLambda3 implements Consumer {
    public final /* synthetic */ DozeTriggers f$0;

    public /* synthetic */ DozeTriggers$$ExternalSyntheticLambda3(DozeTriggers dozeTriggers) {
        this.f$0 = dozeTriggers;
    }

    public final void accept(Object obj) {
        this.f$0.onProximityFar(((Boolean) obj).booleanValue());
    }
}
