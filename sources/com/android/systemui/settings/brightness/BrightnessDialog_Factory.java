package com.android.systemui.settings.brightness;

import android.os.Handler;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.settings.brightness.BrightnessSliderController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class BrightnessDialog_Factory implements Factory<BrightnessDialog> {
    public final Provider<Handler> bgHandlerProvider;
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<BrightnessSliderController.Factory> factoryProvider;

    public BrightnessDialog_Factory(Provider<BroadcastDispatcher> provider, Provider<BrightnessSliderController.Factory> provider2, Provider<Handler> provider3) {
        this.broadcastDispatcherProvider = provider;
        this.factoryProvider = provider2;
        this.bgHandlerProvider = provider3;
    }

    public BrightnessDialog get() {
        return newInstance(this.broadcastDispatcherProvider.get(), this.factoryProvider.get(), this.bgHandlerProvider.get());
    }

    public static BrightnessDialog_Factory create(Provider<BroadcastDispatcher> provider, Provider<BrightnessSliderController.Factory> provider2, Provider<Handler> provider3) {
        return new BrightnessDialog_Factory(provider, provider2, provider3);
    }

    public static BrightnessDialog newInstance(BroadcastDispatcher broadcastDispatcher, BrightnessSliderController.Factory factory, Handler handler) {
        return new BrightnessDialog(broadcastDispatcher, factory, handler);
    }
}
