package com.android.systemui.qs;

import android.provider.DeviceConfig;

/* compiled from: FgsManagerController.kt */
public final class FgsManagerController$init$1$2 implements DeviceConfig.OnPropertiesChangedListener {
    public final /* synthetic */ FgsManagerController this$0;

    public FgsManagerController$init$1$2(FgsManagerController fgsManagerController) {
        this.this$0 = fgsManagerController;
    }

    public final void onPropertiesChanged(DeviceConfig.Properties properties) {
        FgsManagerController fgsManagerController = this.this$0;
        fgsManagerController.isAvailable = properties.getBoolean("task_manager_enabled", fgsManagerController.isAvailable());
        FgsManagerController fgsManagerController2 = this.this$0;
        fgsManagerController2.showFooterDot = properties.getBoolean("task_manager_show_footer_dot", fgsManagerController2.getShowFooterDot());
    }
}
