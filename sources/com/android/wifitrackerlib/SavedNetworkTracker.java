package com.android.wifitrackerlib;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.hotspot2.PasspointConfiguration;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import androidx.core.util.Preconditions;
import androidx.lifecycle.Lifecycle;
import com.android.wifitrackerlib.BaseWifiTracker;
import com.android.wifitrackerlib.StandardWifiEntry;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SavedNetworkTracker extends BaseWifiTracker {
    public WifiEntry mConnectedWifiEntry;
    public NetworkInfo mCurrentNetworkInfo;
    public final SavedNetworkTrackerCallback mListener;
    public final Object mLock = new Object();
    public final Map<String, PasspointWifiEntry> mPasspointWifiEntryCache = new HashMap();
    public final List<WifiEntry> mSavedWifiEntries = new ArrayList();
    public final List<StandardWifiEntry> mStandardWifiEntryCache = new ArrayList();
    public final List<WifiEntry> mSubscriptionWifiEntries = new ArrayList();

    public interface SavedNetworkTrackerCallback extends BaseWifiTracker.BaseWifiTrackerCallback {
        void onSavedWifiEntriesChanged();

        void onSubscriptionWifiEntriesChanged();
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public SavedNetworkTracker(WifiTrackerInjector wifiTrackerInjector, Lifecycle lifecycle, Context context, WifiManager wifiManager, ConnectivityManager connectivityManager, Handler handler, Handler handler2, Clock clock, long j, long j2, SavedNetworkTrackerCallback savedNetworkTrackerCallback) {
        super(wifiTrackerInjector, lifecycle, context, wifiManager, connectivityManager, handler, handler2, clock, j, j2, savedNetworkTrackerCallback, "SavedNetworkTracker");
        this.mListener = savedNetworkTrackerCallback;
    }

    public void handleOnStart() {
        updateStandardWifiEntryConfigs(this.mWifiManager.getConfiguredNetworks());
        updatePasspointWifiEntryConfigs(this.mWifiManager.getPasspointConfigurations());
        boolean z = true;
        conditionallyUpdateScanResults(true);
        WifiInfo connectionInfo = this.mWifiManager.getConnectionInfo();
        Network currentNetwork = this.mWifiManager.getCurrentNetwork();
        NetworkInfo networkInfo = this.mConnectivityManager.getNetworkInfo(currentNetwork);
        this.mCurrentNetworkInfo = networkInfo;
        updateConnectionInfo(connectionInfo, networkInfo);
        updateWifiEntries();
        handleNetworkCapabilitiesChanged(this.mConnectivityManager.getNetworkCapabilities(currentNetwork));
        handleLinkPropertiesChanged(this.mConnectivityManager.getLinkProperties(currentNetwork));
        WifiEntry wifiEntry = this.mConnectedWifiEntry;
        if (wifiEntry != null) {
            wifiEntry.setIsDefaultNetwork(this.mIsWifiDefaultRoute);
            WifiEntry wifiEntry2 = this.mConnectedWifiEntry;
            if (!this.mIsWifiValidated || !this.mIsCellDefaultRoute) {
                z = false;
            }
            wifiEntry2.setIsLowQuality(z);
        }
    }

    public void handleWifiStateChangedAction() {
        conditionallyUpdateScanResults(true);
        updateWifiEntries();
    }

    public void handleScanResultsAvailableAction(Intent intent) {
        Preconditions.checkNotNull(intent, "Intent cannot be null!");
        conditionallyUpdateScanResults(intent.getBooleanExtra("resultsUpdated", true));
        updateWifiEntries();
    }

    public void handleConfiguredNetworksChangedAction(Intent intent) {
        Preconditions.checkNotNull(intent, "Intent cannot be null!");
        updateStandardWifiEntryConfigs(this.mWifiManager.getConfiguredNetworks());
        updatePasspointWifiEntryConfigs(this.mWifiManager.getPasspointConfigurations());
        updateWifiEntries();
    }

    public void handleNetworkStateChangedAction(Intent intent) {
        Preconditions.checkNotNull(intent, "Intent cannot be null!");
        this.mCurrentNetworkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
        updateConnectionInfo(this.mWifiManager.getConnectionInfo(), this.mCurrentNetworkInfo);
    }

    public void handleRssiChangedAction() {
        WifiInfo connectionInfo = this.mWifiManager.getConnectionInfo();
        WifiEntry wifiEntry = this.mConnectedWifiEntry;
        if (wifiEntry != null) {
            wifiEntry.updateConnectionInfo(connectionInfo, this.mCurrentNetworkInfo);
        }
    }

    public void handleLinkPropertiesChanged(LinkProperties linkProperties) {
        WifiEntry wifiEntry = this.mConnectedWifiEntry;
        if (wifiEntry != null && wifiEntry.getConnectedState() == 2) {
            this.mConnectedWifiEntry.updateLinkProperties(linkProperties);
        }
    }

    public void handleNetworkCapabilitiesChanged(NetworkCapabilities networkCapabilities) {
        WifiEntry wifiEntry = this.mConnectedWifiEntry;
        if (wifiEntry != null && wifiEntry.getConnectedState() == 2) {
            this.mConnectedWifiEntry.updateNetworkCapabilities(networkCapabilities);
            this.mConnectedWifiEntry.setIsLowQuality(this.mIsWifiValidated && this.mIsCellDefaultRoute);
        }
    }

    public void handleDefaultRouteChanged() {
        WifiEntry wifiEntry = this.mConnectedWifiEntry;
        if (wifiEntry != null) {
            wifiEntry.setIsDefaultNetwork(this.mIsWifiDefaultRoute);
            this.mConnectedWifiEntry.setIsLowQuality(this.mIsWifiValidated && this.mIsCellDefaultRoute);
        }
    }

    public final void updateWifiEntries() {
        synchronized (this.mLock) {
            this.mConnectedWifiEntry = null;
            for (WifiEntry next : this.mStandardWifiEntryCache) {
                if (next.getConnectedState() != 0) {
                    this.mConnectedWifiEntry = next;
                }
            }
            for (WifiEntry next2 : this.mSubscriptionWifiEntries) {
                if (next2.getConnectedState() != 0) {
                    this.mConnectedWifiEntry = next2;
                }
            }
            WifiEntry wifiEntry = this.mConnectedWifiEntry;
            if (wifiEntry != null) {
                wifiEntry.setIsDefaultNetwork(this.mIsWifiDefaultRoute);
            }
            this.mSavedWifiEntries.clear();
            this.mSavedWifiEntries.addAll(this.mStandardWifiEntryCache);
            Collections.sort(this.mSavedWifiEntries, WifiEntry.TITLE_COMPARATOR);
            this.mSubscriptionWifiEntries.clear();
            this.mSubscriptionWifiEntries.addAll(this.mPasspointWifiEntryCache.values());
            Collections.sort(this.mSubscriptionWifiEntries, WifiEntry.TITLE_COMPARATOR);
            if (BaseWifiTracker.isVerboseLoggingEnabled()) {
                Log.v("SavedNetworkTracker", "Updated SavedWifiEntries: " + Arrays.toString(this.mSavedWifiEntries.toArray()));
                Log.v("SavedNetworkTracker", "Updated SubscriptionWifiEntries: " + Arrays.toString(this.mSubscriptionWifiEntries.toArray()));
            }
        }
        notifyOnSavedWifiEntriesChanged();
        notifyOnSubscriptionWifiEntriesChanged();
    }

    public final void updateStandardWifiEntryScans(List<ScanResult> list) {
        Preconditions.checkNotNull(list, "Scan Result list should not be null!");
        this.mStandardWifiEntryCache.forEach(new SavedNetworkTracker$$ExternalSyntheticLambda7((Map) list.stream().collect(Collectors.groupingBy(new SavedNetworkTracker$$ExternalSyntheticLambda6()))));
    }

    public final void updatePasspointWifiEntryScans(List<ScanResult> list) {
        Preconditions.checkNotNull(list, "Scan Result list should not be null!");
        TreeSet treeSet = new TreeSet();
        for (Pair pair : this.mWifiManager.getAllMatchingWifiConfigs(list)) {
            WifiConfiguration wifiConfiguration = (WifiConfiguration) pair.first;
            String uniqueIdToPasspointWifiEntryKey = PasspointWifiEntry.uniqueIdToPasspointWifiEntryKey(wifiConfiguration.getKey());
            treeSet.add(uniqueIdToPasspointWifiEntryKey);
            if (this.mPasspointWifiEntryCache.containsKey(uniqueIdToPasspointWifiEntryKey)) {
                this.mPasspointWifiEntryCache.get(uniqueIdToPasspointWifiEntryKey).updateScanResultInfo(wifiConfiguration, (List) ((Map) pair.second).get(0), (List) ((Map) pair.second).get(1));
            }
        }
        for (PasspointWifiEntry next : this.mPasspointWifiEntryCache.values()) {
            if (!treeSet.contains(next.getKey())) {
                next.updateScanResultInfo((WifiConfiguration) null, (List<ScanResult>) null, (List<ScanResult>) null);
            }
        }
    }

    public final void conditionallyUpdateScanResults(boolean z) {
        if (this.mWifiManager.getWifiState() == 1) {
            updateStandardWifiEntryScans(Collections.emptyList());
            updatePasspointWifiEntryScans(Collections.emptyList());
            return;
        }
        long j = this.mMaxScanAgeMillis;
        if (z) {
            this.mScanResultUpdater.update(this.mWifiManager.getScanResults());
        } else {
            j += this.mScanIntervalMillis;
        }
        updateStandardWifiEntryScans(this.mScanResultUpdater.getScanResults(j));
        updatePasspointWifiEntryScans(this.mScanResultUpdater.getScanResults(j));
    }

    public final void updateStandardWifiEntryConfigs(List<WifiConfiguration> list) {
        Preconditions.checkNotNull(list, "Config list should not be null!");
        Map map = (Map) list.stream().filter(new SavedNetworkTracker$$ExternalSyntheticLambda2()).collect(Collectors.groupingBy(new SavedNetworkTracker$$ExternalSyntheticLambda3()));
        this.mStandardWifiEntryCache.removeIf(new SavedNetworkTracker$$ExternalSyntheticLambda4(map));
        for (StandardWifiEntry.StandardWifiEntryKey standardWifiEntryKey : map.keySet()) {
            this.mStandardWifiEntryCache.add(new StandardWifiEntry(this.mInjector, this.mContext, this.mMainHandler, standardWifiEntryKey, (List) map.get(standardWifiEntryKey), (List<ScanResult>) null, this.mWifiManager, true));
        }
    }

    public static /* synthetic */ boolean lambda$updateStandardWifiEntryConfigs$1(WifiConfiguration wifiConfiguration) {
        return !wifiConfiguration.carrierMerged;
    }

    public final void updatePasspointWifiEntryConfigs(List<PasspointConfiguration> list) {
        Preconditions.checkNotNull(list, "Config list should not be null!");
        Map map = (Map) list.stream().collect(Collectors.toMap(new SavedNetworkTracker$$ExternalSyntheticLambda0(), Function.identity()));
        this.mPasspointWifiEntryCache.entrySet().removeIf(new SavedNetworkTracker$$ExternalSyntheticLambda1(map));
        for (String str : map.keySet()) {
            this.mPasspointWifiEntryCache.put(str, new PasspointWifiEntry(this.mInjector, this.mContext, this.mMainHandler, (PasspointConfiguration) map.get(str), this.mWifiManager, true));
        }
    }

    public static /* synthetic */ boolean lambda$updatePasspointWifiEntryConfigs$4(Map map, Map.Entry entry) {
        PasspointWifiEntry passpointWifiEntry = (PasspointWifiEntry) entry.getValue();
        PasspointConfiguration passpointConfiguration = (PasspointConfiguration) map.remove(passpointWifiEntry.getKey());
        if (passpointConfiguration == null) {
            return true;
        }
        passpointWifiEntry.updatePasspointConfig(passpointConfiguration);
        return false;
    }

    public final void updateConnectionInfo(WifiInfo wifiInfo, NetworkInfo networkInfo) {
        for (WifiEntry next : this.mStandardWifiEntryCache) {
            next.updateConnectionInfo(wifiInfo, networkInfo);
            if (next.getConnectedState() != 0) {
                this.mConnectedWifiEntry = next;
            }
        }
        for (WifiEntry next2 : this.mPasspointWifiEntryCache.values()) {
            next2.updateConnectionInfo(wifiInfo, networkInfo);
            if (next2.getConnectedState() != 0) {
                this.mConnectedWifiEntry = next2;
            }
        }
    }

    public final void notifyOnSavedWifiEntriesChanged() {
        SavedNetworkTrackerCallback savedNetworkTrackerCallback = this.mListener;
        if (savedNetworkTrackerCallback != null) {
            Handler handler = this.mMainHandler;
            Objects.requireNonNull(savedNetworkTrackerCallback);
            handler.post(new SavedNetworkTracker$$ExternalSyntheticLambda5(savedNetworkTrackerCallback));
        }
    }

    public final void notifyOnSubscriptionWifiEntriesChanged() {
        SavedNetworkTrackerCallback savedNetworkTrackerCallback = this.mListener;
        if (savedNetworkTrackerCallback != null) {
            Handler handler = this.mMainHandler;
            Objects.requireNonNull(savedNetworkTrackerCallback);
            handler.post(new SavedNetworkTracker$$ExternalSyntheticLambda8(savedNetworkTrackerCallback));
        }
    }
}
