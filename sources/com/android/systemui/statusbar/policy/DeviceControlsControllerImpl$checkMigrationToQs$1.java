package com.android.systemui.statusbar.policy;

import com.android.systemui.controls.controller.ControlsController;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;

/* compiled from: DeviceControlsControllerImpl.kt */
public final class DeviceControlsControllerImpl$checkMigrationToQs$1<T> implements Consumer {
    public final /* synthetic */ DeviceControlsControllerImpl this$0;

    public DeviceControlsControllerImpl$checkMigrationToQs$1(DeviceControlsControllerImpl deviceControlsControllerImpl) {
        this.this$0 = deviceControlsControllerImpl;
    }

    public final void accept(@NotNull ControlsController controlsController) {
        if (!controlsController.getFavorites().isEmpty()) {
            this.this$0.setPosition$frameworks__base__packages__SystemUI__android_common__SystemUI_core(3);
            this.this$0.fireControlsUpdate();
        }
    }
}
