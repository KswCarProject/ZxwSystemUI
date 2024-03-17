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
import android.net.wifi.hotspot2.OsuProvider;
import android.net.wifi.hotspot2.PasspointConfiguration;
import android.os.Handler;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import androidx.core.util.Preconditions;
import androidx.lifecycle.Lifecycle;
import com.android.wifitrackerlib.BaseWifiTracker;
import com.android.wifitrackerlib.StandardWifiEntry;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

public class WifiPickerTracker extends BaseWifiTracker {
    public WifiEntry mConnectedWifiEntry;
    public NetworkInfo mCurrentNetworkInfo;
    public final WifiPickerTrackerCallback mListener;
    public final Object mLock;
    public MergedCarrierEntry mMergedCarrierEntry;
    public final ArrayMap<StandardWifiEntry.StandardWifiEntryKey, List<WifiConfiguration>> mNetworkRequestConfigCache;
    public NetworkRequestEntry mNetworkRequestEntry;
    public int mNumSavedNetworks;
    public final Map<String, OsuWifiEntry> mOsuWifiEntryCache;
    public final Map<String, PasspointConfiguration> mPasspointConfigCache;
    public final SparseArray<WifiConfiguration> mPasspointWifiConfigCache;
    public final Map<String, PasspointWifiEntry> mPasspointWifiEntryCache;
    public final Map<StandardWifiEntry.StandardWifiEntryKey, List<WifiConfiguration>> mStandardWifiConfigCache;
    public final List<StandardWifiEntry> mStandardWifiEntryCache;
    public final Map<StandardWifiEntry.StandardWifiEntryKey, List<WifiConfiguration>> mSuggestedConfigCache;
    public final List<StandardWifiEntry> mSuggestedWifiEntryCache;
    public final List<WifiEntry> mWifiEntries;

    public interface WifiPickerTrackerCallback extends BaseWifiTracker.BaseWifiTrackerCallback {
        void onNumSavedNetworksChanged();

        void onNumSavedSubscriptionsChanged();

        void onWifiEntriesChanged();
    }

    public void updateContextualWifiEntryScans(List<ScanResult> list) {
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public WifiPickerTracker(androidx.lifecycle.Lifecycle r15, android.content.Context r16, android.net.wifi.WifiManager r17, android.net.ConnectivityManager r18, android.os.Handler r19, android.os.Handler r20, java.time.Clock r21, long r22, long r24, com.android.wifitrackerlib.WifiPickerTracker.WifiPickerTrackerCallback r26) {
        /*
            r14 = this;
            com.android.wifitrackerlib.WifiTrackerInjector r1 = new com.android.wifitrackerlib.WifiTrackerInjector
            r3 = r16
            r1.<init>(r3)
            r0 = r14
            r2 = r15
            r4 = r17
            r5 = r18
            r6 = r19
            r7 = r20
            r8 = r21
            r9 = r22
            r11 = r24
            r13 = r26
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8, r9, r11, r13)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.WifiPickerTracker.<init>(androidx.lifecycle.Lifecycle, android.content.Context, android.net.wifi.WifiManager, android.net.ConnectivityManager, android.os.Handler, android.os.Handler, java.time.Clock, long, long, com.android.wifitrackerlib.WifiPickerTracker$WifiPickerTrackerCallback):void");
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public WifiPickerTracker(WifiTrackerInjector wifiTrackerInjector, Lifecycle lifecycle, Context context, WifiManager wifiManager, ConnectivityManager connectivityManager, Handler handler, Handler handler2, Clock clock, long j, long j2, WifiPickerTrackerCallback wifiPickerTrackerCallback) {
        super(wifiTrackerInjector, lifecycle, context, wifiManager, connectivityManager, handler, handler2, clock, j, j2, wifiPickerTrackerCallback, "WifiPickerTracker");
        this.mLock = new Object();
        this.mWifiEntries = new ArrayList();
        this.mStandardWifiConfigCache = new ArrayMap();
        this.mSuggestedConfigCache = new ArrayMap();
        this.mNetworkRequestConfigCache = new ArrayMap<>();
        this.mStandardWifiEntryCache = new ArrayList();
        this.mSuggestedWifiEntryCache = new ArrayList();
        this.mPasspointConfigCache = new ArrayMap();
        this.mPasspointWifiConfigCache = new SparseArray<>();
        this.mPasspointWifiEntryCache = new ArrayMap();
        this.mOsuWifiEntryCache = new ArrayMap();
        this.mListener = wifiPickerTrackerCallback;
    }

    public WifiEntry getConnectedWifiEntry() {
        return this.mConnectedWifiEntry;
    }

    public List<WifiEntry> getWifiEntries() {
        ArrayList arrayList;
        synchronized (this.mLock) {
            arrayList = new ArrayList(this.mWifiEntries);
        }
        return arrayList;
    }

    public MergedCarrierEntry getMergedCarrierEntry() {
        int defaultDataSubscriptionId;
        if (!isInitialized() && this.mMergedCarrierEntry == null && (defaultDataSubscriptionId = SubscriptionManager.getDefaultDataSubscriptionId()) != -1) {
            this.mMergedCarrierEntry = new MergedCarrierEntry(this.mWorkerHandler, this.mWifiManager, false, this.mContext, defaultDataSubscriptionId);
        }
        return this.mMergedCarrierEntry;
    }

    public void handleOnStart() {
        updateWifiConfigurations(this.mWifiManager.getPrivilegedConfiguredNetworks());
        updatePasspointConfigurations(this.mWifiManager.getPasspointConfigurations());
        this.mScanResultUpdater.update(this.mWifiManager.getScanResults());
        conditionallyUpdateScanResults(true);
        WifiInfo connectionInfo = this.mWifiManager.getConnectionInfo();
        Network currentNetwork = this.mWifiManager.getCurrentNetwork();
        NetworkInfo networkInfo = this.mConnectivityManager.getNetworkInfo(currentNetwork);
        this.mCurrentNetworkInfo = networkInfo;
        updateConnectionInfo(connectionInfo, networkInfo);
        notifyOnNumSavedNetworksChanged();
        notifyOnNumSavedSubscriptionsChanged();
        handleDefaultSubscriptionChanged(SubscriptionManager.getDefaultDataSubscriptionId());
        updateWifiEntries();
        handleNetworkCapabilitiesChanged(this.mConnectivityManager.getNetworkCapabilities(currentNetwork));
        handleLinkPropertiesChanged(this.mConnectivityManager.getLinkProperties(currentNetwork));
        handleDefaultRouteChanged();
    }

    public void handleWifiStateChangedAction() {
        if (this.mWifiManager.getWifiState() == 1) {
            updateConnectionInfo((WifiInfo) null, (NetworkInfo) null);
        }
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
        processConfiguredNetworksChanged();
    }

    public void processConfiguredNetworksChanged() {
        updateWifiConfigurations(this.mWifiManager.getPrivilegedConfiguredNetworks());
        updatePasspointConfigurations(this.mWifiManager.getPasspointConfigurations());
        List<ScanResult> scanResults = this.mScanResultUpdater.getScanResults();
        updateStandardWifiEntryScans(scanResults);
        updateNetworkRequestEntryScans(scanResults);
        updatePasspointWifiEntryScans(scanResults);
        updateOsuWifiEntryScans(scanResults);
        notifyOnNumSavedNetworksChanged();
        notifyOnNumSavedSubscriptionsChanged();
        updateWifiEntries();
    }

    public void handleNetworkStateChangedAction(Intent intent) {
        Preconditions.checkNotNull(intent, "Intent cannot be null!");
        this.mCurrentNetworkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
        updateConnectionInfo(this.mWifiManager.getConnectionInfo(), this.mCurrentNetworkInfo);
        updateWifiEntries();
    }

    public void handleRssiChangedAction() {
        WifiInfo connectionInfo = this.mWifiManager.getConnectionInfo();
        WifiEntry wifiEntry = this.mConnectedWifiEntry;
        if (wifiEntry != null) {
            wifiEntry.updateConnectionInfo(connectionInfo, this.mCurrentNetworkInfo);
        }
        MergedCarrierEntry mergedCarrierEntry = this.mMergedCarrierEntry;
        if (mergedCarrierEntry != null) {
            mergedCarrierEntry.updateConnectionInfo(connectionInfo, this.mCurrentNetworkInfo);
        }
    }

    public void handleLinkPropertiesChanged(LinkProperties linkProperties) {
        WifiEntry wifiEntry = this.mConnectedWifiEntry;
        if (wifiEntry != null && wifiEntry.getConnectedState() == 2) {
            this.mConnectedWifiEntry.updateLinkProperties(linkProperties);
        }
        MergedCarrierEntry mergedCarrierEntry = this.mMergedCarrierEntry;
        if (mergedCarrierEntry != null) {
            mergedCarrierEntry.updateLinkProperties(linkProperties);
        }
    }

    public void handleNetworkCapabilitiesChanged(NetworkCapabilities networkCapabilities) {
        WifiEntry wifiEntry = this.mConnectedWifiEntry;
        if (wifiEntry != null && wifiEntry.getConnectedState() == 2) {
            this.mConnectedWifiEntry.updateNetworkCapabilities(networkCapabilities);
            this.mConnectedWifiEntry.setIsLowQuality(this.mIsWifiValidated && this.mIsCellDefaultRoute);
        }
        MergedCarrierEntry mergedCarrierEntry = this.mMergedCarrierEntry;
        if (mergedCarrierEntry != null) {
            mergedCarrierEntry.updateNetworkCapabilities(networkCapabilities);
        }
    }

    public void handleDefaultRouteChanged() {
        WifiEntry wifiEntry = this.mConnectedWifiEntry;
        if (wifiEntry != null) {
            wifiEntry.setIsDefaultNetwork(this.mIsWifiDefaultRoute);
            this.mConnectedWifiEntry.setIsLowQuality(this.mIsWifiValidated && this.mIsCellDefaultRoute);
        }
        MergedCarrierEntry mergedCarrierEntry = this.mMergedCarrierEntry;
        if (mergedCarrierEntry != null) {
            if (mergedCarrierEntry.getConnectedState() == 2) {
                this.mMergedCarrierEntry.setIsDefaultNetwork(this.mIsWifiDefaultRoute);
            }
            this.mMergedCarrierEntry.updateIsCellDefaultRoute(this.mIsCellDefaultRoute);
        }
    }

    public void handleDefaultSubscriptionChanged(int i) {
        updateMergedCarrierEntry(i);
    }

    public void updateWifiEntries() {
        NetworkRequestEntry networkRequestEntry;
        synchronized (this.mLock) {
            WifiEntry wifiEntry = (WifiEntry) this.mStandardWifiEntryCache.stream().filter(new WifiPickerTracker$$ExternalSyntheticLambda0()).findAny().orElse((Object) null);
            this.mConnectedWifiEntry = wifiEntry;
            if (wifiEntry == null) {
                this.mConnectedWifiEntry = (WifiEntry) this.mSuggestedWifiEntryCache.stream().filter(new WifiPickerTracker$$ExternalSyntheticLambda1()).findAny().orElse((Object) null);
            }
            if (this.mConnectedWifiEntry == null) {
                this.mConnectedWifiEntry = this.mPasspointWifiEntryCache.values().stream().filter(new WifiPickerTracker$$ExternalSyntheticLambda2()).findAny().orElse((Object) null);
            }
            if (!(this.mConnectedWifiEntry != null || (networkRequestEntry = this.mNetworkRequestEntry) == null || networkRequestEntry.getConnectedState() == 0)) {
                this.mConnectedWifiEntry = this.mNetworkRequestEntry;
            }
            WifiEntry wifiEntry2 = this.mConnectedWifiEntry;
            if (wifiEntry2 != null) {
                wifiEntry2.setIsDefaultNetwork(this.mIsWifiDefaultRoute);
            }
            this.mWifiEntries.clear();
            Set set = (Set) this.mSuggestedWifiEntryCache.stream().filter(new WifiPickerTracker$$ExternalSyntheticLambda3(this)).map(new WifiPickerTracker$$ExternalSyntheticLambda4()).collect(Collectors.toSet());
            ArraySet arraySet = new ArraySet();
            for (PasspointWifiEntry allUtf8Ssids : this.mPasspointWifiEntryCache.values()) {
                arraySet.addAll(allUtf8Ssids.getAllUtf8Ssids());
            }
            for (StandardWifiEntry next : this.mStandardWifiEntryCache) {
                next.updateAdminRestrictions();
                if (next != this.mConnectedWifiEntry) {
                    if (!next.isSaved()) {
                        if (!set.contains(next.getStandardWifiEntryKey().getScanResultKey())) {
                            if (arraySet.contains(next.getSsid())) {
                            }
                        }
                    }
                    this.mWifiEntries.add(next);
                }
            }
            this.mWifiEntries.addAll((Collection) this.mSuggestedWifiEntryCache.stream().filter(new WifiPickerTracker$$ExternalSyntheticLambda5()).collect(Collectors.toList()));
            this.mWifiEntries.addAll((Collection) this.mPasspointWifiEntryCache.values().stream().filter(new WifiPickerTracker$$ExternalSyntheticLambda6()).collect(Collectors.toList()));
            this.mWifiEntries.addAll((Collection) this.mOsuWifiEntryCache.values().stream().filter(new WifiPickerTracker$$ExternalSyntheticLambda7()).collect(Collectors.toList()));
            this.mWifiEntries.addAll((Collection) getContextualWifiEntries().stream().filter(new WifiPickerTracker$$ExternalSyntheticLambda8()).collect(Collectors.toList()));
            Collections.sort(this.mWifiEntries, WifiEntry.WIFI_PICKER_COMPARATOR);
            if (BaseWifiTracker.isVerboseLoggingEnabled()) {
                Log.v("WifiPickerTracker", "Connected WifiEntry: " + this.mConnectedWifiEntry);
                Log.v("WifiPickerTracker", "Updated WifiEntries: " + Arrays.toString(this.mWifiEntries.toArray()));
            }
        }
        notifyOnWifiEntriesChanged();
    }

    public static /* synthetic */ boolean lambda$updateWifiEntries$0(StandardWifiEntry standardWifiEntry) {
        int connectedState = standardWifiEntry.getConnectedState();
        return connectedState == 2 || connectedState == 1;
    }

    public static /* synthetic */ boolean lambda$updateWifiEntries$1(StandardWifiEntry standardWifiEntry) {
        int connectedState = standardWifiEntry.getConnectedState();
        return connectedState == 2 || connectedState == 1;
    }

    public static /* synthetic */ boolean lambda$updateWifiEntries$2(PasspointWifiEntry passpointWifiEntry) {
        int connectedState = passpointWifiEntry.getConnectedState();
        return connectedState == 2 || connectedState == 1;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$updateWifiEntries$3(StandardWifiEntry standardWifiEntry) {
        return standardWifiEntry.isUserShareable() || standardWifiEntry == this.mConnectedWifiEntry;
    }

    public static /* synthetic */ boolean lambda$updateWifiEntries$5(StandardWifiEntry standardWifiEntry) {
        return standardWifiEntry.getConnectedState() == 0 && standardWifiEntry.isUserShareable();
    }

    public static /* synthetic */ boolean lambda$updateWifiEntries$6(PasspointWifiEntry passpointWifiEntry) {
        return passpointWifiEntry.getConnectedState() == 0;
    }

    public static /* synthetic */ boolean lambda$updateWifiEntries$7(OsuWifiEntry osuWifiEntry) {
        return osuWifiEntry.getConnectedState() == 0 && !osuWifiEntry.isAlreadyProvisioned();
    }

    public static /* synthetic */ boolean lambda$updateWifiEntries$8(WifiEntry wifiEntry) {
        return wifiEntry.getConnectedState() == 0;
    }

    public final void updateMergedCarrierEntry(int i) {
        if (i != -1) {
            MergedCarrierEntry mergedCarrierEntry = this.mMergedCarrierEntry;
            if (mergedCarrierEntry == null || i != mergedCarrierEntry.getSubscriptionId()) {
                MergedCarrierEntry mergedCarrierEntry2 = new MergedCarrierEntry(this.mWorkerHandler, this.mWifiManager, false, this.mContext, i);
                this.mMergedCarrierEntry = mergedCarrierEntry2;
                mergedCarrierEntry2.updateConnectionInfo(this.mWifiManager.getConnectionInfo(), this.mCurrentNetworkInfo);
            } else {
                return;
            }
        } else if (this.mMergedCarrierEntry != null) {
            this.mMergedCarrierEntry = null;
        } else {
            return;
        }
        notifyOnWifiEntriesChanged();
    }

    public List<WifiEntry> getContextualWifiEntries() {
        return Collections.emptyList();
    }

    public final void updateStandardWifiEntryScans(List<ScanResult> list) {
        Preconditions.checkNotNull(list, "Scan Result list should not be null!");
        Map map = (Map) list.stream().filter(new WifiPickerTracker$$ExternalSyntheticLambda21()).collect(Collectors.groupingBy(new SavedNetworkTracker$$ExternalSyntheticLambda6()));
        ArraySet<StandardWifiEntry.ScanResultKey> arraySet = new ArraySet<>(map.keySet());
        this.mStandardWifiEntryCache.forEach(new WifiPickerTracker$$ExternalSyntheticLambda22(arraySet, map));
        for (StandardWifiEntry.ScanResultKey scanResultKey : arraySet) {
            StandardWifiEntry.StandardWifiEntryKey standardWifiEntryKey = new StandardWifiEntry.StandardWifiEntryKey(scanResultKey, true);
            this.mStandardWifiEntryCache.add(new StandardWifiEntry(this.mInjector, this.mContext, this.mMainHandler, standardWifiEntryKey, this.mStandardWifiConfigCache.get(standardWifiEntryKey), (List) map.get(scanResultKey), this.mWifiManager, false));
        }
        this.mStandardWifiEntryCache.removeIf(new WifiPickerTracker$$ExternalSyntheticLambda23());
    }

    public static /* synthetic */ boolean lambda$updateStandardWifiEntryScans$9(ScanResult scanResult) {
        return !TextUtils.isEmpty(scanResult.SSID);
    }

    public static /* synthetic */ void lambda$updateStandardWifiEntryScans$10(Set set, Map map, StandardWifiEntry standardWifiEntry) {
        StandardWifiEntry.ScanResultKey scanResultKey = standardWifiEntry.getStandardWifiEntryKey().getScanResultKey();
        set.remove(scanResultKey);
        standardWifiEntry.updateScanResultInfo((List) map.get(scanResultKey));
    }

    public static /* synthetic */ boolean lambda$updateStandardWifiEntryScans$11(StandardWifiEntry standardWifiEntry) {
        return standardWifiEntry.getLevel() == -1;
    }

    public final void updateSuggestedWifiEntryScans(List<ScanResult> list) {
        Preconditions.checkNotNull(list, "Scan Result list should not be null!");
        Set set = (Set) this.mWifiManager.getWifiConfigForMatchedNetworkSuggestionsSharedWithUser(list).stream().map(new SavedNetworkTracker$$ExternalSyntheticLambda3()).collect(Collectors.toSet());
        Map map = (Map) list.stream().filter(new WifiPickerTracker$$ExternalSyntheticLambda24()).collect(Collectors.groupingBy(new SavedNetworkTracker$$ExternalSyntheticLambda6()));
        ArraySet arraySet = new ArraySet();
        this.mSuggestedWifiEntryCache.forEach(new WifiPickerTracker$$ExternalSyntheticLambda25(arraySet, map, set));
        for (StandardWifiEntry.StandardWifiEntryKey next : this.mSuggestedConfigCache.keySet()) {
            StandardWifiEntry.ScanResultKey scanResultKey = next.getScanResultKey();
            if (!arraySet.contains(next) && map.containsKey(scanResultKey)) {
                StandardWifiEntry standardWifiEntry = new StandardWifiEntry(this.mInjector, this.mContext, this.mMainHandler, next, this.mSuggestedConfigCache.get(next), (List) map.get(scanResultKey), this.mWifiManager, false);
                standardWifiEntry.setUserShareable(set.contains(next));
                this.mSuggestedWifiEntryCache.add(standardWifiEntry);
            }
        }
        this.mSuggestedWifiEntryCache.removeIf(new WifiPickerTracker$$ExternalSyntheticLambda26());
    }

    public static /* synthetic */ boolean lambda$updateSuggestedWifiEntryScans$12(ScanResult scanResult) {
        return !TextUtils.isEmpty(scanResult.SSID);
    }

    public static /* synthetic */ void lambda$updateSuggestedWifiEntryScans$13(Set set, Map map, Set set2, StandardWifiEntry standardWifiEntry) {
        StandardWifiEntry.StandardWifiEntryKey standardWifiEntryKey = standardWifiEntry.getStandardWifiEntryKey();
        set.add(standardWifiEntryKey);
        standardWifiEntry.updateScanResultInfo((List) map.get(standardWifiEntryKey.getScanResultKey()));
        standardWifiEntry.setUserShareable(set2.contains(standardWifiEntryKey));
    }

    public static /* synthetic */ boolean lambda$updateSuggestedWifiEntryScans$14(StandardWifiEntry standardWifiEntry) {
        return standardWifiEntry.getLevel() == -1;
    }

    public final void updatePasspointWifiEntryScans(List<ScanResult> list) {
        List<ScanResult> list2 = list;
        Preconditions.checkNotNull(list2, "Scan Result list should not be null!");
        TreeSet treeSet = new TreeSet();
        for (Pair pair : this.mWifiManager.getAllMatchingWifiConfigs(list2)) {
            WifiConfiguration wifiConfiguration = (WifiConfiguration) pair.first;
            List list3 = (List) ((Map) pair.second).get(0);
            List list4 = (List) ((Map) pair.second).get(1);
            String uniqueIdToPasspointWifiEntryKey = PasspointWifiEntry.uniqueIdToPasspointWifiEntryKey(wifiConfiguration.getKey());
            treeSet.add(uniqueIdToPasspointWifiEntryKey);
            if (!this.mPasspointWifiEntryCache.containsKey(uniqueIdToPasspointWifiEntryKey)) {
                if (wifiConfiguration.fromWifiNetworkSuggestion) {
                    this.mPasspointWifiEntryCache.put(uniqueIdToPasspointWifiEntryKey, new PasspointWifiEntry(this.mInjector, this.mContext, this.mMainHandler, wifiConfiguration, this.mWifiManager, false));
                } else if (this.mPasspointConfigCache.containsKey(uniqueIdToPasspointWifiEntryKey)) {
                    Map<String, PasspointWifiEntry> map = this.mPasspointWifiEntryCache;
                    WifiTrackerInjector wifiTrackerInjector = this.mInjector;
                    Context context = this.mContext;
                    map.put(uniqueIdToPasspointWifiEntryKey, new PasspointWifiEntry(wifiTrackerInjector, context, this.mMainHandler, this.mPasspointConfigCache.get(uniqueIdToPasspointWifiEntryKey), this.mWifiManager, false));
                }
            }
            this.mPasspointWifiEntryCache.get(uniqueIdToPasspointWifiEntryKey).updateScanResultInfo(wifiConfiguration, list3, list4);
        }
        this.mPasspointWifiEntryCache.entrySet().removeIf(new WifiPickerTracker$$ExternalSyntheticLambda19(treeSet));
    }

    public static /* synthetic */ boolean lambda$updatePasspointWifiEntryScans$15(Set set, Map.Entry entry) {
        return ((PasspointWifiEntry) entry.getValue()).getLevel() == -1 || (!set.contains(entry.getKey()) && ((PasspointWifiEntry) entry.getValue()).getConnectedState() == 0);
    }

    public final void updateOsuWifiEntryScans(List<ScanResult> list) {
        Preconditions.checkNotNull(list, "Scan Result list should not be null!");
        Map matchingOsuProviders = this.mWifiManager.getMatchingOsuProviders(list);
        Map matchingPasspointConfigsForOsuProviders = this.mWifiManager.getMatchingPasspointConfigsForOsuProviders(matchingOsuProviders.keySet());
        for (OsuWifiEntry next : this.mOsuWifiEntryCache.values()) {
            next.updateScanResultInfo((List) matchingOsuProviders.remove(next.getOsuProvider()));
        }
        for (OsuProvider osuProvider : matchingOsuProviders.keySet()) {
            OsuWifiEntry osuWifiEntry = new OsuWifiEntry(this.mInjector, this.mContext, this.mMainHandler, osuProvider, this.mWifiManager, false);
            osuWifiEntry.updateScanResultInfo((List) matchingOsuProviders.get(osuProvider));
            this.mOsuWifiEntryCache.put(OsuWifiEntry.osuProviderToOsuWifiEntryKey(osuProvider), osuWifiEntry);
        }
        this.mOsuWifiEntryCache.values().forEach(new WifiPickerTracker$$ExternalSyntheticLambda27(this, matchingPasspointConfigsForOsuProviders));
        this.mOsuWifiEntryCache.entrySet().removeIf(new WifiPickerTracker$$ExternalSyntheticLambda28());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateOsuWifiEntryScans$16(Map map, OsuWifiEntry osuWifiEntry) {
        PasspointConfiguration passpointConfiguration = (PasspointConfiguration) map.get(osuWifiEntry.getOsuProvider());
        if (passpointConfiguration == null) {
            osuWifiEntry.setAlreadyProvisioned(false);
            return;
        }
        osuWifiEntry.setAlreadyProvisioned(true);
        PasspointWifiEntry passpointWifiEntry = this.mPasspointWifiEntryCache.get(PasspointWifiEntry.uniqueIdToPasspointWifiEntryKey(passpointConfiguration.getUniqueId()));
        if (passpointWifiEntry != null) {
            passpointWifiEntry.setOsuWifiEntry(osuWifiEntry);
        }
    }

    public static /* synthetic */ boolean lambda$updateOsuWifiEntryScans$17(Map.Entry entry) {
        return ((OsuWifiEntry) entry.getValue()).getLevel() == -1;
    }

    public final void updateNetworkRequestEntryScans(List<ScanResult> list) {
        Preconditions.checkNotNull(list, "Scan Result list should not be null!");
        NetworkRequestEntry networkRequestEntry = this.mNetworkRequestEntry;
        if (networkRequestEntry != null) {
            StandardWifiEntry.ScanResultKey scanResultKey = networkRequestEntry.getStandardWifiEntryKey().getScanResultKey();
            this.mNetworkRequestEntry.updateScanResultInfo((List) list.stream().filter(new WifiPickerTracker$$ExternalSyntheticLambda18(scanResultKey)).collect(Collectors.toList()));
        }
    }

    public final void conditionallyUpdateScanResults(boolean z) {
        if (this.mWifiManager.getWifiState() == 1) {
            updateStandardWifiEntryScans(Collections.emptyList());
            updateSuggestedWifiEntryScans(Collections.emptyList());
            updatePasspointWifiEntryScans(Collections.emptyList());
            updateOsuWifiEntryScans(Collections.emptyList());
            updateNetworkRequestEntryScans(Collections.emptyList());
            updateContextualWifiEntryScans(Collections.emptyList());
            return;
        }
        long j = this.mMaxScanAgeMillis;
        if (z) {
            this.mScanResultUpdater.update(this.mWifiManager.getScanResults());
        } else {
            j += this.mScanIntervalMillis;
        }
        List<ScanResult> scanResults = this.mScanResultUpdater.getScanResults(j);
        updateStandardWifiEntryScans(scanResults);
        updateSuggestedWifiEntryScans(scanResults);
        updatePasspointWifiEntryScans(scanResults);
        updateOsuWifiEntryScans(scanResults);
        updateNetworkRequestEntryScans(scanResults);
        updateContextualWifiEntryScans(scanResults);
    }

    public final void updateWifiConfigurations(List<WifiConfiguration> list) {
        Preconditions.checkNotNull(list, "Config list should not be null!");
        this.mStandardWifiConfigCache.clear();
        this.mSuggestedConfigCache.clear();
        this.mNetworkRequestConfigCache.clear();
        new ArrayList();
        for (WifiConfiguration next : list) {
            if (!next.carrierMerged) {
                StandardWifiEntry.StandardWifiEntryKey standardWifiEntryKey = new StandardWifiEntry.StandardWifiEntryKey(next, true);
                if (next.isPasspoint()) {
                    this.mPasspointWifiConfigCache.put(next.networkId, next);
                } else if (next.fromWifiNetworkSuggestion) {
                    if (!this.mSuggestedConfigCache.containsKey(standardWifiEntryKey)) {
                        this.mSuggestedConfigCache.put(standardWifiEntryKey, new ArrayList());
                    }
                    this.mSuggestedConfigCache.get(standardWifiEntryKey).add(next);
                } else if (next.fromWifiNetworkSpecifier) {
                    if (!this.mNetworkRequestConfigCache.containsKey(standardWifiEntryKey)) {
                        this.mNetworkRequestConfigCache.put(standardWifiEntryKey, new ArrayList());
                    }
                    this.mNetworkRequestConfigCache.get(standardWifiEntryKey).add(next);
                } else {
                    if (!this.mStandardWifiConfigCache.containsKey(standardWifiEntryKey)) {
                        this.mStandardWifiConfigCache.put(standardWifiEntryKey, new ArrayList());
                    }
                    this.mStandardWifiConfigCache.get(standardWifiEntryKey).add(next);
                }
            }
        }
        this.mNumSavedNetworks = (int) this.mStandardWifiConfigCache.values().stream().flatMap(new WifiPickerTracker$$ExternalSyntheticLambda12()).filter(new WifiPickerTracker$$ExternalSyntheticLambda13()).map(new WifiPickerTracker$$ExternalSyntheticLambda14()).distinct().count();
        this.mStandardWifiEntryCache.forEach(new WifiPickerTracker$$ExternalSyntheticLambda15(this));
        this.mSuggestedWifiEntryCache.removeIf(new WifiPickerTracker$$ExternalSyntheticLambda16(this));
        NetworkRequestEntry networkRequestEntry = this.mNetworkRequestEntry;
        if (networkRequestEntry != null) {
            networkRequestEntry.updateConfig(this.mNetworkRequestConfigCache.get(networkRequestEntry.getStandardWifiEntryKey()));
        }
    }

    public static /* synthetic */ boolean lambda$updateWifiConfigurations$19(WifiConfiguration wifiConfiguration) {
        return !wifiConfiguration.isEphemeral();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateWifiConfigurations$21(StandardWifiEntry standardWifiEntry) {
        standardWifiEntry.updateConfig(this.mStandardWifiConfigCache.get(standardWifiEntry.getStandardWifiEntryKey()));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$updateWifiConfigurations$22(StandardWifiEntry standardWifiEntry) {
        standardWifiEntry.updateConfig(this.mSuggestedConfigCache.get(standardWifiEntry.getStandardWifiEntryKey()));
        return !standardWifiEntry.isSuggestion();
    }

    public final void updatePasspointConfigurations(List<PasspointConfiguration> list) {
        Preconditions.checkNotNull(list, "Config list should not be null!");
        this.mPasspointConfigCache.clear();
        this.mPasspointConfigCache.putAll((Map) list.stream().collect(Collectors.toMap(new WifiPickerTracker$$ExternalSyntheticLambda10(), Function.identity())));
        this.mPasspointWifiEntryCache.entrySet().removeIf(new WifiPickerTracker$$ExternalSyntheticLambda11(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$updatePasspointConfigurations$24(Map.Entry entry) {
        PasspointWifiEntry passpointWifiEntry = (PasspointWifiEntry) entry.getValue();
        passpointWifiEntry.updatePasspointConfig(this.mPasspointConfigCache.get(passpointWifiEntry.getKey()));
        return !passpointWifiEntry.isSubscription() && !passpointWifiEntry.isSuggestion();
    }

    public final void updateConnectionInfo(WifiInfo wifiInfo, NetworkInfo networkInfo) {
        for (StandardWifiEntry updateConnectionInfo : this.mStandardWifiEntryCache) {
            updateConnectionInfo.updateConnectionInfo(wifiInfo, networkInfo);
        }
        for (StandardWifiEntry updateConnectionInfo2 : this.mSuggestedWifiEntryCache) {
            updateConnectionInfo2.updateConnectionInfo(wifiInfo, networkInfo);
        }
        for (PasspointWifiEntry updateConnectionInfo3 : this.mPasspointWifiEntryCache.values()) {
            updateConnectionInfo3.updateConnectionInfo(wifiInfo, networkInfo);
        }
        for (OsuWifiEntry updateConnectionInfo4 : this.mOsuWifiEntryCache.values()) {
            updateConnectionInfo4.updateConnectionInfo(wifiInfo, networkInfo);
        }
        NetworkRequestEntry networkRequestEntry = this.mNetworkRequestEntry;
        if (networkRequestEntry != null) {
            networkRequestEntry.updateConnectionInfo(wifiInfo, networkInfo);
        }
        updateNetworkRequestEntryConnectionInfo(wifiInfo, networkInfo);
        MergedCarrierEntry mergedCarrierEntry = this.mMergedCarrierEntry;
        if (mergedCarrierEntry != null) {
            mergedCarrierEntry.updateConnectionInfo(wifiInfo, networkInfo);
        }
        conditionallyCreateConnectedStandardWifiEntry(wifiInfo, networkInfo);
        conditionallyCreateConnectedSuggestedWifiEntry(wifiInfo, networkInfo);
        conditionallyCreateConnectedPasspointWifiEntry(wifiInfo, networkInfo);
    }

    public final void updateNetworkRequestEntryConnectionInfo(WifiInfo wifiInfo, NetworkInfo networkInfo) {
        ArrayList arrayList = new ArrayList();
        if (wifiInfo != null) {
            int i = 0;
            while (true) {
                if (i >= this.mNetworkRequestConfigCache.size()) {
                    break;
                }
                List valueAt = this.mNetworkRequestConfigCache.valueAt(i);
                if (!valueAt.isEmpty() && ((WifiConfiguration) valueAt.get(0)).networkId == wifiInfo.getNetworkId()) {
                    arrayList.addAll(valueAt);
                    break;
                }
                i++;
            }
        }
        if (arrayList.isEmpty()) {
            this.mNetworkRequestEntry = null;
            return;
        }
        StandardWifiEntry.StandardWifiEntryKey standardWifiEntryKey = new StandardWifiEntry.StandardWifiEntryKey((WifiConfiguration) arrayList.get(0));
        NetworkRequestEntry networkRequestEntry = this.mNetworkRequestEntry;
        if (networkRequestEntry == null || !networkRequestEntry.getStandardWifiEntryKey().equals(standardWifiEntryKey)) {
            NetworkRequestEntry networkRequestEntry2 = new NetworkRequestEntry(this.mInjector, this.mContext, this.mMainHandler, standardWifiEntryKey, this.mWifiManager, false);
            this.mNetworkRequestEntry = networkRequestEntry2;
            networkRequestEntry2.updateConfig(arrayList);
            updateNetworkRequestEntryScans(this.mScanResultUpdater.getScanResults());
        }
        this.mNetworkRequestEntry.updateConnectionInfo(wifiInfo, networkInfo);
    }

    public final void conditionallyCreateConnectedStandardWifiEntry(WifiInfo wifiInfo, NetworkInfo networkInfo) {
        if (wifiInfo != null && !wifiInfo.isPasspointAp() && !wifiInfo.isOsuAp()) {
            int networkId = wifiInfo.getNetworkId();
            for (List next : this.mStandardWifiConfigCache.values()) {
                if (next.stream().map(new WifiPickerTracker$$ExternalSyntheticLambda29()).filter(new WifiPickerTracker$$ExternalSyntheticLambda30(networkId)).count() != 0) {
                    StandardWifiEntry.StandardWifiEntryKey standardWifiEntryKey = new StandardWifiEntry.StandardWifiEntryKey((WifiConfiguration) next.get(0), true);
                    for (StandardWifiEntry standardWifiEntryKey2 : this.mStandardWifiEntryCache) {
                        if (standardWifiEntryKey.equals(standardWifiEntryKey2.getStandardWifiEntryKey())) {
                            return;
                        }
                    }
                    StandardWifiEntry standardWifiEntry = new StandardWifiEntry(this.mInjector, this.mContext, this.mMainHandler, standardWifiEntryKey, next, (List<ScanResult>) null, this.mWifiManager, false);
                    standardWifiEntry.updateConnectionInfo(wifiInfo, networkInfo);
                    this.mStandardWifiEntryCache.add(standardWifiEntry);
                    return;
                }
            }
        }
    }

    public static /* synthetic */ boolean lambda$conditionallyCreateConnectedStandardWifiEntry$26(int i, Integer num) {
        return num.intValue() == i;
    }

    public final void conditionallyCreateConnectedSuggestedWifiEntry(WifiInfo wifiInfo, NetworkInfo networkInfo) {
        if (wifiInfo != null && !wifiInfo.isPasspointAp() && !wifiInfo.isOsuAp()) {
            int networkId = wifiInfo.getNetworkId();
            for (List next : this.mSuggestedConfigCache.values()) {
                if (!next.isEmpty() && ((WifiConfiguration) next.get(0)).networkId == networkId) {
                    StandardWifiEntry.StandardWifiEntryKey standardWifiEntryKey = new StandardWifiEntry.StandardWifiEntryKey((WifiConfiguration) next.get(0), true);
                    for (StandardWifiEntry standardWifiEntryKey2 : this.mSuggestedWifiEntryCache) {
                        if (standardWifiEntryKey.equals(standardWifiEntryKey2.getStandardWifiEntryKey())) {
                            return;
                        }
                    }
                    StandardWifiEntry standardWifiEntry = new StandardWifiEntry(this.mInjector, this.mContext, this.mMainHandler, standardWifiEntryKey, next, (List<ScanResult>) null, this.mWifiManager, false);
                    standardWifiEntry.updateConnectionInfo(wifiInfo, networkInfo);
                    this.mSuggestedWifiEntryCache.add(standardWifiEntry);
                    return;
                }
            }
        }
    }

    public final void conditionallyCreateConnectedPasspointWifiEntry(WifiInfo wifiInfo, NetworkInfo networkInfo) {
        WifiConfiguration wifiConfiguration;
        PasspointWifiEntry passpointWifiEntry;
        if (wifiInfo != null && wifiInfo.isPasspointAp() && (wifiConfiguration = this.mPasspointWifiConfigCache.get(wifiInfo.getNetworkId())) != null) {
            if (!this.mPasspointWifiEntryCache.containsKey(PasspointWifiEntry.uniqueIdToPasspointWifiEntryKey(wifiConfiguration.getKey()))) {
                PasspointConfiguration passpointConfiguration = this.mPasspointConfigCache.get(PasspointWifiEntry.uniqueIdToPasspointWifiEntryKey(wifiConfiguration.getKey()));
                if (passpointConfiguration != null) {
                    passpointWifiEntry = new PasspointWifiEntry(this.mInjector, this.mContext, this.mMainHandler, passpointConfiguration, this.mWifiManager, false);
                } else {
                    passpointWifiEntry = new PasspointWifiEntry(this.mInjector, this.mContext, this.mMainHandler, wifiConfiguration, this.mWifiManager, false);
                }
                passpointWifiEntry.updateConnectionInfo(wifiInfo, networkInfo);
                this.mPasspointWifiEntryCache.put(passpointWifiEntry.getKey(), passpointWifiEntry);
            }
        }
    }

    public final void notifyOnWifiEntriesChanged() {
        WifiPickerTrackerCallback wifiPickerTrackerCallback = this.mListener;
        if (wifiPickerTrackerCallback != null) {
            Handler handler = this.mMainHandler;
            Objects.requireNonNull(wifiPickerTrackerCallback);
            handler.post(new WifiPickerTracker$$ExternalSyntheticLambda20(wifiPickerTrackerCallback));
        }
    }

    public final void notifyOnNumSavedNetworksChanged() {
        WifiPickerTrackerCallback wifiPickerTrackerCallback = this.mListener;
        if (wifiPickerTrackerCallback != null) {
            Handler handler = this.mMainHandler;
            Objects.requireNonNull(wifiPickerTrackerCallback);
            handler.post(new WifiPickerTracker$$ExternalSyntheticLambda9(wifiPickerTrackerCallback));
        }
    }

    public final void notifyOnNumSavedSubscriptionsChanged() {
        WifiPickerTrackerCallback wifiPickerTrackerCallback = this.mListener;
        if (wifiPickerTrackerCallback != null) {
            Handler handler = this.mMainHandler;
            Objects.requireNonNull(wifiPickerTrackerCallback);
            handler.post(new WifiPickerTracker$$ExternalSyntheticLambda17(wifiPickerTrackerCallback));
        }
    }
}
