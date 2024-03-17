package com.android.systemui.wifi;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import com.android.internal.app.AlertActivity;
import com.android.internal.app.AlertController;
import com.android.systemui.R$string;

public class WifiDebuggingSecondaryUserActivity extends AlertActivity implements DialogInterface.OnClickListener {
    public WifiChangeReceiver mWifiChangeReceiver;
    public WifiManager mWifiManager;

    /* JADX WARNING: type inference failed for: r1v0, types: [android.content.DialogInterface$OnClickListener, com.android.internal.app.AlertActivity, android.app.Activity, com.android.systemui.wifi.WifiDebuggingSecondaryUserActivity] */
    public void onCreate(Bundle bundle) {
        WifiDebuggingSecondaryUserActivity.super.onCreate(bundle);
        this.mWifiManager = (WifiManager) getSystemService("wifi");
        this.mWifiChangeReceiver = new WifiChangeReceiver(this);
        AlertController.AlertParams alertParams = this.mAlertParams;
        alertParams.mTitle = getString(R$string.wifi_debugging_secondary_user_title);
        alertParams.mMessage = getString(R$string.wifi_debugging_secondary_user_message);
        alertParams.mPositiveButtonText = getString(17039370);
        alertParams.mPositiveButtonListener = this;
        setupAlert();
    }

    public class WifiChangeReceiver extends BroadcastReceiver {
        public final Activity mActivity;

        public WifiChangeReceiver(Activity activity) {
            this.mActivity = activity;
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.net.wifi.WIFI_STATE_CHANGED".equals(action)) {
                if (intent.getIntExtra("wifi_state", 1) == 1) {
                    this.mActivity.finish();
                }
            } else if ("android.net.wifi.STATE_CHANGE".equals(action)) {
                NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
                if (networkInfo.getType() != 1) {
                    return;
                }
                if (!networkInfo.isConnected()) {
                    this.mActivity.finish();
                    return;
                }
                WifiInfo connectionInfo = WifiDebuggingSecondaryUserActivity.this.mWifiManager.getConnectionInfo();
                if (connectionInfo == null || connectionInfo.getNetworkId() == -1) {
                    this.mActivity.finish();
                }
            }
        }
    }

    public void onStart() {
        WifiDebuggingSecondaryUserActivity.super.onStart();
        IntentFilter intentFilter = new IntentFilter("android.net.wifi.WIFI_STATE_CHANGED");
        intentFilter.addAction("android.net.wifi.STATE_CHANGE");
        registerReceiver(this.mWifiChangeReceiver, intentFilter);
        sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
    }

    public void onStop() {
        WifiChangeReceiver wifiChangeReceiver = this.mWifiChangeReceiver;
        if (wifiChangeReceiver != null) {
            unregisterReceiver(wifiChangeReceiver);
        }
        WifiDebuggingSecondaryUserActivity.super.onStop();
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        finish();
    }
}
