package com.android.systemui.statusbar.notification.collection.coordinator;

import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class KeyguardCoordinator$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ KeyguardCoordinator f$0;

    public /* synthetic */ KeyguardCoordinator$$ExternalSyntheticLambda0(KeyguardCoordinator keyguardCoordinator) {
        this.f$0 = keyguardCoordinator;
    }

    public final void accept(Object obj) {
        this.f$0.invalidateListFromFilter((String) obj);
    }
}
