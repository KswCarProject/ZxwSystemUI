package com.android.wm.shell.pip.phone;

import com.android.wm.shell.common.SingleInstanceRemoteListener;
import com.android.wm.shell.pip.IPipAnimationListener;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class PipController$IPipImpl$1$$ExternalSyntheticLambda2 implements SingleInstanceRemoteListener.RemoteCall {
    public final /* synthetic */ int f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ PipController$IPipImpl$1$$ExternalSyntheticLambda2(int i, int i2) {
        this.f$0 = i;
        this.f$1 = i2;
    }

    public final void accept(Object obj) {
        ((IPipAnimationListener) obj).onPipResourceDimensionsChanged(this.f$0, this.f$1);
    }
}
