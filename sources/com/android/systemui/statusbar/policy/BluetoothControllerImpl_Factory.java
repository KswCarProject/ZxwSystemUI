package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.os.Looper;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.systemui.dump.DumpManager;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class BluetoothControllerImpl_Factory implements Factory<BluetoothControllerImpl> {
    public final Provider<Looper> bgLooperProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<LocalBluetoothManager> localBluetoothManagerProvider;
    public final Provider<Looper> mainLooperProvider;

    public BluetoothControllerImpl_Factory(Provider<Context> provider, Provider<DumpManager> provider2, Provider<Looper> provider3, Provider<Looper> provider4, Provider<LocalBluetoothManager> provider5) {
        this.contextProvider = provider;
        this.dumpManagerProvider = provider2;
        this.bgLooperProvider = provider3;
        this.mainLooperProvider = provider4;
        this.localBluetoothManagerProvider = provider5;
    }

    public BluetoothControllerImpl get() {
        return newInstance(this.contextProvider.get(), this.dumpManagerProvider.get(), this.bgLooperProvider.get(), this.mainLooperProvider.get(), this.localBluetoothManagerProvider.get());
    }

    public static BluetoothControllerImpl_Factory create(Provider<Context> provider, Provider<DumpManager> provider2, Provider<Looper> provider3, Provider<Looper> provider4, Provider<LocalBluetoothManager> provider5) {
        return new BluetoothControllerImpl_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static BluetoothControllerImpl newInstance(Context context, DumpManager dumpManager, Looper looper, Looper looper2, LocalBluetoothManager localBluetoothManager) {
        return new BluetoothControllerImpl(context, dumpManager, looper, looper2, localBluetoothManager);
    }
}
