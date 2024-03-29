package com.android.systemui.controls.controller;

import com.android.systemui.controls.ControlsServiceInfo;
import com.android.systemui.controls.management.ControlsListingController;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/* compiled from: ControlsControllerImpl.kt */
public final class ControlsControllerImpl$listingCallback$1 implements ControlsListingController.ControlsListingCallback {
    public final /* synthetic */ ControlsControllerImpl this$0;

    public ControlsControllerImpl$listingCallback$1(ControlsControllerImpl controlsControllerImpl) {
        this.this$0 = controlsControllerImpl;
    }

    public void onServicesUpdated(@NotNull List<ControlsServiceInfo> list) {
        this.this$0.executor.execute(new ControlsControllerImpl$listingCallback$1$onServicesUpdated$1(list, this.this$0));
    }
}
