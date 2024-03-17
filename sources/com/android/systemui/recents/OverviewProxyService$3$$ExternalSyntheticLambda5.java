package com.android.systemui.recents;

import android.os.Bundle;
import com.android.wm.shell.back.BackAnimation;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class OverviewProxyService$3$$ExternalSyntheticLambda5 implements Consumer {
    public final /* synthetic */ Bundle f$0;

    public /* synthetic */ OverviewProxyService$3$$ExternalSyntheticLambda5(Bundle bundle) {
        this.f$0 = bundle;
    }

    public final void accept(Object obj) {
        this.f$0.putBinder("extra_shell_back_animation", ((BackAnimation) obj).createExternalInterface().asBinder());
    }
}
