package com.android.wifitrackerlib;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.vcn.VcnTransportInfo;
import android.net.wifi.WifiInfo;
import android.os.Handler;
import android.os.UserManager;

public class NonSdkApiWrapper {
    public static void startCaptivePortalApp(ConnectivityManager connectivityManager, Network network) {
        connectivityManager.startCaptivePortalApp(network);
    }

    public static boolean isVcnOverWifi(NetworkCapabilities networkCapabilities) {
        VcnTransportInfo transportInfo = networkCapabilities.getTransportInfo();
        return (transportInfo == null || !(transportInfo instanceof VcnTransportInfo) || transportInfo.getWifiInfo() == null) ? false : true;
    }

    public static boolean isDemoMode(Context context) {
        return UserManager.isDeviceInDemoMode(context);
    }

    public static void registerSystemDefaultNetworkCallback(ConnectivityManager connectivityManager, ConnectivityManager.NetworkCallback networkCallback, Handler handler) {
        connectivityManager.registerSystemDefaultNetworkCallback(networkCallback, handler);
    }

    public static boolean isPrimary(WifiInfo wifiInfo) {
        return wifiInfo.isPrimary();
    }
}
