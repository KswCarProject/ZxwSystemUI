package com.android.systemui.util.sensors;

import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.concurrency.Execution;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ProximitySensorImpl_Factory implements Factory<ProximitySensorImpl> {
    public final Provider<DelayableExecutor> delayableExecutorProvider;
    public final Provider<Execution> executionProvider;
    public final Provider<ThresholdSensor> primaryProvider;
    public final Provider<ThresholdSensor> secondaryProvider;

    public ProximitySensorImpl_Factory(Provider<ThresholdSensor> provider, Provider<ThresholdSensor> provider2, Provider<DelayableExecutor> provider3, Provider<Execution> provider4) {
        this.primaryProvider = provider;
        this.secondaryProvider = provider2;
        this.delayableExecutorProvider = provider3;
        this.executionProvider = provider4;
    }

    public ProximitySensorImpl get() {
        return newInstance(this.primaryProvider.get(), this.secondaryProvider.get(), this.delayableExecutorProvider.get(), this.executionProvider.get());
    }

    public static ProximitySensorImpl_Factory create(Provider<ThresholdSensor> provider, Provider<ThresholdSensor> provider2, Provider<DelayableExecutor> provider3, Provider<Execution> provider4) {
        return new ProximitySensorImpl_Factory(provider, provider2, provider3, provider4);
    }

    public static ProximitySensorImpl newInstance(ThresholdSensor thresholdSensor, ThresholdSensor thresholdSensor2, DelayableExecutor delayableExecutor, Execution execution) {
        return new ProximitySensorImpl(thresholdSensor, thresholdSensor2, delayableExecutor, execution);
    }
}
