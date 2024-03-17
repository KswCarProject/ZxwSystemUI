package com.android.systemui.qs.tiles;

import com.android.systemui.controls.ControlsServiceInfo;
import com.android.systemui.controls.management.ControlsListingController;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/* compiled from: DeviceControlsTile.kt */
public final class DeviceControlsTile$listingCallback$1 implements ControlsListingController.ControlsListingCallback {
    public final /* synthetic */ DeviceControlsTile this$0;

    public DeviceControlsTile$listingCallback$1(DeviceControlsTile deviceControlsTile) {
        this.this$0 = deviceControlsTile;
    }

    public void onServicesUpdated(@NotNull List<ControlsServiceInfo> list) {
        if (this.this$0.hasControlsApps.compareAndSet(list.isEmpty(), !list.isEmpty())) {
            this.this$0.refreshState();
        }
    }
}
