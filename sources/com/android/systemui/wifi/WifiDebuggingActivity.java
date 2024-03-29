package com.android.systemui.wifi;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.debug.IAdbManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.ServiceManager;
import android.util.EventLog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.Toast;
import com.android.internal.app.AlertActivity;
import com.android.internal.app.AlertController;
import com.android.systemui.R$string;

public class WifiDebuggingActivity extends AlertActivity implements DialogInterface.OnClickListener {
    public CheckBox mAlwaysAllow;
    public String mBssid;
    public boolean mClicked = false;
    public WifiChangeReceiver mWifiChangeReceiver;
    public WifiManager mWifiManager;

    /* JADX WARNING: type inference failed for: r6v0, types: [android.content.DialogInterface$OnClickListener, com.android.internal.app.AlertActivity, com.android.systemui.wifi.WifiDebuggingActivity, android.app.Activity] */
    public void onCreate(Bundle bundle) {
        Window window = getWindow();
        window.addSystemFlags(524288);
        window.setType(2008);
        WifiDebuggingActivity.super.onCreate(bundle);
        this.mWifiManager = (WifiManager) getSystemService("wifi");
        this.mWifiChangeReceiver = new WifiChangeReceiver(this);
        Intent intent = getIntent();
        String stringExtra = intent.getStringExtra("ssid");
        String stringExtra2 = intent.getStringExtra("bssid");
        this.mBssid = stringExtra2;
        if (stringExtra == null || stringExtra2 == null) {
            finish();
            return;
        }
        AlertController.AlertParams alertParams = this.mAlertParams;
        alertParams.mTitle = getString(R$string.wifi_debugging_title);
        alertParams.mMessage = getString(R$string.wifi_debugging_message, new Object[]{stringExtra, this.mBssid});
        alertParams.mPositiveButtonText = getString(R$string.wifi_debugging_allow);
        alertParams.mNegativeButtonText = getString(17039360);
        alertParams.mPositiveButtonListener = this;
        alertParams.mNegativeButtonListener = this;
        View inflate = LayoutInflater.from(alertParams.mContext).inflate(17367093, (ViewGroup) null);
        CheckBox checkBox = (CheckBox) inflate.findViewById(16908774);
        this.mAlwaysAllow = checkBox;
        checkBox.setText(getString(R$string.wifi_debugging_always));
        alertParams.mView = inflate;
        window.setCloseOnTouchOutside(false);
        setupAlert();
        this.mAlert.getButton(-1).setOnTouchListener(new WifiDebuggingActivity$$ExternalSyntheticLambda0());
    }

    public static /* synthetic */ boolean lambda$onCreate$0(View view, MotionEvent motionEvent) {
        if ((motionEvent.getFlags() & 1) == 0 && (motionEvent.getFlags() & 2) == 0) {
            return false;
        }
        if (motionEvent.getAction() == 1) {
            EventLog.writeEvent(1397638484, "62187985");
            Toast.makeText(view.getContext(), R$string.touch_filtered_warning, 0).show();
        }
        return true;
    }

    public void onWindowAttributesChanged(WindowManager.LayoutParams layoutParams) {
        WifiDebuggingActivity.super.onWindowAttributesChanged(layoutParams);
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
                WifiInfo connectionInfo = WifiDebuggingActivity.this.mWifiManager.getConnectionInfo();
                if (connectionInfo == null || connectionInfo.getNetworkId() == -1) {
                    this.mActivity.finish();
                    return;
                }
                String bssid = connectionInfo.getBSSID();
                if (bssid == null || bssid.isEmpty()) {
                    this.mActivity.finish();
                } else if (!bssid.equals(WifiDebuggingActivity.this.mBssid)) {
                    this.mActivity.finish();
                }
            }
        }
    }

    public void onStart() {
        WifiDebuggingActivity.super.onStart();
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
        WifiDebuggingActivity.super.onStop();
    }

    public void onDestroy() {
        WifiDebuggingActivity.super.onDestroy();
        if (!this.mClicked) {
            try {
                IAdbManager.Stub.asInterface(ServiceManager.getService("adb")).denyWirelessDebugging();
            } catch (Exception e) {
                Log.e("WifiDebuggingActivity", "Unable to notify Adb service", e);
            }
        }
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        boolean z = true;
        this.mClicked = true;
        boolean z2 = i == -1;
        if (!z2 || !this.mAlwaysAllow.isChecked()) {
            z = false;
        }
        try {
            IAdbManager asInterface = IAdbManager.Stub.asInterface(ServiceManager.getService("adb"));
            if (z2) {
                asInterface.allowWirelessDebugging(z, this.mBssid);
            } else {
                asInterface.denyWirelessDebugging();
            }
        } catch (Exception e) {
            Log.e("WifiDebuggingActivity", "Unable to notify Adb service", e);
        }
        finish();
    }
}
