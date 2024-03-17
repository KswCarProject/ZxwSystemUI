package com.google.android.setupcompat.internal;

import com.google.android.setupcompat.internal.ClockProvider;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ClockProvider$$ExternalSyntheticLambda1 implements Ticker {
    public final /* synthetic */ ClockProvider.Supplier f$0;

    public /* synthetic */ ClockProvider$$ExternalSyntheticLambda1(ClockProvider.Supplier supplier) {
        this.f$0 = supplier;
    }

    public final long read() {
        return ((Long) this.f$0.get()).longValue();
    }
}
