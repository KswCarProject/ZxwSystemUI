package com.android.keyguard;

import android.media.AudioManager;
import android.telephony.TelephonyManager;
import com.android.keyguard.KeyguardSecurityContainerController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class KeyguardHostViewController_Factory implements Factory<KeyguardHostViewController> {
    public final Provider<AudioManager> audioManagerProvider;
    public final Provider<KeyguardSecurityContainerController.Factory> keyguardSecurityContainerControllerFactoryProvider;
    public final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    public final Provider<TelephonyManager> telephonyManagerProvider;
    public final Provider<ViewMediatorCallback> viewMediatorCallbackProvider;
    public final Provider<KeyguardHostView> viewProvider;

    public KeyguardHostViewController_Factory(Provider<KeyguardHostView> provider, Provider<KeyguardUpdateMonitor> provider2, Provider<AudioManager> provider3, Provider<TelephonyManager> provider4, Provider<ViewMediatorCallback> provider5, Provider<KeyguardSecurityContainerController.Factory> provider6) {
        this.viewProvider = provider;
        this.keyguardUpdateMonitorProvider = provider2;
        this.audioManagerProvider = provider3;
        this.telephonyManagerProvider = provider4;
        this.viewMediatorCallbackProvider = provider5;
        this.keyguardSecurityContainerControllerFactoryProvider = provider6;
    }

    public KeyguardHostViewController get() {
        return newInstance(this.viewProvider.get(), this.keyguardUpdateMonitorProvider.get(), this.audioManagerProvider.get(), this.telephonyManagerProvider.get(), this.viewMediatorCallbackProvider.get(), this.keyguardSecurityContainerControllerFactoryProvider.get());
    }

    public static KeyguardHostViewController_Factory create(Provider<KeyguardHostView> provider, Provider<KeyguardUpdateMonitor> provider2, Provider<AudioManager> provider3, Provider<TelephonyManager> provider4, Provider<ViewMediatorCallback> provider5, Provider<KeyguardSecurityContainerController.Factory> provider6) {
        return new KeyguardHostViewController_Factory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static KeyguardHostViewController newInstance(KeyguardHostView keyguardHostView, KeyguardUpdateMonitor keyguardUpdateMonitor, AudioManager audioManager, TelephonyManager telephonyManager, ViewMediatorCallback viewMediatorCallback, Object obj) {
        return new KeyguardHostViewController(keyguardHostView, keyguardUpdateMonitor, audioManager, telephonyManager, viewMediatorCallback, (KeyguardSecurityContainerController.Factory) obj);
    }
}
