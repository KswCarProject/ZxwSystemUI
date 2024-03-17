package com.android.systemui.statusbar.policy;

import android.os.Handler;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.util.settings.GlobalSettings;
import com.android.systemui.util.settings.SecureSettings;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class DeviceProvisionedControllerImpl_Factory implements Factory<DeviceProvisionedControllerImpl> {
    public final Provider<Handler> backgroundHandlerProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<GlobalSettings> globalSettingsProvider;
    public final Provider<Executor> mainExecutorProvider;
    public final Provider<SecureSettings> secureSettingsProvider;
    public final Provider<UserTracker> userTrackerProvider;

    public DeviceProvisionedControllerImpl_Factory(Provider<SecureSettings> provider, Provider<GlobalSettings> provider2, Provider<UserTracker> provider3, Provider<DumpManager> provider4, Provider<Handler> provider5, Provider<Executor> provider6) {
        this.secureSettingsProvider = provider;
        this.globalSettingsProvider = provider2;
        this.userTrackerProvider = provider3;
        this.dumpManagerProvider = provider4;
        this.backgroundHandlerProvider = provider5;
        this.mainExecutorProvider = provider6;
    }

    public DeviceProvisionedControllerImpl get() {
        return newInstance(this.secureSettingsProvider.get(), this.globalSettingsProvider.get(), this.userTrackerProvider.get(), this.dumpManagerProvider.get(), this.backgroundHandlerProvider.get(), this.mainExecutorProvider.get());
    }

    public static DeviceProvisionedControllerImpl_Factory create(Provider<SecureSettings> provider, Provider<GlobalSettings> provider2, Provider<UserTracker> provider3, Provider<DumpManager> provider4, Provider<Handler> provider5, Provider<Executor> provider6) {
        return new DeviceProvisionedControllerImpl_Factory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static DeviceProvisionedControllerImpl newInstance(SecureSettings secureSettings, GlobalSettings globalSettings, UserTracker userTracker, DumpManager dumpManager, Handler handler, Executor executor) {
        return new DeviceProvisionedControllerImpl(secureSettings, globalSettings, userTracker, dumpManager, handler, executor);
    }
}
