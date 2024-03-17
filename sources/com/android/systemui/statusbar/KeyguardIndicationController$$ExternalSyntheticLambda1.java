package com.android.systemui.statusbar;

import java.util.function.Supplier;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class KeyguardIndicationController$$ExternalSyntheticLambda1 implements Supplier {
    public final /* synthetic */ KeyguardIndicationController f$0;

    public /* synthetic */ KeyguardIndicationController$$ExternalSyntheticLambda1(KeyguardIndicationController keyguardIndicationController) {
        this.f$0 = keyguardIndicationController;
    }

    public final Object get() {
        return Boolean.valueOf(this.f$0.isOrganizationOwnedDevice());
    }
}
