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
import com.android.systemui.util.settings.SecureSettings;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class BatterySaverTile_Factory implements Factory<BatterySaverTile> {
    public final Provider<ActivityStarter> activityStarterProvider;
    public final Provider<Looper> backgroundLooperProvider;
    public final Provider<BatteryController> batteryControllerProvider;
    public final Provider<FalsingManager> falsingManagerProvider;
    public final Provider<QSHost> hostProvider;
    public final Provider<Handler> mainHandlerProvider;
    public final Provider<MetricsLogger> metricsLoggerProvider;
    public final Provider<QSLogger> qsLoggerProvider;
    public final Provider<SecureSettings> secureSettingsProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;

    public BatterySaverTile_Factory(Provider<QSHost> provider, Provider<Looper> provider2, Provider<Handler> provider3, Provider<FalsingManager> provider4, Provider<MetricsLogger> provider5, Provider<StatusBarStateController> provider6, Provider<ActivityStarter> provider7, Provider<QSLogger> provider8, Provider<BatteryController> provider9, Provider<SecureSettings> provider10) {
        this.hostProvider = provider;
        this.backgroundLooperProvider = provider2;
        this.mainHandlerProvider = provider3;
        this.falsingManagerProvider = provider4;
        this.metricsLoggerProvider = provider5;
        this.statusBarStateControllerProvider = provider6;
        this.activityStarterProvider = provider7;
        this.qsLoggerProvider = provider8;
        this.batteryControllerProvider = provider9;
        this.secureSettingsProvider = provider10;
    }

    public BatterySaverTile get() {
        return newInstance(this.hostProvider.get(), this.backgroundLooperProvider.get(), this.mainHandlerProvider.get(), this.falsingManagerProvider.get(), this.metricsLoggerProvider.get(), this.statusBarStateControllerProvider.get(), this.activityStarterProvider.get(), this.qsLoggerProvider.get(), this.batteryControllerProvider.get(), this.secureSettingsProvider.get());
    }

    public static BatterySaverTile_Factory create(Provider<QSHost> provider, Provider<Looper> provider2, Provider<Handler> provider3, Provider<FalsingManager> provider4, Provider<MetricsLogger> provider5, Provider<StatusBarStateController> provider6, Provider<ActivityStarter> provider7, Provider<QSLogger> provider8, Provider<BatteryController> provider9, Provider<SecureSettings> provider10) {
        return new BatterySaverTile_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10);
    }

    public static BatterySaverTile newInstance(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, BatteryController batteryController, SecureSettings secureSettings) {
        return new BatterySaverTile(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger, batteryController, secureSettings);
    }
}
