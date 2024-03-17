package com.android.wifitrackerlib;

import android.content.Context;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;
import com.android.wifitrackerlib.WifiEntry;
import java.util.StringJoiner;

public class MergedCarrierEntry extends WifiEntry {
    public final Context mContext;
    public boolean mIsCellDefaultRoute;
    public final String mKey;
    public final int mSubscriptionId;

    public MergedCarrierEntry(Handler handler, WifiManager wifiManager, boolean z, Context context, int i) throws IllegalArgumentException {
        super(handler, wifiManager, z);
        this.mContext = context;
        this.mSubscriptionId = i;
        this.mKey = "MergedCarrierEntry:" + i;
    }

    public String getKey() {
        return this.mKey;
    }

    public String getSummary(boolean z) {
        StringJoiner stringJoiner = new StringJoiner(this.mContext.getString(R$string.wifitrackerlib_summary_separator));
        if (!z) {
            String verboseLoggingDescription = Utils.getVerboseLoggingDescription(this);
            if (!TextUtils.isEmpty(verboseLoggingDescription)) {
                stringJoiner.add(verboseLoggingDescription);
            }
        }
        return stringJoiner.toString();
    }

    public synchronized String getSsid() {
        WifiInfo wifiInfo = this.mWifiInfo;
        if (wifiInfo == null) {
            return null;
        }
        return WifiInfo.sanitizeSsid(wifiInfo.getSSID());
    }

    public synchronized boolean canConnect() {
        return getConnectedState() == 0 && !this.mIsCellDefaultRoute;
    }

    public synchronized void connect(WifiEntry.ConnectCallback connectCallback) {
        connect(connectCallback, true);
    }

    public synchronized void connect(WifiEntry.ConnectCallback connectCallback, boolean z) {
        this.mConnectCallback = connectCallback;
        this.mWifiManager.startRestrictingAutoJoinToSubscriptionId(this.mSubscriptionId);
        if (z) {
            Toast.makeText(this.mContext, R$string.wifitrackerlib_wifi_wont_autoconnect_for_now, 0).show();
        }
        if (this.mConnectCallback != null) {
            this.mCallbackHandler.post(new MergedCarrierEntry$$ExternalSyntheticLambda0(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$connect$0() {
        WifiEntry.ConnectCallback connectCallback = this.mConnectCallback;
        if (connectCallback != null) {
            connectCallback.onConnectResult(0);
        }
    }

    public boolean canDisconnect() {
        return getConnectedState() == 2;
    }

    public boolean connectionInfoMatches(WifiInfo wifiInfo, NetworkInfo networkInfo) {
        return wifiInfo.isCarrierMerged() && this.mSubscriptionId == wifiInfo.getSubscriptionId();
    }

    public void setEnabled(boolean z) {
        this.mWifiManager.setCarrierNetworkOffloadEnabled(this.mSubscriptionId, true, z);
        if (!z) {
            this.mWifiManager.stopRestrictingAutoJoinToSubscriptionId();
            this.mWifiManager.startScan();
        }
    }

    public int getSubscriptionId() {
        return this.mSubscriptionId;
    }

    public synchronized void updateIsCellDefaultRoute(boolean z) {
        this.mIsCellDefaultRoute = z;
        notifyOnUpdated();
    }
}
