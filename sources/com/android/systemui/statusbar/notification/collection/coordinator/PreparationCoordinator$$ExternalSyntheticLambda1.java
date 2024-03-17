package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class PreparationCoordinator$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ NotifFilter f$0;

    public /* synthetic */ PreparationCoordinator$$ExternalSyntheticLambda1(NotifFilter notifFilter) {
        this.f$0 = notifFilter;
    }

    public final void run() {
        this.f$0.invalidateList();
    }
}
