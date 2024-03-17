package com.android.systemui.usb;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.debug.IAdbManager;
import android.os.Bundle;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import com.android.internal.app.AlertActivity;
import com.android.internal.app.AlertController;
import com.android.systemui.R$string;
import com.android.systemui.broadcast.BroadcastDispatcher;

public class UsbDebuggingActivity extends AlertActivity implements DialogInterface.OnClickListener {
    public CheckBox mAlwaysAllow;
    public final BroadcastDispatcher mBroadcastDispatcher;
    public UsbDisconnectedReceiver mDisconnectedReceiver;
    public String mKey;
    public boolean mServiceNotified;

    public UsbDebuggingActivity(BroadcastDispatcher broadcastDispatcher) {
        this.mBroadcastDispatcher = broadcastDispatcher;
    }

    /* JADX WARNING: type inference failed for: r5v0, types: [android.content.DialogInterface$OnClickListener, com.android.internal.app.AlertActivity, com.android.systemui.usb.UsbDebuggingActivity, android.app.Activity] */
    public void onCreate(Bundle bundle) {
        Window window = getWindow();
        window.addSystemFlags(524288);
        window.setType(2008);
        UsbDebuggingActivity.super.onCreate(bundle);
        boolean equals = SystemProperties.get("ro.boot.qemu").equals("1");
        if (SystemProperties.getInt("service.adb.tcp.port", 0) == 0 && !equals) {
            this.mDisconnectedReceiver = new UsbDisconnectedReceiver(this);
            this.mBroadcastDispatcher.registerReceiver(this.mDisconnectedReceiver, new IntentFilter("android.hardware.usb.action.USB_STATE"));
        }
        Intent intent = getIntent();
        String stringExtra = intent.getStringExtra("fingerprints");
        String stringExtra2 = intent.getStringExtra("key");
        this.mKey = stringExtra2;
        if (stringExtra == null || stringExtra2 == null) {
            finish();
            return;
        }
        AlertController.AlertParams alertParams = this.mAlertParams;
        alertParams.mTitle = getString(R$string.usb_debugging_title);
        alertParams.mMessage = getString(R$string.usb_debugging_message, new Object[]{stringExtra});
        alertParams.mPositiveButtonText = getString(R$string.usb_debugging_allow);
        alertParams.mNegativeButtonText = getString(17039360);
        alertParams.mPositiveButtonListener = this;
        alertParams.mNegativeButtonListener = this;
        View inflate = LayoutInflater.from(alertParams.mContext).inflate(17367093, (ViewGroup) null);
        CheckBox checkBox = (CheckBox) inflate.findViewById(16908774);
        this.mAlwaysAllow = checkBox;
        checkBox.setText(getString(R$string.usb_debugging_always));
        alertParams.mView = inflate;
        window.setCloseOnTouchOutside(false);
        setupAlert();
    }

    public void onWindowAttributesChanged(WindowManager.LayoutParams layoutParams) {
        UsbDebuggingActivity.super.onWindowAttributesChanged(layoutParams);
    }

    public class UsbDisconnectedReceiver extends BroadcastReceiver {
        public final Activity mActivity;

        public UsbDisconnectedReceiver(Activity activity) {
            this.mActivity = activity;
        }

        public void onReceive(Context context, Intent intent) {
            if ("android.hardware.usb.action.USB_STATE".equals(intent.getAction()) && !intent.getBooleanExtra("connected", false)) {
                Log.d("UsbDebuggingActivity", "USB disconnected, notifying service");
                UsbDebuggingActivity.this.notifyService(false);
                this.mActivity.finish();
            }
        }
    }

    public void onDestroy() {
        UsbDisconnectedReceiver usbDisconnectedReceiver = this.mDisconnectedReceiver;
        if (usbDisconnectedReceiver != null) {
            this.mBroadcastDispatcher.unregisterReceiver(usbDisconnectedReceiver);
        }
        if (isFinishing() && !this.mServiceNotified) {
            notifyService(false);
        }
        UsbDebuggingActivity.super.onDestroy();
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        boolean z = true;
        boolean z2 = i == -1;
        if (!z2 || !this.mAlwaysAllow.isChecked()) {
            z = false;
        }
        notifyService(z2, z);
        finish();
    }

    public final void notifyService(boolean z) {
        notifyService(z, false);
    }

    public final void notifyService(boolean z, boolean z2) {
        try {
            IAdbManager asInterface = IAdbManager.Stub.asInterface(ServiceManager.getService("adb"));
            if (z) {
                asInterface.allowDebugging(z2, this.mKey);
            } else {
                asInterface.denyDebugging();
            }
            this.mServiceNotified = true;
        } catch (Exception e) {
            Log.e("UsbDebuggingActivity", "Unable to notify Usb service", e);
        }
    }
}