package com.android.systemui.statusbar.connectivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkScoreManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class WifiStatusTrackerFactory_Factory implements Factory<WifiStatusTrackerFactory> {
    public final Provider<ConnectivityManager> mConnectivityManagerProvider;
    public final Provider<Context> mContextProvider;
    public final Provider<Handler> mMainHandlerProvider;
    public final Provider<NetworkScoreManager> mNetworkScoreManagerProvider;
    public final Provider<WifiManager> mWifiManagerProvider;

    public WifiStatusTrackerFactory_Factory(Provider<Context> provider, Provider<WifiManager> provider2, Provider<NetworkScoreManager> provider3, Provider<ConnectivityManager> provider4, Provider<Handler> provider5) {
        this.mContextProvider = provider;
        this.mWifiManagerProvider = provider2;
        this.mNetworkScoreManagerProvider = provider3;
        this.mConnectivityManagerProvider = provider4;
        this.mMainHandlerProvider = provider5;
    }

    public WifiStatusTrackerFactory get() {
        return newInstance(this.mContextProvider.get(), this.mWifiManagerProvider.get(), this.mNetworkScoreManagerProvider.get(), this.mConnectivityManagerProvider.get(), this.mMainHandlerProvider.get());
    }

    public static WifiStatusTrackerFactory_Factory create(Provider<Context> provider, Provider<WifiManager> provider2, Provider<NetworkScoreManager> provider3, Provider<ConnectivityManager> provider4, Provider<Handler> provider5) {
        return new WifiStatusTrackerFactory_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static WifiStatusTrackerFactory newInstance(Context context, WifiManager wifiManager, NetworkScoreManager networkScoreManager, ConnectivityManager connectivityManager, Handler handler) {
        return new WifiStatusTrackerFactory(context, wifiManager, networkScoreManager, connectivityManager, handler);
    }
}
