package com.android.systemui.statusbar.policy;

import android.app.NotificationManager;
import com.android.systemui.statusbar.policy.ZenModeController;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ZenModeControllerImpl$$ExternalSyntheticLambda4 implements Consumer {
    public final /* synthetic */ NotificationManager.Policy f$0;

    public /* synthetic */ ZenModeControllerImpl$$ExternalSyntheticLambda4(NotificationManager.Policy policy) {
        this.f$0 = policy;
    }

    public final void accept(Object obj) {
        ((ZenModeController.Callback) obj).onConsolidatedPolicyChanged(this.f$0);
    }
}
