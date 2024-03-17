package com.android.systemui.doze;

import android.app.UiModeManager;
import android.hardware.display.AmbientDisplayConfiguration;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class DozeSuppressor_Factory implements Factory<DozeSuppressor> {
    public final Provider<BiometricUnlockController> biometricUnlockControllerLazyProvider;
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<AmbientDisplayConfiguration> configProvider;
    public final Provider<DozeHost> dozeHostProvider;
    public final Provider<DozeLog> dozeLogProvider;
    public final Provider<UiModeManager> uiModeManagerProvider;

    public DozeSuppressor_Factory(Provider<DozeHost> provider, Provider<AmbientDisplayConfiguration> provider2, Provider<DozeLog> provider3, Provider<BroadcastDispatcher> provider4, Provider<UiModeManager> provider5, Provider<BiometricUnlockController> provider6) {
        this.dozeHostProvider = provider;
        this.configProvider = provider2;
        this.dozeLogProvider = provider3;
        this.broadcastDispatcherProvider = provider4;
        this.uiModeManagerProvider = provider5;
        this.biometricUnlockControllerLazyProvider = provider6;
    }

    public DozeSuppressor get() {
        return newInstance(this.dozeHostProvider.get(), this.configProvider.get(), this.dozeLogProvider.get(), this.broadcastDispatcherProvider.get(), this.uiModeManagerProvider.get(), DoubleCheck.lazy(this.biometricUnlockControllerLazyProvider));
    }

    public static DozeSuppressor_Factory create(Provider<DozeHost> provider, Provider<AmbientDisplayConfiguration> provider2, Provider<DozeLog> provider3, Provider<BroadcastDispatcher> provider4, Provider<UiModeManager> provider5, Provider<BiometricUnlockController> provider6) {
        return new DozeSuppressor_Factory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static DozeSuppressor newInstance(DozeHost dozeHost, AmbientDisplayConfiguration ambientDisplayConfiguration, DozeLog dozeLog, BroadcastDispatcher broadcastDispatcher, UiModeManager uiModeManager, Lazy<BiometricUnlockController> lazy) {
        return new DozeSuppressor(dozeHost, ambientDisplayConfiguration, dozeLog, broadcastDispatcher, uiModeManager, lazy);
    }
}
