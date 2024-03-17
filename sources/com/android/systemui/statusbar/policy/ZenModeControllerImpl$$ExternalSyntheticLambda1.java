package com.android.systemui.statusbar.policy;

import android.service.notification.ZenModeConfig;
import com.android.systemui.statusbar.policy.ZenModeController;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ZenModeControllerImpl$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ ZenModeConfig.ZenRule f$0;

    public /* synthetic */ ZenModeControllerImpl$$ExternalSyntheticLambda1(ZenModeConfig.ZenRule zenRule) {
        this.f$0 = zenRule;
    }

    public final void accept(Object obj) {
        ((ZenModeController.Callback) obj).onManualRuleChanged(this.f$0);
    }
}
