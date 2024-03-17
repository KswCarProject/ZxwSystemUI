package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.listbuilder.OnBeforeFinalizeFilterListener;
import java.util.List;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class PreparationCoordinator$$ExternalSyntheticLambda2 implements OnBeforeFinalizeFilterListener {
    public final /* synthetic */ PreparationCoordinator f$0;

    public /* synthetic */ PreparationCoordinator$$ExternalSyntheticLambda2(PreparationCoordinator preparationCoordinator) {
        this.f$0 = preparationCoordinator;
    }

    public final void onBeforeFinalizeFilter(List list) {
        this.f$0.inflateAllRequiredViews(list);
    }
}
