package com.android.systemui.qs.tiles;

import android.provider.DeviceConfig;
import java.util.function.Supplier;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class CameraToggleTile$$ExternalSyntheticLambda0 implements Supplier {
    public final Object get() {
        return Boolean.valueOf(DeviceConfig.getBoolean("privacy", "camera_toggle_enabled", true));
    }
}
