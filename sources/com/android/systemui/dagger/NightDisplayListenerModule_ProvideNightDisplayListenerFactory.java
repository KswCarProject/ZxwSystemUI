package com.android.systemui.dagger;

import android.content.Context;
import android.hardware.display.NightDisplayListener;
import android.os.Handler;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class NightDisplayListenerModule_ProvideNightDisplayListenerFactory implements Factory<NightDisplayListener> {
    public final Provider<Handler> bgHandlerProvider;
    public final Provider<Context> contextProvider;
    public final NightDisplayListenerModule module;

    public NightDisplayListenerModule_ProvideNightDisplayListenerFactory(NightDisplayListenerModule nightDisplayListenerModule, Provider<Context> provider, Provider<Handler> provider2) {
        this.module = nightDisplayListenerModule;
        this.contextProvider = provider;
        this.bgHandlerProvider = provider2;
    }

    public NightDisplayListener get() {
        return provideNightDisplayListener(this.module, this.contextProvider.get(), this.bgHandlerProvider.get());
    }

    public static NightDisplayListenerModule_ProvideNightDisplayListenerFactory create(NightDisplayListenerModule nightDisplayListenerModule, Provider<Context> provider, Provider<Handler> provider2) {
        return new NightDisplayListenerModule_ProvideNightDisplayListenerFactory(nightDisplayListenerModule, provider, provider2);
    }

    public static NightDisplayListener provideNightDisplayListener(NightDisplayListenerModule nightDisplayListenerModule, Context context, Handler handler) {
        return (NightDisplayListener) Preconditions.checkNotNullFromProvides(nightDisplayListenerModule.provideNightDisplayListener(context, handler));
    }
}
