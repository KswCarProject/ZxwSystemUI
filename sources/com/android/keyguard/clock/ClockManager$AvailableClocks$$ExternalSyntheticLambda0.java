package com.android.keyguard.clock;

import com.android.systemui.plugins.ClockPlugin;
import java.util.function.Supplier;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ClockManager$AvailableClocks$$ExternalSyntheticLambda0 implements Supplier {
    public final /* synthetic */ ClockPlugin f$0;

    public /* synthetic */ ClockManager$AvailableClocks$$ExternalSyntheticLambda0(ClockPlugin clockPlugin) {
        this.f$0 = clockPlugin;
    }

    public final Object get() {
        return this.f$0.getTitle();
    }
}
