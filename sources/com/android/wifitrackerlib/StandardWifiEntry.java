package com.android.wifitrackerlib;

import android.annotation.SuppressLint;
import android.app.admin.DevicePolicyManager;
import android.app.admin.WifiSsidPolicy;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiSsid;
import android.os.Handler;
import android.os.SystemClock;
import android.os.UserManager;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import androidx.core.os.BuildCompat;
import com.android.wifitrackerlib.WifiEntry;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StandardWifiEntry extends WifiEntry {
    public final Context mContext;
    public final DevicePolicyManager mDevicePolicyManager;
    public boolean mHasAddConfigUserRestriction;
    public final WifiTrackerInjector mInjector;
    public boolean mIsAdminRestricted;
    public final boolean mIsEnhancedOpenSupported;
    public boolean mIsUserShareable;
    public final boolean mIsWpa3SaeSupported;
    public final boolean mIsWpa3SuiteBSupported;
    public final StandardWifiEntryKey mKey;
    public final Map<Integer, List<ScanResult>> mMatchingScanResults;
    public final Map<Integer, WifiConfiguration> mMatchingWifiConfigs;
    public boolean mShouldAutoOpenCaptivePortal;
    public final List<ScanResult> mTargetScanResults;
    public List<Integer> mTargetSecurityTypes;
    public WifiConfiguration mTargetWifiConfig;
    public final UserManager mUserManager;

    public StandardWifiEntry(WifiTrackerInjector wifiTrackerInjector, Context context, Handler handler, StandardWifiEntryKey standardWifiEntryKey, WifiManager wifiManager, boolean z) {
        super(handler, wifiManager, z);
        this.mMatchingScanResults = new HashMap();
        this.mMatchingWifiConfigs = new HashMap();
        this.mTargetScanResults = new ArrayList();
        this.mTargetSecurityTypes = new ArrayList();
        this.mIsUserShareable = false;
        this.mShouldAutoOpenCaptivePortal = false;
        this.mIsAdminRestricted = false;
        this.mHasAddConfigUserRestriction = false;
        this.mInjector = wifiTrackerInjector;
        this.mContext = context;
        this.mKey = standardWifiEntryKey;
        this.mIsWpa3SaeSupported = wifiManager.isWpa3SaeSupported();
        this.mIsWpa3SuiteBSupported = wifiManager.isWpa3SuiteBSupported();
        this.mIsEnhancedOpenSupported = wifiManager.isEnhancedOpenSupported();
        this.mUserManager = wifiTrackerInjector.getUserManager();
        this.mDevicePolicyManager = wifiTrackerInjector.getDevicePolicyManager();
        updateSecurityTypes();
        updateAdminRestrictions();
    }

    public StandardWifiEntry(WifiTrackerInjector wifiTrackerInjector, Context context, Handler handler, StandardWifiEntryKey standardWifiEntryKey, List<WifiConfiguration> list, List<ScanResult> list2, WifiManager wifiManager, boolean z) throws IllegalArgumentException {
        this(wifiTrackerInjector, context, handler, standardWifiEntryKey, wifiManager, z);
        if (list != null && !list.isEmpty()) {
            updateConfig(list);
        }
        if (list2 != null && !list2.isEmpty()) {
            updateScanResultInfo(list2);
        }
    }

    public String getKey() {
        return this.mKey.toString();
    }

    public StandardWifiEntryKey getStandardWifiEntryKey() {
        return this.mKey;
    }

    public String getTitle() {
        if (WifiEntry.isGbkSsidSupported()) {
            return Utils.getReadableText(this.mKey.getScanResultKey().getSsid());
        }
        return this.mKey.getScanResultKey().getSsid();
    }

    public synchronized String getSummary(boolean z) {
        String str;
        if (hasAdminRestrictions()) {
            return this.mContext.getString(R$string.wifitrackerlib_admin_restricted_network);
        }
        StringJoiner stringJoiner = new StringJoiner(this.mContext.getString(R$string.wifitrackerlib_summary_separator));
        int connectedState = getConnectedState();
        if (connectedState == 0) {
            str = Utils.getDisconnectedDescription(this.mInjector, this.mContext, this.mTargetWifiConfig, this.mForSavedNetworksPage, z);
        } else if (connectedState == 1) {
            str = Utils.getConnectingDescription(this.mContext, this.mNetworkInfo);
        } else if (connectedState != 2) {
            Log.e("StandardWifiEntry", "getConnectedState() returned unknown state: " + connectedState);
            str = null;
        } else {
            str = Utils.getConnectedDescription(this.mContext, this.mTargetWifiConfig, this.mNetworkCapabilities, this.mIsDefaultNetwork, this.mIsLowQuality);
        }
        if (!TextUtils.isEmpty(str)) {
            stringJoiner.add(str);
        }
        String autoConnectDescription = Utils.getAutoConnectDescription(this.mContext, this);
        if (!TextUtils.isEmpty(autoConnectDescription)) {
            stringJoiner.add(autoConnectDescription);
        }
        String meteredDescription = Utils.getMeteredDescription(this.mContext, this);
        if (!TextUtils.isEmpty(meteredDescription)) {
            stringJoiner.add(meteredDescription);
        }
        if (!z) {
            String verboseLoggingDescription = Utils.getVerboseLoggingDescription(this);
            if (!TextUtils.isEmpty(verboseLoggingDescription)) {
                stringJoiner.add(verboseLoggingDescription);
            }
        }
        return stringJoiner.toString();
    }

    public String getSsid() {
        return this.mKey.getScanResultKey().getSsid();
    }

    public synchronized List<Integer> getSecurityTypes() {
        return new ArrayList(this.mTargetSecurityTypes);
    }

    public synchronized boolean isMetered() {
        boolean z;
        WifiConfiguration wifiConfiguration;
        z = true;
        if (getMeteredChoice() != 1 && ((wifiConfiguration = this.mTargetWifiConfig) == null || !wifiConfiguration.meteredHint)) {
            z = false;
        }
        return z;
    }

    public synchronized boolean isSaved() {
        WifiConfiguration wifiConfiguration;
        wifiConfiguration = this.mTargetWifiConfig;
        return wifiConfiguration != null && !wifiConfiguration.fromWifiNetworkSuggestion && !wifiConfiguration.isEphemeral();
    }

    public synchronized boolean isSuggestion() {
        WifiConfiguration wifiConfiguration;
        wifiConfiguration = this.mTargetWifiConfig;
        return wifiConfiguration != null && wifiConfiguration.fromWifiNetworkSuggestion;
    }

    public synchronized WifiConfiguration getWifiConfiguration() {
        if (!isSaved()) {
            return null;
        }
        return this.mTargetWifiConfig;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:43:0x0072, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x0074, code lost:
        return true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x0076, code lost:
        return false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean canConnect() {
        /*
            r5 = this;
            monitor-enter(r5)
            int r0 = r5.mLevel     // Catch:{ all -> 0x0077 }
            r1 = -1
            r2 = 0
            if (r0 == r1) goto L_0x0075
            int r0 = r5.getConnectedState()     // Catch:{ all -> 0x0077 }
            if (r0 == 0) goto L_0x000e
            goto L_0x0075
        L_0x000e:
            boolean r0 = r5.hasAdminRestrictions()     // Catch:{ all -> 0x0077 }
            if (r0 == 0) goto L_0x0016
            monitor-exit(r5)
            return r2
        L_0x0016:
            java.util.List<java.lang.Integer> r0 = r5.mTargetSecurityTypes     // Catch:{ all -> 0x0077 }
            r3 = 3
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch:{ all -> 0x0077 }
            boolean r0 = r0.contains(r3)     // Catch:{ all -> 0x0077 }
            r3 = 1
            if (r0 == 0) goto L_0x0073
            android.net.wifi.WifiConfiguration r0 = r5.mTargetWifiConfig     // Catch:{ all -> 0x0077 }
            if (r0 == 0) goto L_0x0073
            android.net.wifi.WifiEnterpriseConfig r0 = r0.enterpriseConfig     // Catch:{ all -> 0x0077 }
            if (r0 == 0) goto L_0x0073
            boolean r0 = r0.isAuthenticationSimBased()     // Catch:{ all -> 0x0077 }
            if (r0 != 0) goto L_0x0034
            monitor-exit(r5)
            return r3
        L_0x0034:
            android.content.Context r0 = r5.mContext     // Catch:{ all -> 0x0077 }
            java.lang.Class<android.telephony.SubscriptionManager> r4 = android.telephony.SubscriptionManager.class
            java.lang.Object r0 = r0.getSystemService(r4)     // Catch:{ all -> 0x0077 }
            android.telephony.SubscriptionManager r0 = (android.telephony.SubscriptionManager) r0     // Catch:{ all -> 0x0077 }
            java.util.List r0 = r0.getActiveSubscriptionInfoList()     // Catch:{ all -> 0x0077 }
            if (r0 == 0) goto L_0x0071
            int r4 = r0.size()     // Catch:{ all -> 0x0077 }
            if (r4 != 0) goto L_0x004b
            goto L_0x0071
        L_0x004b:
            android.net.wifi.WifiConfiguration r4 = r5.mTargetWifiConfig     // Catch:{ all -> 0x0077 }
            int r4 = r4.carrierId     // Catch:{ all -> 0x0077 }
            if (r4 != r1) goto L_0x0053
            monitor-exit(r5)
            return r3
        L_0x0053:
            java.util.Iterator r0 = r0.iterator()     // Catch:{ all -> 0x0077 }
        L_0x0057:
            boolean r1 = r0.hasNext()     // Catch:{ all -> 0x0077 }
            if (r1 == 0) goto L_0x006f
            java.lang.Object r1 = r0.next()     // Catch:{ all -> 0x0077 }
            android.telephony.SubscriptionInfo r1 = (android.telephony.SubscriptionInfo) r1     // Catch:{ all -> 0x0077 }
            int r1 = r1.getCarrierId()     // Catch:{ all -> 0x0077 }
            android.net.wifi.WifiConfiguration r4 = r5.mTargetWifiConfig     // Catch:{ all -> 0x0077 }
            int r4 = r4.carrierId     // Catch:{ all -> 0x0077 }
            if (r1 != r4) goto L_0x0057
            monitor-exit(r5)
            return r3
        L_0x006f:
            monitor-exit(r5)
            return r2
        L_0x0071:
            monitor-exit(r5)
            return r2
        L_0x0073:
            monitor-exit(r5)
            return r3
        L_0x0075:
            monitor-exit(r5)
            return r2
        L_0x0077:
            r0 = move-exception
            monitor-exit(r5)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.StandardWifiEntry.canConnect():boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0141, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x0151, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void connect(com.android.wifitrackerlib.WifiEntry.ConnectCallback r5) {
        /*
            r4 = this;
            monitor-enter(r4)
            r4.mConnectCallback = r5     // Catch:{ all -> 0x0152 }
            r0 = 1
            r4.mShouldAutoOpenCaptivePortal = r0     // Catch:{ all -> 0x0152 }
            android.net.wifi.WifiManager r0 = r4.mWifiManager     // Catch:{ all -> 0x0152 }
            r0.stopRestrictingAutoJoinToSubscriptionId()     // Catch:{ all -> 0x0152 }
            boolean r0 = r4.isSaved()     // Catch:{ all -> 0x0152 }
            if (r0 != 0) goto L_0x0120
            boolean r0 = r4.isSuggestion()     // Catch:{ all -> 0x0152 }
            if (r0 == 0) goto L_0x0019
            goto L_0x0120
        L_0x0019:
            java.util.List<java.lang.Integer> r0 = r4.mTargetSecurityTypes     // Catch:{ all -> 0x0152 }
            r1 = 6
            java.lang.Integer r2 = java.lang.Integer.valueOf(r1)     // Catch:{ all -> 0x0152 }
            boolean r0 = r0.contains(r2)     // Catch:{ all -> 0x0152 }
            r2 = 0
            if (r0 == 0) goto L_0x00bf
            android.net.wifi.WifiConfiguration r5 = new android.net.wifi.WifiConfiguration     // Catch:{ all -> 0x0152 }
            r5.<init>()     // Catch:{ all -> 0x0152 }
            boolean r0 = com.android.wifitrackerlib.WifiEntry.isGbkSsidSupported()     // Catch:{ all -> 0x0152 }
            if (r0 == 0) goto L_0x003f
            com.android.wifitrackerlib.StandardWifiEntry$StandardWifiEntryKey r0 = r4.mKey     // Catch:{ all -> 0x0152 }
            com.android.wifitrackerlib.StandardWifiEntry$ScanResultKey r0 = r0.getScanResultKey()     // Catch:{ all -> 0x0152 }
            java.lang.String r0 = r0.getSsid()     // Catch:{ all -> 0x0152 }
            r5.SSID = r0     // Catch:{ all -> 0x0152 }
            goto L_0x0061
        L_0x003f:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x0152 }
            r0.<init>()     // Catch:{ all -> 0x0152 }
            java.lang.String r3 = "\""
            r0.append(r3)     // Catch:{ all -> 0x0152 }
            com.android.wifitrackerlib.StandardWifiEntry$StandardWifiEntryKey r3 = r4.mKey     // Catch:{ all -> 0x0152 }
            com.android.wifitrackerlib.StandardWifiEntry$ScanResultKey r3 = r3.getScanResultKey()     // Catch:{ all -> 0x0152 }
            java.lang.String r3 = r3.getSsid()     // Catch:{ all -> 0x0152 }
            r0.append(r3)     // Catch:{ all -> 0x0152 }
            java.lang.String r3 = "\""
            r0.append(r3)     // Catch:{ all -> 0x0152 }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x0152 }
            r5.SSID = r0     // Catch:{ all -> 0x0152 }
        L_0x0061:
            r5.setSecurityParams(r1)     // Catch:{ all -> 0x0152 }
            android.net.wifi.WifiManager r0 = r4.mWifiManager     // Catch:{ all -> 0x0152 }
            com.android.wifitrackerlib.WifiEntry$ConnectActionListener r1 = new com.android.wifitrackerlib.WifiEntry$ConnectActionListener     // Catch:{ all -> 0x0152 }
            r1.<init>()     // Catch:{ all -> 0x0152 }
            r0.connect(r5, r1)     // Catch:{ all -> 0x0152 }
            java.util.List<java.lang.Integer> r5 = r4.mTargetSecurityTypes     // Catch:{ all -> 0x0152 }
            java.lang.Integer r0 = java.lang.Integer.valueOf(r2)     // Catch:{ all -> 0x0152 }
            boolean r5 = r5.contains(r0)     // Catch:{ all -> 0x0152 }
            if (r5 == 0) goto L_0x0150
            android.net.wifi.WifiConfiguration r5 = new android.net.wifi.WifiConfiguration     // Catch:{ all -> 0x0152 }
            r5.<init>()     // Catch:{ all -> 0x0152 }
            boolean r0 = com.android.wifitrackerlib.WifiEntry.isGbkSsidSupported()     // Catch:{ all -> 0x0152 }
            if (r0 == 0) goto L_0x0092
            com.android.wifitrackerlib.StandardWifiEntry$StandardWifiEntryKey r0 = r4.mKey     // Catch:{ all -> 0x0152 }
            com.android.wifitrackerlib.StandardWifiEntry$ScanResultKey r0 = r0.getScanResultKey()     // Catch:{ all -> 0x0152 }
            java.lang.String r0 = r0.getSsid()     // Catch:{ all -> 0x0152 }
            r5.SSID = r0     // Catch:{ all -> 0x0152 }
            goto L_0x00b4
        L_0x0092:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x0152 }
            r0.<init>()     // Catch:{ all -> 0x0152 }
            java.lang.String r1 = "\""
            r0.append(r1)     // Catch:{ all -> 0x0152 }
            com.android.wifitrackerlib.StandardWifiEntry$StandardWifiEntryKey r1 = r4.mKey     // Catch:{ all -> 0x0152 }
            com.android.wifitrackerlib.StandardWifiEntry$ScanResultKey r1 = r1.getScanResultKey()     // Catch:{ all -> 0x0152 }
            java.lang.String r1 = r1.getSsid()     // Catch:{ all -> 0x0152 }
            r0.append(r1)     // Catch:{ all -> 0x0152 }
            java.lang.String r1 = "\""
            r0.append(r1)     // Catch:{ all -> 0x0152 }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x0152 }
            r5.SSID = r0     // Catch:{ all -> 0x0152 }
        L_0x00b4:
            r5.setSecurityParams(r2)     // Catch:{ all -> 0x0152 }
            android.net.wifi.WifiManager r0 = r4.mWifiManager     // Catch:{ all -> 0x0152 }
            r1 = 0
            r0.save(r5, r1)     // Catch:{ all -> 0x0152 }
            goto L_0x0150
        L_0x00bf:
            java.util.List<java.lang.Integer> r0 = r4.mTargetSecurityTypes     // Catch:{ all -> 0x0152 }
            java.lang.Integer r1 = java.lang.Integer.valueOf(r2)     // Catch:{ all -> 0x0152 }
            boolean r0 = r0.contains(r1)     // Catch:{ all -> 0x0152 }
            if (r0 == 0) goto L_0x0113
            android.net.wifi.WifiConfiguration r5 = new android.net.wifi.WifiConfiguration     // Catch:{ all -> 0x0152 }
            r5.<init>()     // Catch:{ all -> 0x0152 }
            boolean r0 = com.android.wifitrackerlib.WifiEntry.isGbkSsidSupported()     // Catch:{ all -> 0x0152 }
            if (r0 == 0) goto L_0x00e3
            com.android.wifitrackerlib.StandardWifiEntry$StandardWifiEntryKey r0 = r4.mKey     // Catch:{ all -> 0x0152 }
            com.android.wifitrackerlib.StandardWifiEntry$ScanResultKey r0 = r0.getScanResultKey()     // Catch:{ all -> 0x0152 }
            java.lang.String r0 = r0.getSsid()     // Catch:{ all -> 0x0152 }
            r5.SSID = r0     // Catch:{ all -> 0x0152 }
            goto L_0x0105
        L_0x00e3:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x0152 }
            r0.<init>()     // Catch:{ all -> 0x0152 }
            java.lang.String r1 = "\""
            r0.append(r1)     // Catch:{ all -> 0x0152 }
            com.android.wifitrackerlib.StandardWifiEntry$StandardWifiEntryKey r1 = r4.mKey     // Catch:{ all -> 0x0152 }
            com.android.wifitrackerlib.StandardWifiEntry$ScanResultKey r1 = r1.getScanResultKey()     // Catch:{ all -> 0x0152 }
            java.lang.String r1 = r1.getSsid()     // Catch:{ all -> 0x0152 }
            r0.append(r1)     // Catch:{ all -> 0x0152 }
            java.lang.String r1 = "\""
            r0.append(r1)     // Catch:{ all -> 0x0152 }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x0152 }
            r5.SSID = r0     // Catch:{ all -> 0x0152 }
        L_0x0105:
            r5.setSecurityParams(r2)     // Catch:{ all -> 0x0152 }
            android.net.wifi.WifiManager r0 = r4.mWifiManager     // Catch:{ all -> 0x0152 }
            com.android.wifitrackerlib.WifiEntry$ConnectActionListener r1 = new com.android.wifitrackerlib.WifiEntry$ConnectActionListener     // Catch:{ all -> 0x0152 }
            r1.<init>()     // Catch:{ all -> 0x0152 }
            r0.connect(r5, r1)     // Catch:{ all -> 0x0152 }
            goto L_0x0150
        L_0x0113:
            if (r5 == 0) goto L_0x0150
            android.os.Handler r0 = r4.mCallbackHandler     // Catch:{ all -> 0x0152 }
            com.android.wifitrackerlib.StandardWifiEntry$$ExternalSyntheticLambda5 r1 = new com.android.wifitrackerlib.StandardWifiEntry$$ExternalSyntheticLambda5     // Catch:{ all -> 0x0152 }
            r1.<init>(r5)     // Catch:{ all -> 0x0152 }
            r0.post(r1)     // Catch:{ all -> 0x0152 }
            goto L_0x0150
        L_0x0120:
            android.net.wifi.WifiConfiguration r0 = r4.mTargetWifiConfig     // Catch:{ all -> 0x0152 }
            boolean r0 = com.android.wifitrackerlib.Utils.isSimCredential(r0)     // Catch:{ all -> 0x0152 }
            if (r0 == 0) goto L_0x0142
            android.content.Context r0 = r4.mContext     // Catch:{ all -> 0x0152 }
            android.net.wifi.WifiConfiguration r1 = r4.mTargetWifiConfig     // Catch:{ all -> 0x0152 }
            int r1 = r1.carrierId     // Catch:{ all -> 0x0152 }
            boolean r0 = com.android.wifitrackerlib.Utils.isSimPresent(r0, r1)     // Catch:{ all -> 0x0152 }
            if (r0 != 0) goto L_0x0142
            if (r5 == 0) goto L_0x0140
            android.os.Handler r0 = r4.mCallbackHandler     // Catch:{ all -> 0x0152 }
            com.android.wifitrackerlib.StandardWifiEntry$$ExternalSyntheticLambda4 r1 = new com.android.wifitrackerlib.StandardWifiEntry$$ExternalSyntheticLambda4     // Catch:{ all -> 0x0152 }
            r1.<init>(r5)     // Catch:{ all -> 0x0152 }
            r0.post(r1)     // Catch:{ all -> 0x0152 }
        L_0x0140:
            monitor-exit(r4)
            return
        L_0x0142:
            android.net.wifi.WifiManager r5 = r4.mWifiManager     // Catch:{ all -> 0x0152 }
            android.net.wifi.WifiConfiguration r0 = r4.mTargetWifiConfig     // Catch:{ all -> 0x0152 }
            int r0 = r0.networkId     // Catch:{ all -> 0x0152 }
            com.android.wifitrackerlib.WifiEntry$ConnectActionListener r1 = new com.android.wifitrackerlib.WifiEntry$ConnectActionListener     // Catch:{ all -> 0x0152 }
            r1.<init>()     // Catch:{ all -> 0x0152 }
            r5.connect(r0, r1)     // Catch:{ all -> 0x0152 }
        L_0x0150:
            monitor-exit(r4)
            return
        L_0x0152:
            r5 = move-exception
            monitor-exit(r4)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.StandardWifiEntry.connect(com.android.wifitrackerlib.WifiEntry$ConnectCallback):void");
    }

    public boolean canDisconnect() {
        return getConnectedState() == 2;
    }

    public synchronized boolean canSignIn() {
        NetworkCapabilities networkCapabilities;
        networkCapabilities = this.mNetworkCapabilities;
        return networkCapabilities != null && networkCapabilities.hasCapability(17);
    }

    public void signIn(WifiEntry.SignInCallback signInCallback) {
        if (canSignIn()) {
            NonSdkApiWrapper.startCaptivePortalApp((ConnectivityManager) this.mContext.getSystemService(ConnectivityManager.class), this.mWifiManager.getCurrentNetwork());
        }
    }

    public synchronized int getMeteredChoice() {
        WifiConfiguration wifiConfiguration;
        if (!isSuggestion() && (wifiConfiguration = this.mTargetWifiConfig) != null) {
            int i = wifiConfiguration.meteredOverride;
            if (i == 1) {
                return 1;
            }
            if (i == 2) {
                return 2;
            }
        }
        return 0;
    }

    public boolean canSetMeteredChoice() {
        return getWifiConfiguration() != null;
    }

    public synchronized boolean isAutoJoinEnabled() {
        WifiConfiguration wifiConfiguration = this.mTargetWifiConfig;
        if (wifiConfiguration == null) {
            return false;
        }
        return wifiConfiguration.allowAutojoin;
    }

    public boolean canSetAutoJoinEnabled() {
        return isSaved() || isSuggestion();
    }

    public synchronized String getStandardString() {
        WifiInfo wifiInfo = this.mWifiInfo;
        if (wifiInfo != null) {
            return Utils.getStandardString(this.mContext, wifiInfo.getWifiStandard());
        } else if (this.mTargetScanResults.isEmpty()) {
            return "";
        } else {
            return Utils.getStandardString(this.mContext, this.mTargetScanResults.get(0).getWifiStandard());
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x002e, code lost:
        return false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean shouldEditBeforeConnect() {
        /*
            r3 = this;
            monitor-enter(r3)
            android.net.wifi.WifiConfiguration r0 = r3.getWifiConfiguration()     // Catch:{ all -> 0x002f }
            r1 = 0
            if (r0 != 0) goto L_0x000a
            monitor-exit(r3)
            return r1
        L_0x000a:
            android.net.wifi.WifiConfiguration$NetworkSelectionStatus r0 = r0.getNetworkSelectionStatus()     // Catch:{ all -> 0x002f }
            int r2 = r0.getNetworkSelectionStatus()     // Catch:{ all -> 0x002f }
            if (r2 == 0) goto L_0x002d
            r2 = 2
            int r2 = r0.getDisableReasonCounter(r2)     // Catch:{ all -> 0x002f }
            if (r2 > 0) goto L_0x002a
            r2 = 8
            int r2 = r0.getDisableReasonCounter(r2)     // Catch:{ all -> 0x002f }
            if (r2 > 0) goto L_0x002a
            r2 = 5
            int r0 = r0.getDisableReasonCounter(r2)     // Catch:{ all -> 0x002f }
            if (r0 <= 0) goto L_0x002d
        L_0x002a:
            r0 = 1
            monitor-exit(r3)
            return r0
        L_0x002d:
            monitor-exit(r3)
            return r1
        L_0x002f:
            r0 = move-exception
            monitor-exit(r3)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.StandardWifiEntry.shouldEditBeforeConnect():boolean");
    }

    public synchronized void updateScanResultInfo(List<ScanResult> list) throws IllegalArgumentException {
        if (list == null) {
            list = new ArrayList<>();
        }
        String ssid = this.mKey.getScanResultKey().getSsid();
        for (ScanResult next : list) {
            String str = next.SSID;
            if (WifiEntry.isGbkSsidSupported()) {
                str = next.getWifiSsid().toString();
            }
            if (!TextUtils.equals(str, ssid)) {
                throw new IllegalArgumentException("Attempted to update with wrong SSID! Expected: " + ssid + ", Actual: " + str + ", ScanResult: " + next);
            }
        }
        this.mMatchingScanResults.clear();
        Set<Integer> securityTypes = this.mKey.getScanResultKey().getSecurityTypes();
        for (ScanResult next2 : list) {
            for (Integer intValue : Utils.getSecurityTypesFromScanResult(next2)) {
                int intValue2 = intValue.intValue();
                if (securityTypes.contains(Integer.valueOf(intValue2))) {
                    if (isSecurityTypeSupported(intValue2)) {
                        if (!this.mMatchingScanResults.containsKey(Integer.valueOf(intValue2))) {
                            this.mMatchingScanResults.put(Integer.valueOf(intValue2), new ArrayList());
                        }
                        this.mMatchingScanResults.get(Integer.valueOf(intValue2)).add(next2);
                    }
                }
            }
        }
        updateSecurityTypes();
        updateTargetScanResultInfo();
        notifyOnUpdated();
    }

    public final synchronized void updateTargetScanResultInfo() {
        ScanResult bestScanResultByLevel = Utils.getBestScanResultByLevel(this.mTargetScanResults);
        if (bestScanResultByLevel != null) {
            updateTransitionModeCapa(bestScanResultByLevel);
        }
        if (getConnectedState() == 0) {
            this.mLevel = bestScanResultByLevel != null ? this.mWifiManager.calculateSignalLevel(bestScanResultByLevel.level) : -1;
        }
        updateWifiGenerationInfo(this.mTargetScanResults);
    }

    public synchronized void updateNetworkCapabilities(NetworkCapabilities networkCapabilities) {
        super.updateNetworkCapabilities(networkCapabilities);
        if (canSignIn() && this.mShouldAutoOpenCaptivePortal) {
            this.mShouldAutoOpenCaptivePortal = false;
            signIn((WifiEntry.SignInCallback) null);
        }
    }

    public synchronized void updateConfig(List<WifiConfiguration> list) throws IllegalArgumentException {
        if (list == null) {
            list = Collections.emptyList();
        }
        ScanResultKey scanResultKey = this.mKey.getScanResultKey();
        String ssid = scanResultKey.getSsid();
        Set<Integer> securityTypes = scanResultKey.getSecurityTypes();
        this.mMatchingWifiConfigs.clear();
        for (WifiConfiguration next : list) {
            String str = next.SSID;
            if (!WifiEntry.isGbkSsidSupported()) {
                str = WifiInfo.sanitizeSsid(next.SSID);
            }
            if (TextUtils.equals(ssid, str)) {
                Iterator<Integer> it = Utils.getSecurityTypesFromWifiConfiguration(next).iterator();
                while (true) {
                    if (it.hasNext()) {
                        int intValue = it.next().intValue();
                        if (!securityTypes.contains(Integer.valueOf(intValue))) {
                            throw new IllegalArgumentException("Attempted to update with wrong security! Expected one of: " + securityTypes + ", Actual: " + intValue + ", Config: " + next);
                        } else if (isSecurityTypeSupported(intValue)) {
                            this.mMatchingWifiConfigs.put(Integer.valueOf(intValue), next);
                        }
                    }
                }
            } else {
                throw new IllegalArgumentException("Attempted to update with wrong SSID! Expected: " + ssid + ", Actual: " + WifiInfo.sanitizeSsid(next.SSID) + ", Config: " + next);
            }
        }
        updateSecurityTypes();
        updateTargetScanResultInfo();
        notifyOnUpdated();
    }

    public final boolean isSecurityTypeSupported(int i) {
        if (i == 4) {
            return this.mIsWpa3SaeSupported;
        }
        if (i == 5) {
            return this.mIsWpa3SuiteBSupported;
        }
        if (i != 6) {
            return true;
        }
        return this.mIsEnhancedOpenSupported;
    }

    public synchronized void updateSecurityTypes() {
        this.mTargetSecurityTypes.clear();
        WifiInfo wifiInfo = this.mWifiInfo;
        if (!(wifiInfo == null || wifiInfo.getCurrentSecurityType() == -1)) {
            this.mTargetSecurityTypes.add(Integer.valueOf(this.mWifiInfo.getCurrentSecurityType()));
        }
        Set<Integer> keySet = this.mMatchingWifiConfigs.keySet();
        if (this.mTargetSecurityTypes.isEmpty() && this.mKey.isTargetingNewNetworks()) {
            boolean z = false;
            Set<Integer> keySet2 = this.mMatchingScanResults.keySet();
            Iterator<Integer> it = keySet.iterator();
            while (true) {
                if (it.hasNext()) {
                    if (keySet2.contains(Integer.valueOf(it.next().intValue()))) {
                        z = true;
                        break;
                    }
                } else {
                    break;
                }
            }
            if (!z) {
                this.mTargetSecurityTypes.addAll(keySet2);
            }
        }
        if (this.mTargetSecurityTypes.isEmpty()) {
            this.mTargetSecurityTypes.addAll(keySet);
        }
        if (this.mTargetSecurityTypes.isEmpty()) {
            this.mTargetSecurityTypes.addAll(this.mKey.getScanResultKey().getSecurityTypes());
        }
        this.mTargetWifiConfig = this.mMatchingWifiConfigs.get(Integer.valueOf(Utils.getSingleSecurityTypeFromMultipleSecurityTypes(this.mTargetSecurityTypes)));
        ArraySet arraySet = new ArraySet();
        for (Integer intValue : this.mTargetSecurityTypes) {
            int intValue2 = intValue.intValue();
            if (this.mMatchingScanResults.containsKey(Integer.valueOf(intValue2))) {
                arraySet.addAll(this.mMatchingScanResults.get(Integer.valueOf(intValue2)));
            }
        }
        this.mTargetScanResults.clear();
        this.mTargetScanResults.addAll(arraySet);
    }

    public synchronized void setUserShareable(boolean z) {
        this.mIsUserShareable = z;
    }

    public synchronized boolean isUserShareable() {
        return this.mIsUserShareable;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0033, code lost:
        return false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean connectionInfoMatches(android.net.wifi.WifiInfo r4, android.net.NetworkInfo r5) {
        /*
            r3 = this;
            monitor-enter(r3)
            boolean r5 = r4.isPasspointAp()     // Catch:{ all -> 0x0034 }
            r0 = 0
            if (r5 != 0) goto L_0x0032
            boolean r5 = r4.isOsuAp()     // Catch:{ all -> 0x0034 }
            if (r5 == 0) goto L_0x000f
            goto L_0x0032
        L_0x000f:
            java.util.Map<java.lang.Integer, android.net.wifi.WifiConfiguration> r5 = r3.mMatchingWifiConfigs     // Catch:{ all -> 0x0034 }
            java.util.Collection r5 = r5.values()     // Catch:{ all -> 0x0034 }
            java.util.Iterator r5 = r5.iterator()     // Catch:{ all -> 0x0034 }
        L_0x0019:
            boolean r1 = r5.hasNext()     // Catch:{ all -> 0x0034 }
            if (r1 == 0) goto L_0x0030
            java.lang.Object r1 = r5.next()     // Catch:{ all -> 0x0034 }
            android.net.wifi.WifiConfiguration r1 = (android.net.wifi.WifiConfiguration) r1     // Catch:{ all -> 0x0034 }
            int r1 = r1.networkId     // Catch:{ all -> 0x0034 }
            int r2 = r4.getNetworkId()     // Catch:{ all -> 0x0034 }
            if (r1 != r2) goto L_0x0019
            r4 = 1
            monitor-exit(r3)
            return r4
        L_0x0030:
            monitor-exit(r3)
            return r0
        L_0x0032:
            monitor-exit(r3)
            return r0
        L_0x0034:
            r4 = move-exception
            monitor-exit(r3)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.StandardWifiEntry.connectionInfoMatches(android.net.wifi.WifiInfo, android.net.NetworkInfo):boolean");
    }

    public synchronized String getScanResultDescription() {
        if (this.mTargetScanResults.size() == 0) {
            return "";
        }
        return "[" + getScanResultDescription(2400, 2500) + ";" + getScanResultDescription(4900, 5900) + ";" + getScanResultDescription(5925, 7125) + ";" + getScanResultDescription(58320, 70200) + "]";
    }

    public final synchronized String getScanResultDescription(int i, int i2) {
        List list = (List) this.mTargetScanResults.stream().filter(new StandardWifiEntry$$ExternalSyntheticLambda0(i, i2)).sorted(Comparator.comparingInt(new StandardWifiEntry$$ExternalSyntheticLambda1())).collect(Collectors.toList());
        int size = list.size();
        if (size == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(size);
        sb.append(")");
        if (size > 4) {
            int asInt = list.stream().mapToInt(new StandardWifiEntry$$ExternalSyntheticLambda2()).max().getAsInt();
            sb.append("max=");
            sb.append(asInt);
            sb.append(",");
        }
        list.forEach(new StandardWifiEntry$$ExternalSyntheticLambda3(this, sb, SystemClock.elapsedRealtime()));
        return sb.toString();
    }

    public static /* synthetic */ boolean lambda$getScanResultDescription$3(int i, int i2, ScanResult scanResult) {
        int i3 = scanResult.frequency;
        return i3 >= i && i3 <= i2;
    }

    public static /* synthetic */ int lambda$getScanResultDescription$4(ScanResult scanResult) {
        return scanResult.level * -1;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$getScanResultDescription$6(StringBuilder sb, long j, ScanResult scanResult) {
        sb.append(getScanResultDescription(scanResult, j));
    }

    @SuppressLint({"NewApi"})
    public final synchronized String getScanResultDescription(ScanResult scanResult, long j) {
        StringBuilder sb;
        sb = new StringBuilder();
        sb.append(" \n{");
        sb.append(scanResult.BSSID);
        WifiInfo wifiInfo = this.mWifiInfo;
        if (wifiInfo != null && scanResult.BSSID.equals(wifiInfo.getBSSID())) {
            sb.append("*");
        }
        sb.append("=");
        sb.append(scanResult.frequency);
        sb.append(",");
        sb.append(scanResult.level);
        int wifiStandard = scanResult.getWifiStandard();
        sb.append(",");
        sb.append(Utils.getStandardString(this.mContext, wifiStandard));
        if (BuildCompat.isAtLeastT() && wifiStandard == 8) {
            sb.append(",mldMac=");
            sb.append(scanResult.getApMldMacAddress());
            sb.append(",linkId=");
            sb.append(scanResult.getApMloLinkId());
            sb.append(",affLinks=");
            sb.append(scanResult.getAffiliatedMloLinks());
        }
        sb.append(",");
        sb.append(((int) (j - (scanResult.timestamp / 1000))) / 1000);
        sb.append("s");
        sb.append("}");
        return sb.toString();
    }

    public String getNetworkSelectionDescription() {
        return Utils.getNetworkSelectionDescription(getWifiConfiguration());
    }

    @SuppressLint({"NewApi"})
    public void updateAdminRestrictions() {
        boolean z;
        if (BuildCompat.isAtLeastT()) {
            UserManager userManager = this.mUserManager;
            if (userManager != null) {
                this.mHasAddConfigUserRestriction = userManager.hasUserRestriction("no_add_wifi_config");
            }
            DevicePolicyManager devicePolicyManager = this.mDevicePolicyManager;
            if (devicePolicyManager != null) {
                int minimumRequiredWifiSecurityLevel = devicePolicyManager.getMinimumRequiredWifiSecurityLevel();
                if (minimumRequiredWifiSecurityLevel != 0) {
                    Iterator<Integer> it = getSecurityTypes().iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            z = false;
                            break;
                        }
                        int convertSecurityTypeToDpmWifiSecurity = Utils.convertSecurityTypeToDpmWifiSecurity(it.next().intValue());
                        if (convertSecurityTypeToDpmWifiSecurity != -1 && minimumRequiredWifiSecurityLevel <= convertSecurityTypeToDpmWifiSecurity) {
                            z = true;
                            break;
                        }
                    }
                    if (!z) {
                        this.mIsAdminRestricted = true;
                        return;
                    }
                }
                WifiSsidPolicy wifiSsidPolicy = this.mDevicePolicyManager.getWifiSsidPolicy();
                if (wifiSsidPolicy != null) {
                    int policyType = wifiSsidPolicy.getPolicyType();
                    Set ssids = wifiSsidPolicy.getSsids();
                    if (policyType == 0 && !ssids.contains(WifiSsid.fromBytes(getSsid().getBytes(StandardCharsets.UTF_8)))) {
                        this.mIsAdminRestricted = true;
                        return;
                    } else if (policyType == 1 && ssids.contains(WifiSsid.fromBytes(getSsid().getBytes(StandardCharsets.UTF_8)))) {
                        this.mIsAdminRestricted = true;
                        return;
                    }
                }
            }
            this.mIsAdminRestricted = false;
        }
    }

    public final boolean hasAdminRestrictions() {
        return (this.mHasAddConfigUserRestriction && !isSaved() && !isSuggestion()) || this.mIsAdminRestricted;
    }

    public static class StandardWifiEntryKey {
        public boolean mIsNetworkRequest;
        public boolean mIsTargetingNewNetworks;
        public ScanResultKey mScanResultKey;
        public String mSuggestionProfileKey;

        public StandardWifiEntryKey(ScanResultKey scanResultKey, boolean z) {
            this.mScanResultKey = scanResultKey;
            this.mIsTargetingNewNetworks = z;
        }

        public StandardWifiEntryKey(WifiConfiguration wifiConfiguration) {
            this(wifiConfiguration, false);
        }

        public StandardWifiEntryKey(WifiConfiguration wifiConfiguration, boolean z) {
            this.mIsTargetingNewNetworks = false;
            this.mScanResultKey = new ScanResultKey(wifiConfiguration);
            if (wifiConfiguration.fromWifiNetworkSuggestion) {
                this.mSuggestionProfileKey = new StringJoiner(",").add(wifiConfiguration.creatorName).add(String.valueOf(wifiConfiguration.carrierId)).add(String.valueOf(wifiConfiguration.subscriptionId)).toString();
            } else if (wifiConfiguration.fromWifiNetworkSpecifier) {
                this.mIsNetworkRequest = true;
            }
            this.mIsTargetingNewNetworks = z;
        }

        public StandardWifiEntryKey(String str) {
            this.mIsTargetingNewNetworks = false;
            this.mScanResultKey = new ScanResultKey();
            if (!str.startsWith("StandardWifiEntry:")) {
                Log.e("StandardWifiEntry", "String key does not start with key prefix!");
                return;
            }
            try {
                JSONObject jSONObject = new JSONObject(str.substring(18));
                if (jSONObject.has("SCAN_RESULT_KEY")) {
                    this.mScanResultKey = new ScanResultKey(jSONObject.getString("SCAN_RESULT_KEY"));
                }
                if (jSONObject.has("SUGGESTION_PROFILE_KEY")) {
                    this.mSuggestionProfileKey = jSONObject.getString("SUGGESTION_PROFILE_KEY");
                }
                if (jSONObject.has("IS_NETWORK_REQUEST")) {
                    this.mIsNetworkRequest = jSONObject.getBoolean("IS_NETWORK_REQUEST");
                }
                if (jSONObject.has("IS_TARGETING_NEW_NETWORKS")) {
                    this.mIsTargetingNewNetworks = jSONObject.getBoolean("IS_TARGETING_NEW_NETWORKS");
                }
            } catch (JSONException e) {
                Log.e("StandardWifiEntry", "JSONException while converting StandardWifiEntryKey to string: " + e);
            }
        }

        public String toString() {
            JSONObject jSONObject = new JSONObject();
            try {
                ScanResultKey scanResultKey = this.mScanResultKey;
                if (scanResultKey != null) {
                    jSONObject.put("SCAN_RESULT_KEY", scanResultKey.toString());
                }
                String str = this.mSuggestionProfileKey;
                if (str != null) {
                    jSONObject.put("SUGGESTION_PROFILE_KEY", str);
                }
                boolean z = this.mIsNetworkRequest;
                if (z) {
                    jSONObject.put("IS_NETWORK_REQUEST", z);
                }
                boolean z2 = this.mIsTargetingNewNetworks;
                if (z2) {
                    jSONObject.put("IS_TARGETING_NEW_NETWORKS", z2);
                }
            } catch (JSONException e) {
                Log.wtf("StandardWifiEntry", "JSONException while converting StandardWifiEntryKey to string: " + e);
            }
            return "StandardWifiEntry:" + jSONObject.toString();
        }

        public ScanResultKey getScanResultKey() {
            return this.mScanResultKey;
        }

        public boolean isNetworkRequest() {
            return this.mIsNetworkRequest;
        }

        public boolean isTargetingNewNetworks() {
            return this.mIsTargetingNewNetworks;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            StandardWifiEntryKey standardWifiEntryKey = (StandardWifiEntryKey) obj;
            if (!Objects.equals(this.mScanResultKey, standardWifiEntryKey.mScanResultKey) || !TextUtils.equals(this.mSuggestionProfileKey, standardWifiEntryKey.mSuggestionProfileKey) || this.mIsNetworkRequest != standardWifiEntryKey.mIsNetworkRequest) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return Objects.hash(new Object[]{this.mScanResultKey, this.mSuggestionProfileKey, Boolean.valueOf(this.mIsNetworkRequest)});
        }
    }

    public static class ScanResultKey {
        public Set<Integer> mSecurityTypes;
        public String mSsid;

        public ScanResultKey() {
            this.mSecurityTypes = new ArraySet();
        }

        public ScanResultKey(String str, List<Integer> list) {
            this.mSecurityTypes = new ArraySet();
            this.mSsid = str;
            for (Integer intValue : list) {
                int intValue2 = intValue.intValue();
                if (intValue2 == 0) {
                    this.mSecurityTypes.add(6);
                } else if (intValue2 == 6) {
                    this.mSecurityTypes.add(0);
                } else if (intValue2 == 9) {
                    this.mSecurityTypes.add(3);
                } else if (intValue2 == 2) {
                    this.mSecurityTypes.add(4);
                } else if (intValue2 == 3) {
                    this.mSecurityTypes.add(9);
                } else if (intValue2 == 4) {
                    this.mSecurityTypes.add(2);
                } else if (intValue2 != 11) {
                    if (intValue2 == 12) {
                    }
                }
                this.mSecurityTypes.add(Integer.valueOf(intValue2));
            }
        }

        /* JADX WARNING: Illegal instructions before constructor call */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public ScanResultKey(android.net.wifi.ScanResult r2) {
            /*
                r1 = this;
                boolean r0 = com.android.wifitrackerlib.WifiEntry.isGbkSsidSupported()
                if (r0 == 0) goto L_0x000f
                android.net.wifi.WifiSsid r0 = r2.getWifiSsid()
                java.lang.String r0 = r0.toString()
                goto L_0x0011
            L_0x000f:
                java.lang.String r0 = r2.SSID
            L_0x0011:
                java.util.List r2 = com.android.wifitrackerlib.Utils.getSecurityTypesFromScanResult(r2)
                r1.<init>(r0, r2)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.StandardWifiEntry.ScanResultKey.<init>(android.net.wifi.ScanResult):void");
        }

        /* JADX WARNING: Illegal instructions before constructor call */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public ScanResultKey(android.net.wifi.WifiConfiguration r2) {
            /*
                r1 = this;
                boolean r0 = com.android.wifitrackerlib.WifiEntry.isGbkSsidSupported()
                if (r0 == 0) goto L_0x0009
                java.lang.String r0 = r2.SSID
                goto L_0x000f
            L_0x0009:
                java.lang.String r0 = r2.SSID
                java.lang.String r0 = android.net.wifi.WifiInfo.sanitizeSsid(r0)
            L_0x000f:
                java.util.List r2 = com.android.wifitrackerlib.Utils.getSecurityTypesFromWifiConfiguration(r2)
                r1.<init>(r0, r2)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.StandardWifiEntry.ScanResultKey.<init>(android.net.wifi.WifiConfiguration):void");
        }

        public ScanResultKey(String str) {
            this.mSecurityTypes = new ArraySet();
            try {
                JSONObject jSONObject = new JSONObject(str);
                this.mSsid = jSONObject.getString("SSID");
                JSONArray jSONArray = jSONObject.getJSONArray("SECURITY_TYPES");
                for (int i = 0; i < jSONArray.length(); i++) {
                    this.mSecurityTypes.add(Integer.valueOf(jSONArray.getInt(i)));
                }
            } catch (JSONException e) {
                Log.wtf("StandardWifiEntry", "JSONException while constructing ScanResultKey from string: " + e);
            }
        }

        public String toString() {
            JSONObject jSONObject = new JSONObject();
            try {
                String str = this.mSsid;
                if (str != null) {
                    jSONObject.put("SSID", str);
                }
                if (!this.mSecurityTypes.isEmpty()) {
                    JSONArray jSONArray = new JSONArray();
                    for (Integer intValue : this.mSecurityTypes) {
                        jSONArray.put(intValue.intValue());
                    }
                    jSONObject.put("SECURITY_TYPES", jSONArray);
                }
            } catch (JSONException e) {
                Log.e("StandardWifiEntry", "JSONException while converting ScanResultKey to string: " + e);
            }
            return jSONObject.toString();
        }

        public String getSsid() {
            return this.mSsid;
        }

        public Set<Integer> getSecurityTypes() {
            return this.mSecurityTypes;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            ScanResultKey scanResultKey = (ScanResultKey) obj;
            if (!TextUtils.equals(this.mSsid, scanResultKey.mSsid) || !this.mSecurityTypes.equals(scanResultKey.mSecurityTypes)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return Objects.hash(new Object[]{this.mSsid, this.mSecurityTypes});
        }
    }
}
