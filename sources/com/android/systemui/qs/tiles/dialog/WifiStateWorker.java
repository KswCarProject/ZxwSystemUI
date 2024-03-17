package com.android.systemui.qs.tiles.dialog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.util.Log;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.util.concurrency.DelayableExecutor;

public class WifiStateWorker extends BroadcastReceiver {
    public DelayableExecutor mBackgroundExecutor;
    public WifiManager mWifiManager;
    public int mWifiState = 1;

    public WifiStateWorker(BroadcastDispatcher broadcastDispatcher, DelayableExecutor delayableExecutor, WifiManager wifiManager) {
        this.mWifiManager = wifiManager;
        this.mBackgroundExecutor = delayableExecutor;
        broadcastDispatcher.registerReceiver(this, new IntentFilter("android.net.wifi.WIFI_STATE_CHANGED"));
        this.mBackgroundExecutor.execute(new WifiStateWorker$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        WifiManager wifiManager = this.mWifiManager;
        if (wifiManager != null) {
            this.mWifiState = wifiManager.getWifiState();
            Log.i("WifiStateWorker", "WifiManager.getWifiState():" + this.mWifiState);
        }
    }

    public void setWifiEnabled(boolean z) {
        this.mBackgroundExecutor.execute(new WifiStateWorker$$ExternalSyntheticLambda1(this, z));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setWifiEnabled$1(boolean z) {
        WifiManager wifiManager = this.mWifiManager;
        if (wifiManager != null) {
            this.mWifiState = z ? 2 : 0;
            if (!wifiManager.setWifiEnabled(z)) {
                Log.e("WifiStateWorker", "Failed to WifiManager.setWifiEnabled(" + z + ");");
            }
        }
    }

    public boolean isWifiEnabled() {
        int i = this.mWifiState;
        return i == 3 || i == 2;
    }

    public void onReceive(Context context, Intent intent) {
        int intExtra;
        if (intent != null && "android.net.wifi.WIFI_STATE_CHANGED".equals(intent.getAction()) && (intExtra = intent.getIntExtra("wifi_state", 1)) != 4) {
            this.mWifiState = intExtra;
        }
    }
}
