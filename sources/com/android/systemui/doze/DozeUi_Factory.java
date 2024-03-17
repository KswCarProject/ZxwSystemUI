package com.android.systemui.doze;

import android.app.AlarmManager;
import android.content.Context;
import android.os.Handler;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.util.wakelock.WakeLock;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class DozeUi_Factory implements Factory<DozeUi> {
    public final Provider<AlarmManager> alarmManagerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DozeLog> dozeLogProvider;
    public final Provider<Handler> handlerProvider;
    public final Provider<DozeHost> hostProvider;
    public final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    public final Provider<DozeParameters> paramsProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;
    public final Provider<WakeLock> wakeLockProvider;

    public DozeUi_Factory(Provider<Context> provider, Provider<AlarmManager> provider2, Provider<WakeLock> provider3, Provider<DozeHost> provider4, Provider<Handler> provider5, Provider<DozeParameters> provider6, Provider<KeyguardUpdateMonitor> provider7, Provider<StatusBarStateController> provider8, Provider<DozeLog> provider9) {
        this.contextProvider = provider;
        this.alarmManagerProvider = provider2;
        this.wakeLockProvider = provider3;
        this.hostProvider = provider4;
        this.handlerProvider = provider5;
        this.paramsProvider = provider6;
        this.keyguardUpdateMonitorProvider = provider7;
        this.statusBarStateControllerProvider = provider8;
        this.dozeLogProvider = provider9;
    }

    public DozeUi get() {
        return newInstance(this.contextProvider.get(), this.alarmManagerProvider.get(), this.wakeLockProvider.get(), this.hostProvider.get(), this.handlerProvider.get(), this.paramsProvider.get(), this.keyguardUpdateMonitorProvider.get(), this.statusBarStateControllerProvider.get(), this.dozeLogProvider.get());
    }

    public static DozeUi_Factory create(Provider<Context> provider, Provider<AlarmManager> provider2, Provider<WakeLock> provider3, Provider<DozeHost> provider4, Provider<Handler> provider5, Provider<DozeParameters> provider6, Provider<KeyguardUpdateMonitor> provider7, Provider<StatusBarStateController> provider8, Provider<DozeLog> provider9) {
        return new DozeUi_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9);
    }

    public static DozeUi newInstance(Context context, AlarmManager alarmManager, WakeLock wakeLock, DozeHost dozeHost, Handler handler, DozeParameters dozeParameters, KeyguardUpdateMonitor keyguardUpdateMonitor, StatusBarStateController statusBarStateController, DozeLog dozeLog) {
        return new DozeUi(context, alarmManager, wakeLock, dozeHost, handler, dozeParameters, keyguardUpdateMonitor, statusBarStateController, dozeLog);
    }
}
