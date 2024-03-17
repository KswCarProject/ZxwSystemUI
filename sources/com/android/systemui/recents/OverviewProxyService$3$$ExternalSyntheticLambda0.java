package com.android.systemui.recents;

import android.os.Bundle;
import com.android.wm.shell.pip.Pip;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class OverviewProxyService$3$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ Bundle f$0;

    public /* synthetic */ OverviewProxyService$3$$ExternalSyntheticLambda0(Bundle bundle) {
        this.f$0 = bundle;
    }

    public final void accept(Object obj) {
        this.f$0.putBinder("extra_shell_pip", ((Pip) obj).createExternalInterface().asBinder());
    }
}
