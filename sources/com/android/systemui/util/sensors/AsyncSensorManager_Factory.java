package com.android.systemui.util.sensors;

import android.hardware.SensorManager;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.util.concurrency.ThreadFactory;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class AsyncSensorManager_Factory implements Factory<AsyncSensorManager> {
    public final Provider<PluginManager> pluginManagerProvider;
    public final Provider<SensorManager> sensorManagerProvider;
    public final Provider<ThreadFactory> threadFactoryProvider;

    public AsyncSensorManager_Factory(Provider<SensorManager> provider, Provider<ThreadFactory> provider2, Provider<PluginManager> provider3) {
        this.sensorManagerProvider = provider;
        this.threadFactoryProvider = provider2;
        this.pluginManagerProvider = provider3;
    }

    public AsyncSensorManager get() {
        return newInstance(this.sensorManagerProvider.get(), this.threadFactoryProvider.get(), this.pluginManagerProvider.get());
    }

    public static AsyncSensorManager_Factory create(Provider<SensorManager> provider, Provider<ThreadFactory> provider2, Provider<PluginManager> provider3) {
        return new AsyncSensorManager_Factory(provider, provider2, provider3);
    }

    public static AsyncSensorManager newInstance(SensorManager sensorManager, ThreadFactory threadFactory, PluginManager pluginManager) {
        return new AsyncSensorManager(sensorManager, threadFactory, pluginManager);
    }
}
