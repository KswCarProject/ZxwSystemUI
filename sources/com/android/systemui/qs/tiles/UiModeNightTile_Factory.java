package com.android.systemui.qs.tiles;

import android.os.Handler;
import android.os.Looper;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.LocationController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class UiModeNightTile_Factory implements Factory<UiModeNightTile> {
    public final Provider<ActivityStarter> activityStarterProvider;
    public final Provider<Looper> backgroundLooperProvider;
    public final Provider<BatteryController> batteryControllerProvider;
    public final Provider<ConfigurationController> configurationControllerProvider;
    public final Provider<FalsingManager> falsingManagerProvider;
    public final Provider<QSHost> hostProvider;
    public final Provider<LocationController> locationControllerProvider;
    public final Provider<Handler> mainHandlerProvider;
    public final Provider<MetricsLogger> metricsLoggerProvider;
    public final Provider<QSLogger> qsLoggerProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;

    public UiModeNightTile_Factory(Provider<QSHost> provider, Provider<Looper> provider2, Provider<Handler> provider3, Provider<FalsingManager> provider4, Provider<MetricsLogger> provider5, Provider<StatusBarStateController> provider6, Provider<ActivityStarter> provider7, Provider<QSLogger> provider8, Provider<ConfigurationController> provider9, Provider<BatteryController> provider10, Provider<LocationController> provider11) {
        this.hostProvider = provider;
        this.backgroundLooperProvider = provider2;
        this.mainHandlerProvider = provider3;
        this.falsingManagerProvider = provider4;
        this.metricsLoggerProvider = provider5;
        this.statusBarStateControllerProvider = provider6;
        this.activityStarterProvider = provider7;
        this.qsLoggerProvider = provider8;
        this.configurationControllerProvider = provider9;
        this.batteryControllerProvider = provider10;
        this.locationControllerProvider = provider11;
    }

    public UiModeNightTile get() {
        return newInstance(this.hostProvider.get(), this.backgroundLooperProvider.get(), this.mainHandlerProvider.get(), this.falsingManagerProvider.get(), this.metricsLoggerProvider.get(), this.statusBarStateControllerProvider.get(), this.activityStarterProvider.get(), this.qsLoggerProvider.get(), this.configurationControllerProvider.get(), this.batteryControllerProvider.get(), this.locationControllerProvider.get());
    }

    public static UiModeNightTile_Factory create(Provider<QSHost> provider, Provider<Looper> provider2, Provider<Handler> provider3, Provider<FalsingManager> provider4, Provider<MetricsLogger> provider5, Provider<StatusBarStateController> provider6, Provider<ActivityStarter> provider7, Provider<QSLogger> provider8, Provider<ConfigurationController> provider9, Provider<BatteryController> provider10, Provider<LocationController> provider11) {
        return new UiModeNightTile_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11);
    }

    public static UiModeNightTile newInstance(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, ConfigurationController configurationController, BatteryController batteryController, LocationController locationController) {
        return new UiModeNightTile(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger, configurationController, batteryController, locationController);
    }
}
