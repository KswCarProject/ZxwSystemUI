package com.android.systemui.telephony;

import android.telephony.TelephonyManager;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class TelephonyListenerManager_Factory implements Factory<TelephonyListenerManager> {
    public final Provider<Executor> executorProvider;
    public final Provider<TelephonyCallback> telephonyCallbackProvider;
    public final Provider<TelephonyManager> telephonyManagerProvider;

    public TelephonyListenerManager_Factory(Provider<TelephonyManager> provider, Provider<Executor> provider2, Provider<TelephonyCallback> provider3) {
        this.telephonyManagerProvider = provider;
        this.executorProvider = provider2;
        this.telephonyCallbackProvider = provider3;
    }

    public TelephonyListenerManager get() {
        return newInstance(this.telephonyManagerProvider.get(), this.executorProvider.get(), this.telephonyCallbackProvider.get());
    }

    public static TelephonyListenerManager_Factory create(Provider<TelephonyManager> provider, Provider<Executor> provider2, Provider<TelephonyCallback> provider3) {
        return new TelephonyListenerManager_Factory(provider, provider2, provider3);
    }

    public static TelephonyListenerManager newInstance(TelephonyManager telephonyManager, Executor executor, Object obj) {
        return new TelephonyListenerManager(telephonyManager, executor, (TelephonyCallback) obj);
    }
}
