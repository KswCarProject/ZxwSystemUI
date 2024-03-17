package com.android.systemui.qrcodescanner.controller;

import android.content.Context;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.systemui.util.settings.SecureSettings;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class QRCodeScannerController_Factory implements Factory<QRCodeScannerController> {
    public final Provider<Context> contextProvider;
    public final Provider<Executor> executorProvider;
    public final Provider<DeviceConfigProxy> proxyProvider;
    public final Provider<SecureSettings> secureSettingsProvider;
    public final Provider<UserTracker> userTrackerProvider;

    public QRCodeScannerController_Factory(Provider<Context> provider, Provider<Executor> provider2, Provider<SecureSettings> provider3, Provider<DeviceConfigProxy> provider4, Provider<UserTracker> provider5) {
        this.contextProvider = provider;
        this.executorProvider = provider2;
        this.secureSettingsProvider = provider3;
        this.proxyProvider = provider4;
        this.userTrackerProvider = provider5;
    }

    public QRCodeScannerController get() {
        return newInstance(this.contextProvider.get(), this.executorProvider.get(), this.secureSettingsProvider.get(), this.proxyProvider.get(), this.userTrackerProvider.get());
    }

    public static QRCodeScannerController_Factory create(Provider<Context> provider, Provider<Executor> provider2, Provider<SecureSettings> provider3, Provider<DeviceConfigProxy> provider4, Provider<UserTracker> provider5) {
        return new QRCodeScannerController_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static QRCodeScannerController newInstance(Context context, Executor executor, SecureSettings secureSettings, DeviceConfigProxy deviceConfigProxy, UserTracker userTracker) {
        return new QRCodeScannerController(context, executor, secureSettings, deviceConfigProxy, userTracker);
    }
}
