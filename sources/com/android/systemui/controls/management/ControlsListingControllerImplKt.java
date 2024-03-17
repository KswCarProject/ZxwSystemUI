package com.android.systemui.controls.management;

import android.content.Context;
import com.android.settingslib.applications.ServiceListing;

/* compiled from: ControlsListingControllerImpl.kt */
public final class ControlsListingControllerImplKt {
    public static final ServiceListing createServiceListing(Context context) {
        ServiceListing.Builder builder = new ServiceListing.Builder(context);
        builder.setIntentAction("android.service.controls.ControlsProviderService");
        builder.setPermission("android.permission.BIND_CONTROLS");
        builder.setNoun("Controls Provider");
        builder.setSetting("controls_providers");
        builder.setTag("controls_providers");
        builder.setAddDeviceLockedFlags(true);
        return builder.build();
    }
}
