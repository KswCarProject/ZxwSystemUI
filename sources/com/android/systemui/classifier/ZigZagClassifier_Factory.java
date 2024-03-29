package com.android.systemui.classifier;

import com.android.systemui.util.DeviceConfigProxy;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ZigZagClassifier_Factory implements Factory<ZigZagClassifier> {
    public final Provider<FalsingDataProvider> dataProvider;
    public final Provider<DeviceConfigProxy> deviceConfigProxyProvider;

    public ZigZagClassifier_Factory(Provider<FalsingDataProvider> provider, Provider<DeviceConfigProxy> provider2) {
        this.dataProvider = provider;
        this.deviceConfigProxyProvider = provider2;
    }

    public ZigZagClassifier get() {
        return newInstance(this.dataProvider.get(), this.deviceConfigProxyProvider.get());
    }

    public static ZigZagClassifier_Factory create(Provider<FalsingDataProvider> provider, Provider<DeviceConfigProxy> provider2) {
        return new ZigZagClassifier_Factory(provider, provider2);
    }

    public static ZigZagClassifier newInstance(FalsingDataProvider falsingDataProvider, DeviceConfigProxy deviceConfigProxy) {
        return new ZigZagClassifier(falsingDataProvider, deviceConfigProxy);
    }
}
