package com.android.wm.shell.legacysplitscreen;

import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DividerView$3$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ Consumer f$0;
    public final /* synthetic */ Boolean f$1;

    public /* synthetic */ DividerView$3$$ExternalSyntheticLambda0(Consumer consumer, Boolean bool) {
        this.f$0 = consumer;
        this.f$1 = bool;
    }

    public final void run() {
        this.f$0.accept(this.f$1);
    }
}
