package com.android.wifitrackerlib;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import androidx.core.util.Preconditions;
import androidx.lifecycle.Lifecycle;
import com.android.wifitrackerlib.StandardWifiEntry;
import java.time.Clock;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class StandardNetworkDetailsTracker extends NetworkDetailsTracker {
    public final StandardWifiEntry mChosenEntry;
    public NetworkInfo mCurrentNetworkInfo;
    public final boolean mIsNetworkRequest;
    public final StandardWifiEntry.StandardWifiEntryKey mKey;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public StandardNetworkDetailsTracker(WifiTrackerInjector wifiTrackerInjector, Lifecycle lifecycle, Context context, WifiManager wifiManager, ConnectivityManager connectivityManager, Handler handler, Handler handler2, Clock clock, long j, long j2, String str) {
        super(wifiTrackerInjector, lifecycle, context, wifiManager, connectivityManager, handler, handler2, clock, j, j2, "StandardNetworkDetailsTracker");
        StandardWifiEntry.StandardWifiEntryKey standardWifiEntryKey = new StandardWifiEntry.StandardWifiEntryKey(str);
        this.mKey = standardWifiEntryKey;
        if (standardWifiEntryKey.isNetworkRequest()) {
            this.mIsNetworkRequest = true;
            this.mChosenEntry = new NetworkRequestEntry(this.mInjector, this.mContext, this.mMainHandler, standardWifiEntryKey, this.mWifiManager, false);
        } else {
            this.mIsNetworkRequest = false;
            this.mChosenEntry = new StandardWifiEntry(this.mInjector, this.mContext, this.mMainHandler, standardWifiEntryKey, this.mWifiManager, false);
        }
        updateStartInfo();
    }

    public WifiEntry getWifiEntry() {
        return this.mChosenEntry;
    }

    public void handleOnStart() {
        updateStartInfo();
    }

    public void handleWifiStateChangedAction() {
        conditionallyUpdateScanResults(true);
    }

    public void handleScanResultsAvailableAction(Intent intent) {
        Preconditions.checkNotNull(intent, "Intent cannot be null!");
        conditionallyUpdateScanResults(intent.getBooleanExtra("resultsUpdated", true));
    }

    public void handleConfiguredNetworksChangedAction(Intent intent) {
        Preconditions.checkNotNull(intent, "Intent cannot be null!");
        conditionallyUpdateConfig();
    }

    public final void updateStartInfo() {
        boolean z = true;
        conditionallyUpdateScanResults(true);
        conditionallyUpdateConfig();
        WifiInfo connectionInfo = this.mWifiManager.getConnectionInfo();
        Network currentNetwork = this.mWifiManager.getCurrentNetwork();
        NetworkInfo networkInfo = this.mConnectivityManager.getNetworkInfo(currentNetwork);
        this.mCurrentNetworkInfo = networkInfo;
        this.mChosenEntry.updateConnectionInfo(connectionInfo, networkInfo);
        handleNetworkCapabilitiesChanged(this.mConnectivityManager.getNetworkCapabilities(currentNetwork));
        handleLinkPropertiesChanged(this.mConnectivityManager.getLinkProperties(currentNetwork));
        this.mChosenEntry.setIsDefaultNetwork(this.mIsWifiDefaultRoute);
        StandardWifiEntry standardWifiEntry = this.mChosenEntry;
        if (!this.mIsWifiValidated || !this.mIsCellDefaultRoute) {
            z = false;
        }
        standardWifiEntry.setIsLowQuality(z);
    }

    public final void conditionallyUpdateScanResults(boolean z) {
        if (this.mWifiManager.getWifiState() == 1) {
            this.mChosenEntry.updateScanResultInfo(Collections.emptyList());
            return;
        }
        long j = this.mMaxScanAgeMillis;
        if (z) {
            cacheNewScanResults();
        } else {
            j += this.mScanIntervalMillis;
        }
        this.mChosenEntry.updateScanResultInfo(this.mScanResultUpdater.getScanResults(j));
    }

    public final void conditionallyUpdateConfig() {
        this.mChosenEntry.updateConfig((List) this.mWifiManager.getPrivilegedConfiguredNetworks().stream().filter(new StandardNetworkDetailsTracker$$ExternalSyntheticLambda0(this)).collect(Collectors.toList()));
    }

    public final void cacheNewScanResults() {
        this.mScanResultUpdater.update((List) this.mWifiManager.getScanResults().stream().filter(new StandardNetworkDetailsTracker$$ExternalSyntheticLambda1(this)).collect(Collectors.toList()));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$cacheNewScanResults$0(ScanResult scanResult) {
        return new StandardWifiEntry.ScanResultKey(scanResult).equals(this.mKey.getScanResultKey());
    }

    public final boolean configMatches(WifiConfiguration wifiConfiguration) {
        if (wifiConfiguration.isPasspoint()) {
            return false;
        }
        StandardWifiEntry.StandardWifiEntryKey standardWifiEntryKey = this.mKey;
        return standardWifiEntryKey.equals(new StandardWifiEntry.StandardWifiEntryKey(wifiConfiguration, standardWifiEntryKey.isTargetingNewNetworks()));
    }
}
