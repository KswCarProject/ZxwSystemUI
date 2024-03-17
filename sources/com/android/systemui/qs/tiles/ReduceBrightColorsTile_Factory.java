package com.android.systemui.qs.tiles;

import android.os.Handler;
import android.os.Looper;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.ReduceBrightColorsController;
import com.android.systemui.qs.logging.QSLogger;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ReduceBrightColorsTile_Factory implements Factory<ReduceBrightColorsTile> {
    public final Provider<ActivityStarter> activityStarterProvider;
    public final Provider<Looper> backgroundLooperProvider;
    public final Provider<FalsingManager> falsingManagerProvider;
    public final Provider<QSHost> hostProvider;
    public final Provider<Boolean> isAvailableProvider;
    public final Provider<Handler> mainHandlerProvider;
    public final Provider<MetricsLogger> metricsLoggerProvider;
    public final Provider<QSLogger> qsLoggerProvider;
    public final Provider<ReduceBrightColorsController> reduceBrightColorsControllerProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;

    public ReduceBrightColorsTile_Factory(Provider<Boolean> provider, Provider<ReduceBrightColorsController> provider2, Provider<QSHost> provider3, Provider<Looper> provider4, Provider<Handler> provider5, Provider<FalsingManager> provider6, Provider<MetricsLogger> provider7, Provider<StatusBarStateController> provider8, Provider<ActivityStarter> provider9, Provider<QSLogger> provider10) {
        this.isAvailableProvider = provider;
        this.reduceBrightColorsControllerProvider = provider2;
        this.hostProvider = provider3;
        this.backgroundLooperProvider = provider4;
        this.mainHandlerProvider = provider5;
        this.falsingManagerProvider = provider6;
        this.metricsLoggerProvider = provider7;
        this.statusBarStateControllerProvider = provider8;
        this.activityStarterProvider = provider9;
        this.qsLoggerProvider = provider10;
    }

    public ReduceBrightColorsTile get() {
        return newInstance(this.isAvailableProvider.get().booleanValue(), this.reduceBrightColorsControllerProvider.get(), this.hostProvider.get(), this.backgroundLooperProvider.get(), this.mainHandlerProvider.get(), this.falsingManagerProvider.get(), this.metricsLoggerProvider.get(), this.statusBarStateControllerProvider.get(), this.activityStarterProvider.get(), this.qsLoggerProvider.get());
    }

    public static ReduceBrightColorsTile_Factory create(Provider<Boolean> provider, Provider<ReduceBrightColorsController> provider2, Provider<QSHost> provider3, Provider<Looper> provider4, Provider<Handler> provider5, Provider<FalsingManager> provider6, Provider<MetricsLogger> provider7, Provider<StatusBarStateController> provider8, Provider<ActivityStarter> provider9, Provider<QSLogger> provider10) {
        return new ReduceBrightColorsTile_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10);
    }

    public static ReduceBrightColorsTile newInstance(boolean z, ReduceBrightColorsController reduceBrightColorsController, QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger) {
        return new ReduceBrightColorsTile(z, reduceBrightColorsController, qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
    }
}
