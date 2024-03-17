package com.android.systemui.statusbar.connectivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkScoreManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import com.android.settingslib.wifi.WifiStatusTracker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: WifiStatusTrackerFactory.kt */
public final class WifiStatusTrackerFactory {
    @NotNull
    public final ConnectivityManager mConnectivityManager;
    @NotNull
    public final Context mContext;
    @NotNull
    public final Handler mMainHandler;
    @NotNull
    public final NetworkScoreManager mNetworkScoreManager;
    @Nullable
    public final WifiManager mWifiManager;

    public WifiStatusTrackerFactory(@NotNull Context context, @Nullable WifiManager wifiManager, @NotNull NetworkScoreManager networkScoreManager, @NotNull ConnectivityManager connectivityManager, @NotNull Handler handler) {
        this.mContext = context;
        this.mWifiManager = wifiManager;
        this.mNetworkScoreManager = networkScoreManager;
        this.mConnectivityManager = connectivityManager;
        this.mMainHandler = handler;
    }

    @NotNull
    public final WifiStatusTracker createTracker(@Nullable Runnable runnable, @Nullable Handler handler) {
        return new WifiStatusTracker(this.mContext, this.mWifiManager, this.mNetworkScoreManager, this.mConnectivityManager, runnable, this.mMainHandler, handler);
    }
}
