package com.android.systemui.statusbar.connectivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import com.android.systemui.statusbar.connectivity.AccessPointControllerImpl;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class AccessPointControllerImpl_WifiPickerTrackerFactory_Factory implements Factory<AccessPointControllerImpl.WifiPickerTrackerFactory> {
    public final Provider<ConnectivityManager> connectivityManagerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<Handler> mainHandlerProvider;
    public final Provider<WifiManager> wifiManagerProvider;
    public final Provider<Handler> workerHandlerProvider;

    public AccessPointControllerImpl_WifiPickerTrackerFactory_Factory(Provider<Context> provider, Provider<WifiManager> provider2, Provider<ConnectivityManager> provider3, Provider<Handler> provider4, Provider<Handler> provider5) {
        this.contextProvider = provider;
        this.wifiManagerProvider = provider2;
        this.connectivityManagerProvider = provider3;
        this.mainHandlerProvider = provider4;
        this.workerHandlerProvider = provider5;
    }

    public AccessPointControllerImpl.WifiPickerTrackerFactory get() {
        return newInstance(this.contextProvider.get(), this.wifiManagerProvider.get(), this.connectivityManagerProvider.get(), this.mainHandlerProvider.get(), this.workerHandlerProvider.get());
    }

    public static AccessPointControllerImpl_WifiPickerTrackerFactory_Factory create(Provider<Context> provider, Provider<WifiManager> provider2, Provider<ConnectivityManager> provider3, Provider<Handler> provider4, Provider<Handler> provider5) {
        return new AccessPointControllerImpl_WifiPickerTrackerFactory_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static AccessPointControllerImpl.WifiPickerTrackerFactory newInstance(Context context, WifiManager wifiManager, ConnectivityManager connectivityManager, Handler handler, Handler handler2) {
        return new AccessPointControllerImpl.WifiPickerTrackerFactory(context, wifiManager, connectivityManager, handler, handler2);
    }
}
