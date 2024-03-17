package com.android.systemui.statusbar.policy.dagger;

import android.content.Context;
import android.content.res.Resources;
import android.os.UserManager;
import com.android.settingslib.devicestate.DeviceStateRotationLockSettingsManager;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.connectivity.AccessPointControllerImpl;
import com.android.systemui.statusbar.connectivity.NetworkController;
import com.android.systemui.statusbar.policy.DataSaverController;
import java.util.concurrent.Executor;

public interface StatusBarPolicyModule {
    static AccessPointControllerImpl provideAccessPointControllerImpl(UserManager userManager, UserTracker userTracker, Executor executor, AccessPointControllerImpl.WifiPickerTrackerFactory wifiPickerTrackerFactory) {
        AccessPointControllerImpl accessPointControllerImpl = new AccessPointControllerImpl(userManager, userTracker, executor, wifiPickerTrackerFactory);
        accessPointControllerImpl.init();
        return accessPointControllerImpl;
    }

    static DeviceStateRotationLockSettingsManager provideAutoRotateSettingsManager(Context context) {
        return DeviceStateRotationLockSettingsManager.getInstance(context);
    }

    static String[] providesDeviceStateRotationLockDefaults(Resources resources) {
        return resources.getStringArray(17236105);
    }

    static DataSaverController provideDataSaverController(NetworkController networkController) {
        return networkController.getDataSaverController();
    }
}
