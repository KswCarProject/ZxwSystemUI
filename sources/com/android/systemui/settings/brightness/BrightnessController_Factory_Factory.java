package com.android.systemui.settings.brightness;

import android.content.Context;
import android.os.Handler;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.settings.brightness.BrightnessController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class BrightnessController_Factory_Factory implements Factory<BrightnessController.Factory> {
    public final Provider<Handler> bgHandlerProvider;
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<Context> contextProvider;

    public BrightnessController_Factory_Factory(Provider<Context> provider, Provider<BroadcastDispatcher> provider2, Provider<Handler> provider3) {
        this.contextProvider = provider;
        this.broadcastDispatcherProvider = provider2;
        this.bgHandlerProvider = provider3;
    }

    public BrightnessController.Factory get() {
        return newInstance(this.contextProvider.get(), this.broadcastDispatcherProvider.get(), this.bgHandlerProvider.get());
    }

    public static BrightnessController_Factory_Factory create(Provider<Context> provider, Provider<BroadcastDispatcher> provider2, Provider<Handler> provider3) {
        return new BrightnessController_Factory_Factory(provider, provider2, provider3);
    }

    public static BrightnessController.Factory newInstance(Context context, BroadcastDispatcher broadcastDispatcher, Handler handler) {
        return new BrightnessController.Factory(context, broadcastDispatcher, handler);
    }
}
