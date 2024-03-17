package com.android.systemui.util;

import android.telephony.CarrierConfigManager;
import com.android.systemui.broadcast.BroadcastDispatcher;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class CarrierConfigTracker_Factory implements Factory<CarrierConfigTracker> {
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<CarrierConfigManager> carrierConfigManagerProvider;

    public CarrierConfigTracker_Factory(Provider<CarrierConfigManager> provider, Provider<BroadcastDispatcher> provider2) {
        this.carrierConfigManagerProvider = provider;
        this.broadcastDispatcherProvider = provider2;
    }

    public CarrierConfigTracker get() {
        return newInstance(this.carrierConfigManagerProvider.get(), this.broadcastDispatcherProvider.get());
    }

    public static CarrierConfigTracker_Factory create(Provider<CarrierConfigManager> provider, Provider<BroadcastDispatcher> provider2) {
        return new CarrierConfigTracker_Factory(provider, provider2);
    }

    public static CarrierConfigTracker newInstance(CarrierConfigManager carrierConfigManager, BroadcastDispatcher broadcastDispatcher) {
        return new CarrierConfigTracker(carrierConfigManager, broadcastDispatcher);
    }
}
