package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.BootCompleteCache;
import com.android.systemui.appops.AppOpsController;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.systemui.util.settings.SecureSettings;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class LocationControllerImpl_Factory implements Factory<LocationControllerImpl> {
    public final Provider<AppOpsController> appOpsControllerProvider;
    public final Provider<Handler> backgroundHandlerProvider;
    public final Provider<BootCompleteCache> bootCompleteCacheProvider;
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DeviceConfigProxy> deviceConfigProxyProvider;
    public final Provider<Looper> mainLooperProvider;
    public final Provider<PackageManager> packageManagerProvider;
    public final Provider<SecureSettings> secureSettingsProvider;
    public final Provider<UiEventLogger> uiEventLoggerProvider;
    public final Provider<UserTracker> userTrackerProvider;

    public LocationControllerImpl_Factory(Provider<Context> provider, Provider<AppOpsController> provider2, Provider<DeviceConfigProxy> provider3, Provider<Looper> provider4, Provider<Handler> provider5, Provider<BroadcastDispatcher> provider6, Provider<BootCompleteCache> provider7, Provider<UserTracker> provider8, Provider<PackageManager> provider9, Provider<UiEventLogger> provider10, Provider<SecureSettings> provider11) {
        this.contextProvider = provider;
        this.appOpsControllerProvider = provider2;
        this.deviceConfigProxyProvider = provider3;
        this.mainLooperProvider = provider4;
        this.backgroundHandlerProvider = provider5;
        this.broadcastDispatcherProvider = provider6;
        this.bootCompleteCacheProvider = provider7;
        this.userTrackerProvider = provider8;
        this.packageManagerProvider = provider9;
        this.uiEventLoggerProvider = provider10;
        this.secureSettingsProvider = provider11;
    }

    public LocationControllerImpl get() {
        return newInstance(this.contextProvider.get(), this.appOpsControllerProvider.get(), this.deviceConfigProxyProvider.get(), this.mainLooperProvider.get(), this.backgroundHandlerProvider.get(), this.broadcastDispatcherProvider.get(), this.bootCompleteCacheProvider.get(), this.userTrackerProvider.get(), this.packageManagerProvider.get(), this.uiEventLoggerProvider.get(), this.secureSettingsProvider.get());
    }

    public static LocationControllerImpl_Factory create(Provider<Context> provider, Provider<AppOpsController> provider2, Provider<DeviceConfigProxy> provider3, Provider<Looper> provider4, Provider<Handler> provider5, Provider<BroadcastDispatcher> provider6, Provider<BootCompleteCache> provider7, Provider<UserTracker> provider8, Provider<PackageManager> provider9, Provider<UiEventLogger> provider10, Provider<SecureSettings> provider11) {
        return new LocationControllerImpl_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11);
    }

    public static LocationControllerImpl newInstance(Context context, AppOpsController appOpsController, DeviceConfigProxy deviceConfigProxy, Looper looper, Handler handler, BroadcastDispatcher broadcastDispatcher, BootCompleteCache bootCompleteCache, UserTracker userTracker, PackageManager packageManager, UiEventLogger uiEventLogger, SecureSettings secureSettings) {
        return new LocationControllerImpl(context, appOpsController, deviceConfigProxy, looper, handler, broadcastDispatcher, bootCompleteCache, userTracker, packageManager, uiEventLogger, secureSettings);
    }
}
