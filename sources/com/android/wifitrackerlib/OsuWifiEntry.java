package com.android.wifitrackerlib;

import android.content.Context;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.hotspot2.OsuProvider;
import android.net.wifi.hotspot2.PasspointConfiguration;
import android.net.wifi.hotspot2.ProvisioningCallback;
import android.os.Handler;
import android.os.UserManager;
import android.text.TextUtils;
import android.util.Pair;
import androidx.core.os.BuildCompat;
import androidx.core.util.Preconditions;
import com.android.wifitrackerlib.WifiEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class OsuWifiEntry extends WifiEntry {
    public final Context mContext;
    public final List<ScanResult> mCurrentScanResults = new ArrayList();
    public boolean mHasAddConfigUserRestriction = false;
    public boolean mIsAlreadyProvisioned = false;
    public final String mKey;
    public final OsuProvider mOsuProvider;
    public String mOsuStatusString;
    public String mSsid;
    public final UserManager mUserManager;

    public String getScanResultDescription() {
        return "";
    }

    public OsuWifiEntry(WifiTrackerInjector wifiTrackerInjector, Context context, Handler handler, OsuProvider osuProvider, WifiManager wifiManager, boolean z) throws IllegalArgumentException {
        super(handler, wifiManager, z);
        Preconditions.checkNotNull(osuProvider, "Cannot construct with null osuProvider!");
        this.mContext = context;
        this.mOsuProvider = osuProvider;
        this.mKey = osuProviderToOsuWifiEntryKey(osuProvider);
        UserManager userManager = wifiTrackerInjector.getUserManager();
        this.mUserManager = userManager;
        if (BuildCompat.isAtLeastT() && userManager != null) {
            this.mHasAddConfigUserRestriction = userManager.hasUserRestriction("no_add_wifi_config");
        }
    }

    public String getKey() {
        return this.mKey;
    }

    public synchronized String getTitle() {
        String friendlyName = this.mOsuProvider.getFriendlyName();
        if (!TextUtils.isEmpty(friendlyName)) {
            return friendlyName;
        }
        if (!TextUtils.isEmpty(this.mSsid)) {
            return this.mSsid;
        }
        Uri serverUri = this.mOsuProvider.getServerUri();
        if (serverUri == null) {
            return "";
        }
        return serverUri.toString();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0031, code lost:
        return r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized java.lang.String getSummary(boolean r2) {
        /*
            r1 = this;
            monitor-enter(r1)
            boolean r0 = r1.hasAdminRestrictions()     // Catch:{ all -> 0x003c }
            if (r0 == 0) goto L_0x0011
            android.content.Context r2 = r1.mContext     // Catch:{ all -> 0x003c }
            int r0 = com.android.wifitrackerlib.R$string.wifitrackerlib_admin_restricted_network     // Catch:{ all -> 0x003c }
            java.lang.String r2 = r2.getString(r0)     // Catch:{ all -> 0x003c }
            monitor-exit(r1)
            return r2
        L_0x0011:
            java.lang.String r0 = r1.mOsuStatusString     // Catch:{ all -> 0x003c }
            if (r0 == 0) goto L_0x0017
            monitor-exit(r1)
            return r0
        L_0x0017:
            boolean r0 = r1.isAlreadyProvisioned()     // Catch:{ all -> 0x003c }
            if (r0 == 0) goto L_0x0032
            if (r2 == 0) goto L_0x0028
            android.content.Context r2 = r1.mContext     // Catch:{ all -> 0x003c }
            int r0 = com.android.wifitrackerlib.R$string.wifitrackerlib_wifi_passpoint_expired     // Catch:{ all -> 0x003c }
            java.lang.String r2 = r2.getString(r0)     // Catch:{ all -> 0x003c }
            goto L_0x0030
        L_0x0028:
            android.content.Context r2 = r1.mContext     // Catch:{ all -> 0x003c }
            int r0 = com.android.wifitrackerlib.R$string.wifitrackerlib_tap_to_renew_subscription_and_connect     // Catch:{ all -> 0x003c }
            java.lang.String r2 = r2.getString(r0)     // Catch:{ all -> 0x003c }
        L_0x0030:
            monitor-exit(r1)
            return r2
        L_0x0032:
            android.content.Context r2 = r1.mContext     // Catch:{ all -> 0x003c }
            int r0 = com.android.wifitrackerlib.R$string.wifitrackerlib_tap_to_sign_up     // Catch:{ all -> 0x003c }
            java.lang.String r2 = r2.getString(r0)     // Catch:{ all -> 0x003c }
            monitor-exit(r1)
            return r2
        L_0x003c:
            r2 = move-exception
            monitor-exit(r1)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.OsuWifiEntry.getSummary(boolean):java.lang.String");
    }

    public synchronized String getSsid() {
        return this.mSsid;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0017, code lost:
        return r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean canConnect() {
        /*
            r3 = this;
            monitor-enter(r3)
            boolean r0 = r3.hasAdminRestrictions()     // Catch:{ all -> 0x0018 }
            r1 = 0
            if (r0 == 0) goto L_0x000a
            monitor-exit(r3)
            return r1
        L_0x000a:
            int r0 = r3.mLevel     // Catch:{ all -> 0x0018 }
            r2 = -1
            if (r0 == r2) goto L_0x0016
            int r0 = r3.getConnectedState()     // Catch:{ all -> 0x0018 }
            if (r0 != 0) goto L_0x0016
            r1 = 1
        L_0x0016:
            monitor-exit(r3)
            return r1
        L_0x0018:
            r0 = move-exception
            monitor-exit(r3)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.OsuWifiEntry.canConnect():boolean");
    }

    public synchronized void connect(WifiEntry.ConnectCallback connectCallback) {
        this.mConnectCallback = connectCallback;
        this.mWifiManager.stopRestrictingAutoJoinToSubscriptionId();
        this.mWifiManager.startSubscriptionProvisioning(this.mOsuProvider, this.mContext.getMainExecutor(), new OsuWifiEntryProvisioningCallback());
    }

    public synchronized void updateScanResultInfo(List<ScanResult> list) throws IllegalArgumentException {
        if (list == null) {
            list = new ArrayList<>();
        }
        this.mCurrentScanResults.clear();
        this.mCurrentScanResults.addAll(list);
        ScanResult bestScanResultByLevel = Utils.getBestScanResultByLevel(list);
        if (bestScanResultByLevel != null) {
            updateTransitionModeCapa(bestScanResultByLevel);
            if (WifiEntry.isGbkSsidSupported()) {
                this.mSsid = bestScanResultByLevel.getWifiSsid().toString();
            } else {
                this.mSsid = bestScanResultByLevel.SSID;
            }
            if (getConnectedState() == 0) {
                this.mLevel = this.mWifiManager.calculateSignalLevel(bestScanResultByLevel.level);
            }
        } else {
            this.mLevel = -1;
        }
        updateWifiGenerationInfo(this.mCurrentScanResults);
        notifyOnUpdated();
    }

    public static String osuProviderToOsuWifiEntryKey(OsuProvider osuProvider) {
        Preconditions.checkNotNull(osuProvider, "Cannot create key with null OsuProvider!");
        return "OsuWifiEntry:" + osuProvider.getFriendlyName() + "," + osuProvider.getServerUri().toString();
    }

    public boolean connectionInfoMatches(WifiInfo wifiInfo, NetworkInfo networkInfo) {
        return wifiInfo.isOsuAp() && TextUtils.equals(wifiInfo.getPasspointProviderFriendlyName(), this.mOsuProvider.getFriendlyName());
    }

    public OsuProvider getOsuProvider() {
        return this.mOsuProvider;
    }

    public synchronized boolean isAlreadyProvisioned() {
        return this.mIsAlreadyProvisioned;
    }

    public synchronized void setAlreadyProvisioned(boolean z) {
        this.mIsAlreadyProvisioned = z;
    }

    public final boolean hasAdminRestrictions() {
        return this.mHasAddConfigUserRestriction && !this.mIsAlreadyProvisioned;
    }

    public class OsuWifiEntryProvisioningCallback extends ProvisioningCallback {
        public OsuWifiEntryProvisioningCallback() {
        }

        public void onProvisioningFailure(int i) {
            synchronized (OsuWifiEntry.this) {
                if (TextUtils.equals(OsuWifiEntry.this.mOsuStatusString, OsuWifiEntry.this.mContext.getString(R$string.wifitrackerlib_osu_completing_sign_up))) {
                    OsuWifiEntry osuWifiEntry = OsuWifiEntry.this;
                    osuWifiEntry.mOsuStatusString = osuWifiEntry.mContext.getString(R$string.wifitrackerlib_osu_sign_up_failed);
                } else {
                    OsuWifiEntry osuWifiEntry2 = OsuWifiEntry.this;
                    osuWifiEntry2.mOsuStatusString = osuWifiEntry2.mContext.getString(R$string.wifitrackerlib_osu_connect_failed);
                }
            }
            WifiEntry.ConnectCallback connectCallback = OsuWifiEntry.this.mConnectCallback;
            if (connectCallback != null) {
                connectCallback.onConnectResult(2);
            }
            OsuWifiEntry.this.notifyOnUpdated();
        }

        public void onProvisioningStatus(int i) {
            String str;
            boolean z = false;
            switch (i) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    str = String.format(OsuWifiEntry.this.mContext.getString(R$string.wifitrackerlib_osu_opening_provider), new Object[]{OsuWifiEntry.this.getTitle()});
                    break;
                case 8:
                case 9:
                case 10:
                case 11:
                    str = OsuWifiEntry.this.mContext.getString(R$string.wifitrackerlib_osu_completing_sign_up);
                    break;
                default:
                    str = null;
                    break;
            }
            synchronized (OsuWifiEntry.this) {
                if (!TextUtils.equals(OsuWifiEntry.this.mOsuStatusString, str)) {
                    z = true;
                }
                OsuWifiEntry.this.mOsuStatusString = str;
                if (z) {
                    OsuWifiEntry.this.notifyOnUpdated();
                }
            }
        }

        public void onProvisioningComplete() {
            ScanResult scanResult;
            synchronized (OsuWifiEntry.this) {
                OsuWifiEntry osuWifiEntry = OsuWifiEntry.this;
                osuWifiEntry.mOsuStatusString = osuWifiEntry.mContext.getString(R$string.wifitrackerlib_osu_sign_up_complete);
            }
            OsuWifiEntry.this.notifyOnUpdated();
            OsuWifiEntry osuWifiEntry2 = OsuWifiEntry.this;
            PasspointConfiguration passpointConfiguration = (PasspointConfiguration) osuWifiEntry2.mWifiManager.getMatchingPasspointConfigsForOsuProviders(Collections.singleton(osuWifiEntry2.mOsuProvider)).get(OsuWifiEntry.this.mOsuProvider);
            WifiEntry.ConnectCallback connectCallback = OsuWifiEntry.this.mConnectCallback;
            if (passpointConfiguration != null) {
                String uniqueId = passpointConfiguration.getUniqueId();
                WifiManager wifiManager = OsuWifiEntry.this.mWifiManager;
                Iterator it = wifiManager.getAllMatchingWifiConfigs(wifiManager.getScanResults()).iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    Pair pair = (Pair) it.next();
                    WifiConfiguration wifiConfiguration = (WifiConfiguration) pair.first;
                    if (TextUtils.equals(wifiConfiguration.getKey(), uniqueId)) {
                        List list = (List) ((Map) pair.second).get(0);
                        List list2 = (List) ((Map) pair.second).get(1);
                        if (list != null && !list.isEmpty()) {
                            scanResult = Utils.getBestScanResultByLevel(list);
                        } else if (list2 != null && !list2.isEmpty()) {
                            scanResult = Utils.getBestScanResultByLevel(list2);
                        }
                        if (WifiEntry.isGbkSsidSupported()) {
                            wifiConfiguration.SSID = scanResult.getWifiSsid().toString();
                        } else {
                            wifiConfiguration.SSID = "\"" + scanResult.SSID + "\"";
                        }
                        OsuWifiEntry.this.mWifiManager.connect(wifiConfiguration, (WifiManager.ActionListener) null);
                        return;
                    }
                }
                if (connectCallback != null) {
                    connectCallback.onConnectResult(2);
                }
            } else if (connectCallback != null) {
                connectCallback.onConnectResult(2);
            }
        }
    }
}
