package com.android.systemui.statusbar.policy;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;

/* compiled from: DeviceProvisionedControllerImpl.kt */
public final class DeviceProvisionedControllerImpl$observer$1 extends ContentObserver {
    public final /* synthetic */ DeviceProvisionedControllerImpl this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public DeviceProvisionedControllerImpl$observer$1(DeviceProvisionedControllerImpl deviceProvisionedControllerImpl, Handler handler) {
        super(handler);
        this.this$0 = deviceProvisionedControllerImpl;
    }

    public void onChange(boolean z, @NotNull Collection<Uri> collection, int i, int i2) {
        boolean contains = collection.contains(this.this$0.deviceProvisionedUri);
        if (!collection.contains(this.this$0.userSetupUri)) {
            i2 = -2;
        }
        this.this$0.updateValues(contains, i2);
        if (contains) {
            this.this$0.onDeviceProvisionedChanged();
        }
        if (i2 != -2) {
            this.this$0.onUserSetupChanged();
        }
    }
}
