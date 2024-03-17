package com.android.systemui.dagger;

import android.content.Context;
import android.os.Handler;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class SettingsLibraryModule_ProvideLocalBluetoothControllerFactory implements Factory<LocalBluetoothManager> {
    public final Provider<Handler> bgHandlerProvider;
    public final Provider<Context> contextProvider;

    public SettingsLibraryModule_ProvideLocalBluetoothControllerFactory(Provider<Context> provider, Provider<Handler> provider2) {
        this.contextProvider = provider;
        this.bgHandlerProvider = provider2;
    }

    public LocalBluetoothManager get() {
        return provideLocalBluetoothController(this.contextProvider.get(), this.bgHandlerProvider.get());
    }

    public static SettingsLibraryModule_ProvideLocalBluetoothControllerFactory create(Provider<Context> provider, Provider<Handler> provider2) {
        return new SettingsLibraryModule_ProvideLocalBluetoothControllerFactory(provider, provider2);
    }

    public static LocalBluetoothManager provideLocalBluetoothController(Context context, Handler handler) {
        return SettingsLibraryModule.provideLocalBluetoothController(context, handler);
    }
}
