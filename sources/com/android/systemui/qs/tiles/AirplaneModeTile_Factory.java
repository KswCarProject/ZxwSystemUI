package com.android.systemui.qs.tiles;

import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Looper;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.util.settings.GlobalSettings;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class AirplaneModeTile_Factory implements Factory<AirplaneModeTile> {
    public final Provider<ActivityStarter> activityStarterProvider;
    public final Provider<Looper> backgroundLooperProvider;
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<ConnectivityManager> connectivityManagerProvider;
    public final Provider<FalsingManager> falsingManagerProvider;
    public final Provider<GlobalSettings> globalSettingsProvider;
    public final Provider<QSHost> hostProvider;
    public final Provider<Handler> mainHandlerProvider;
    public final Provider<MetricsLogger> metricsLoggerProvider;
    public final Provider<QSLogger> qsLoggerProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;

    public AirplaneModeTile_Factory(Provider<QSHost> provider, Provider<Looper> provider2, Provider<Handler> provider3, Provider<FalsingManager> provider4, Provider<MetricsLogger> provider5, Provider<StatusBarStateController> provider6, Provider<ActivityStarter> provider7, Provider<QSLogger> provider8, Provider<BroadcastDispatcher> provider9, Provider<ConnectivityManager> provider10, Provider<GlobalSettings> provider11) {
        this.hostProvider = provider;
        this.backgroundLooperProvider = provider2;
        this.mainHandlerProvider = provider3;
        this.falsingManagerProvider = provider4;
        this.metricsLoggerProvider = provider5;
        this.statusBarStateControllerProvider = provider6;
        this.activityStarterProvider = provider7;
        this.qsLoggerProvider = provider8;
        this.broadcastDispatcherProvider = provider9;
        this.connectivityManagerProvider = provider10;
        this.globalSettingsProvider = provider11;
    }

    public AirplaneModeTile get() {
        return newInstance(this.hostProvider.get(), this.backgroundLooperProvider.get(), this.mainHandlerProvider.get(), this.falsingManagerProvider.get(), this.metricsLoggerProvider.get(), this.statusBarStateControllerProvider.get(), this.activityStarterProvider.get(), this.qsLoggerProvider.get(), this.broadcastDispatcherProvider.get(), DoubleCheck.lazy(this.connectivityManagerProvider), this.globalSettingsProvider.get());
    }

    public static AirplaneModeTile_Factory create(Provider<QSHost> provider, Provider<Looper> provider2, Provider<Handler> provider3, Provider<FalsingManager> provider4, Provider<MetricsLogger> provider5, Provider<StatusBarStateController> provider6, Provider<ActivityStarter> provider7, Provider<QSLogger> provider8, Provider<BroadcastDispatcher> provider9, Provider<ConnectivityManager> provider10, Provider<GlobalSettings> provider11) {
        return new AirplaneModeTile_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11);
    }

    public static AirplaneModeTile newInstance(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, BroadcastDispatcher broadcastDispatcher, Lazy<ConnectivityManager> lazy, GlobalSettings globalSettings) {
        return new AirplaneModeTile(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger, broadcastDispatcher, lazy, globalSettings);
    }
}
