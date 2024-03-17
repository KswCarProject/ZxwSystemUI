package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.content.pm.UserInfo;
import com.android.systemui.settings.UserTracker;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/* compiled from: DeviceProvisionedControllerImpl.kt */
public final class DeviceProvisionedControllerImpl$userChangedCallback$1 implements UserTracker.Callback {
    public final /* synthetic */ DeviceProvisionedControllerImpl this$0;

    public void onProfilesChanged(@NotNull List<? extends UserInfo> list) {
    }

    public DeviceProvisionedControllerImpl$userChangedCallback$1(DeviceProvisionedControllerImpl deviceProvisionedControllerImpl) {
        this.this$0 = deviceProvisionedControllerImpl;
    }

    public void onUserChanged(int i, @NotNull Context context) {
        this.this$0.updateValues(false, i);
        this.this$0.onUserSwitched();
    }
}
