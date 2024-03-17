package com.android.systemui.media;

import android.content.Context;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class LocalMediaManagerFactory_Factory implements Factory<LocalMediaManagerFactory> {
    public final Provider<Context> contextProvider;
    public final Provider<LocalBluetoothManager> localBluetoothManagerProvider;

    public LocalMediaManagerFactory_Factory(Provider<Context> provider, Provider<LocalBluetoothManager> provider2) {
        this.contextProvider = provider;
        this.localBluetoothManagerProvider = provider2;
    }

    public LocalMediaManagerFactory get() {
        return newInstance(this.contextProvider.get(), this.localBluetoothManagerProvider.get());
    }

    public static LocalMediaManagerFactory_Factory create(Provider<Context> provider, Provider<LocalBluetoothManager> provider2) {
        return new LocalMediaManagerFactory_Factory(provider, provider2);
    }

    public static LocalMediaManagerFactory newInstance(Context context, LocalBluetoothManager localBluetoothManager) {
        return new LocalMediaManagerFactory(context, localBluetoothManager);
    }
}
