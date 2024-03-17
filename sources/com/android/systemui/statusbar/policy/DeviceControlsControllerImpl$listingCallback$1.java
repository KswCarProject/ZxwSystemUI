package com.android.systemui.statusbar.policy;

import com.android.systemui.controls.ControlsServiceInfo;
import com.android.systemui.controls.management.ControlsListingController;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/* compiled from: DeviceControlsControllerImpl.kt */
public final class DeviceControlsControllerImpl$listingCallback$1 implements ControlsListingController.ControlsListingCallback {
    public final /* synthetic */ DeviceControlsControllerImpl this$0;

    public DeviceControlsControllerImpl$listingCallback$1(DeviceControlsControllerImpl deviceControlsControllerImpl) {
        this.this$0 = deviceControlsControllerImpl;
    }

    public void onServicesUpdated(@NotNull List<ControlsServiceInfo> list) {
        if (!list.isEmpty()) {
            this.this$0.seedFavorites(list);
        }
    }
}
