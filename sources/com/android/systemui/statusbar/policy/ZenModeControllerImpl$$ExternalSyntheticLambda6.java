package com.android.systemui.statusbar.policy;

import com.android.systemui.statusbar.policy.ZenModeController;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ZenModeControllerImpl$$ExternalSyntheticLambda6 implements Consumer {
    public final /* synthetic */ int f$0;

    public /* synthetic */ ZenModeControllerImpl$$ExternalSyntheticLambda6(int i) {
        this.f$0 = i;
    }

    public final void accept(Object obj) {
        ((ZenModeController.Callback) obj).onZenChanged(this.f$0);
    }
}
