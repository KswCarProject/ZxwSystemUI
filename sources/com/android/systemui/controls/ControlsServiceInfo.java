package com.android.systemui.controls;

import android.content.Context;
import android.content.pm.ServiceInfo;
import com.android.settingslib.applications.DefaultAppInfo;
import org.jetbrains.annotations.NotNull;

/* compiled from: ControlsServiceInfo.kt */
public final class ControlsServiceInfo extends DefaultAppInfo {
    @NotNull
    public final ServiceInfo serviceInfo;

    @NotNull
    public final ServiceInfo getServiceInfo() {
        return this.serviceInfo;
    }

    public ControlsServiceInfo(@NotNull Context context, @NotNull ServiceInfo serviceInfo2) {
        super(context, context.getPackageManager(), context.getUserId(), serviceInfo2.getComponentName());
        this.serviceInfo = serviceInfo2;
    }
}
