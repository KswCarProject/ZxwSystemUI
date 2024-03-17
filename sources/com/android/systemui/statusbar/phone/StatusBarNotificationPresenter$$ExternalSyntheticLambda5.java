package com.android.systemui.statusbar.phone;

import com.android.systemui.statusbar.policy.KeyguardStateController;
import java.util.function.BooleanSupplier;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class StatusBarNotificationPresenter$$ExternalSyntheticLambda5 implements BooleanSupplier {
    public final /* synthetic */ KeyguardStateController f$0;

    public /* synthetic */ StatusBarNotificationPresenter$$ExternalSyntheticLambda5(KeyguardStateController keyguardStateController) {
        this.f$0 = keyguardStateController;
    }

    public final boolean getAsBoolean() {
        return this.f$0.canDismissLockScreen();
    }
}
