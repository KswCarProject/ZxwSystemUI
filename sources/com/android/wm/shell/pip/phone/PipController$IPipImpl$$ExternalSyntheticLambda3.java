package com.android.wm.shell.pip.phone;

import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class PipController$IPipImpl$$ExternalSyntheticLambda3 implements Consumer {
    public final /* synthetic */ boolean f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ PipController$IPipImpl$$ExternalSyntheticLambda3(boolean z, int i) {
        this.f$0 = z;
        this.f$1 = i;
    }

    public final void accept(Object obj) {
        ((PipController) obj).setShelfHeight(this.f$0, this.f$1);
    }
}
