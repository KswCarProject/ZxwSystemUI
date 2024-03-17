package com.android.systemui.statusbar.phone;

import android.content.Context;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.util.sensors.AsyncSensorManager;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class KeyguardLiftController_Factory implements Factory<KeyguardLiftController> {
    public final Provider<AsyncSensorManager> asyncSensorManagerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;

    public KeyguardLiftController_Factory(Provider<Context> provider, Provider<StatusBarStateController> provider2, Provider<AsyncSensorManager> provider3, Provider<KeyguardUpdateMonitor> provider4, Provider<DumpManager> provider5) {
        this.contextProvider = provider;
        this.statusBarStateControllerProvider = provider2;
        this.asyncSensorManagerProvider = provider3;
        this.keyguardUpdateMonitorProvider = provider4;
        this.dumpManagerProvider = provider5;
    }

    public KeyguardLiftController get() {
        return newInstance(this.contextProvider.get(), this.statusBarStateControllerProvider.get(), this.asyncSensorManagerProvider.get(), this.keyguardUpdateMonitorProvider.get(), this.dumpManagerProvider.get());
    }

    public static KeyguardLiftController_Factory create(Provider<Context> provider, Provider<StatusBarStateController> provider2, Provider<AsyncSensorManager> provider3, Provider<KeyguardUpdateMonitor> provider4, Provider<DumpManager> provider5) {
        return new KeyguardLiftController_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static KeyguardLiftController newInstance(Context context, StatusBarStateController statusBarStateController, AsyncSensorManager asyncSensorManager, KeyguardUpdateMonitor keyguardUpdateMonitor, DumpManager dumpManager) {
        return new KeyguardLiftController(context, statusBarStateController, asyncSensorManager, keyguardUpdateMonitor, dumpManager);
    }
}
