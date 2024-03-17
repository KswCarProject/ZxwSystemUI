package com.android.wifitrackerlib;

import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.RouteInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import androidx.core.util.Preconditions;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class WifiEntry {
    public static Comparator<WifiEntry> TITLE_COMPARATOR = Comparator.comparing(new WifiEntry$$ExternalSyntheticLambda7());
    public static Comparator<WifiEntry> WIFI_PICKER_COMPARATOR = Comparator.comparing(new WifiEntry$$ExternalSyntheticLambda0()).thenComparing(new WifiEntry$$ExternalSyntheticLambda1()).thenComparing(new WifiEntry$$ExternalSyntheticLambda2()).thenComparing(new WifiEntry$$ExternalSyntheticLambda3()).thenComparing(new WifiEntry$$ExternalSyntheticLambda4()).thenComparing(new WifiEntry$$ExternalSyntheticLambda5()).thenComparing(new WifiEntry$$ExternalSyntheticLambda6());
    public final Handler mCallbackHandler;
    public boolean mCalledConnect = false;
    public boolean mCalledDisconnect = false;
    public ConnectCallback mConnectCallback;
    public ConnectedInfo mConnectedInfo;
    public int mDeviceWifiStandard;
    public final boolean mForSavedNetworksPage;
    public boolean mIsDefaultNetwork;
    public boolean mIsLowQuality;
    public boolean mIsOweTransitionMode;
    public boolean mIsPskSaeTransitionMode;
    public boolean mIsValidated;
    public int mLevel = -1;
    public WifiEntryCallback mListener;
    public Optional<Object> mManageSubscriptionAction = Optional.empty();
    public NetworkCapabilities mNetworkCapabilities;
    public NetworkInfo mNetworkInfo;
    public WifiInfo mWifiInfo;
    public final WifiManager mWifiManager;
    public int mWifiStandard = 1;

    public interface ConnectCallback {
        void onConnectResult(int i);
    }

    public interface SignInCallback {
    }

    public interface WifiEntryCallback {
        void onUpdated();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateConnectionInfo$10() {
    }

    public boolean canConnect() {
        return false;
    }

    public boolean canDisconnect() {
        return false;
    }

    public boolean canSetAutoJoinEnabled() {
        return false;
    }

    public boolean canSetMeteredChoice() {
        return false;
    }

    public boolean canSignIn() {
        return false;
    }

    public void connect(ConnectCallback connectCallback) {
    }

    public boolean connectionInfoMatches(WifiInfo wifiInfo, NetworkInfo networkInfo) {
        return false;
    }

    public String getHelpUriString() {
        return null;
    }

    public String getKey() {
        return "";
    }

    public int getMeteredChoice() {
        return 0;
    }

    public String getNetworkSelectionDescription() {
        return "";
    }

    public String getScanResultDescription() {
        return "";
    }

    public String getSsid() {
        return null;
    }

    public String getStandardString() {
        return "";
    }

    public String getSummary(boolean z) {
        return "";
    }

    public String getTitle() {
        return "";
    }

    public WifiConfiguration getWifiConfiguration() {
        return null;
    }

    public boolean isAutoJoinEnabled() {
        return false;
    }

    public boolean isMetered() {
        return false;
    }

    public boolean isSaved() {
        return false;
    }

    public boolean isSubscription() {
        return false;
    }

    public boolean isSuggestion() {
        return false;
    }

    public boolean shouldEditBeforeConnect() {
        return false;
    }

    public void updateSecurityTypes() {
    }

    public static /* synthetic */ Boolean lambda$static$0(WifiEntry wifiEntry) {
        return Boolean.valueOf(wifiEntry.getConnectedState() != 2);
    }

    public WifiEntry(Handler handler, WifiManager wifiManager, boolean z) throws IllegalArgumentException {
        Preconditions.checkNotNull(handler, "Cannot construct with null handler!");
        Preconditions.checkNotNull(wifiManager, "Cannot construct with null WifiManager!");
        this.mCallbackHandler = handler;
        this.mForSavedNetworksPage = z;
        this.mWifiManager = wifiManager;
        updatetDeviceWifiGenerationInfo();
    }

    public static boolean isGbkSsidSupported() {
        return WifiTrackerInjector.isGbkSsidSupported();
    }

    public synchronized int getConnectedState() {
        NetworkInfo networkInfo = this.mNetworkInfo;
        if (networkInfo == null) {
            return 0;
        }
        switch (AnonymousClass1.$SwitchMap$android$net$NetworkInfo$DetailedState[networkInfo.getDetailedState().ordinal()]) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                return 1;
            case 7:
                return 2;
            default:
                return 0;
        }
    }

    /* renamed from: com.android.wifitrackerlib.WifiEntry$1  reason: invalid class name */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$android$net$NetworkInfo$DetailedState;

        /* JADX WARNING: Can't wrap try/catch for region: R(14:0|1|2|3|4|5|6|7|8|9|10|11|12|(3:13|14|16)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(16:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|16) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0049 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                android.net.NetworkInfo$DetailedState[] r0 = android.net.NetworkInfo.DetailedState.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$android$net$NetworkInfo$DetailedState = r0
                android.net.NetworkInfo$DetailedState r1 = android.net.NetworkInfo.DetailedState.SCANNING     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$android$net$NetworkInfo$DetailedState     // Catch:{ NoSuchFieldError -> 0x001d }
                android.net.NetworkInfo$DetailedState r1 = android.net.NetworkInfo.DetailedState.CONNECTING     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$android$net$NetworkInfo$DetailedState     // Catch:{ NoSuchFieldError -> 0x0028 }
                android.net.NetworkInfo$DetailedState r1 = android.net.NetworkInfo.DetailedState.AUTHENTICATING     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$android$net$NetworkInfo$DetailedState     // Catch:{ NoSuchFieldError -> 0x0033 }
                android.net.NetworkInfo$DetailedState r1 = android.net.NetworkInfo.DetailedState.OBTAINING_IPADDR     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$android$net$NetworkInfo$DetailedState     // Catch:{ NoSuchFieldError -> 0x003e }
                android.net.NetworkInfo$DetailedState r1 = android.net.NetworkInfo.DetailedState.VERIFYING_POOR_LINK     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = $SwitchMap$android$net$NetworkInfo$DetailedState     // Catch:{ NoSuchFieldError -> 0x0049 }
                android.net.NetworkInfo$DetailedState r1 = android.net.NetworkInfo.DetailedState.CAPTIVE_PORTAL_CHECK     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                int[] r0 = $SwitchMap$android$net$NetworkInfo$DetailedState     // Catch:{ NoSuchFieldError -> 0x0054 }
                android.net.NetworkInfo$DetailedState r1 = android.net.NetworkInfo.DetailedState.CONNECTED     // Catch:{ NoSuchFieldError -> 0x0054 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0054 }
                r2 = 7
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0054 }
            L_0x0054:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.WifiEntry.AnonymousClass1.<clinit>():void");
        }
    }

    public String getSummary() {
        return getSummary(true);
    }

    public int getLevel() {
        return this.mLevel;
    }

    public boolean shouldShowXLevelIcon() {
        return getConnectedState() != 0 && (!this.mIsValidated || !this.mIsDefaultNetwork) && !canSignIn();
    }

    public boolean hasInternetAccess() {
        return this.mIsValidated;
    }

    public boolean isDefaultNetwork() {
        return this.mIsDefaultNetwork;
    }

    public int getSecurity() {
        switch (Utils.getSingleSecurityTypeFromMultipleSecurityTypes(getSecurityTypes())) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 5;
            case 5:
                return 6;
            case 6:
                return 4;
            case 9:
                return 7;
            case 11:
            case 12:
                return 3;
            default:
                return 0;
        }
    }

    public List<Integer> getSecurityTypes() {
        return Collections.emptyList();
    }

    public synchronized ConnectedInfo getConnectedInfo() {
        if (getConnectedState() != 2) {
            return null;
        }
        return new ConnectedInfo(this.mConnectedInfo);
    }

    public static class ConnectedInfo {
        public List<String> dnsServers = new ArrayList();
        public int frequencyMhz;
        public String gateway;
        public String ipAddress;
        public List<String> ipv6Addresses = new ArrayList();
        public int linkSpeedMbps;
        public NetworkCapabilities networkCapabilities;
        public String subnetMask;
        public int wifiStandard = 0;

        public ConnectedInfo() {
        }

        public ConnectedInfo(ConnectedInfo connectedInfo) {
            this.frequencyMhz = connectedInfo.frequencyMhz;
            this.dnsServers = new ArrayList(this.dnsServers);
            this.linkSpeedMbps = connectedInfo.linkSpeedMbps;
            this.ipAddress = connectedInfo.ipAddress;
            this.ipv6Addresses = new ArrayList(connectedInfo.ipv6Addresses);
            this.gateway = connectedInfo.gateway;
            this.subnetMask = connectedInfo.subnetMask;
            this.wifiStandard = connectedInfo.wifiStandard;
            this.networkCapabilities = connectedInfo.networkCapabilities;
        }
    }

    public String getNetworkCapabilityDescription() {
        StringBuilder sb = new StringBuilder();
        if (getConnectedState() == 2) {
            sb.append("isValidated:");
            sb.append(this.mIsValidated);
            sb.append(", isDefaultNetwork:");
            sb.append(this.mIsDefaultNetwork);
            sb.append(", isLowQuality:");
            sb.append(this.mIsLowQuality);
        }
        return sb.toString();
    }

    public synchronized void setListener(WifiEntryCallback wifiEntryCallback) {
        this.mListener = wifiEntryCallback;
    }

    public void notifyOnUpdated() {
        if (this.mListener != null) {
            this.mCallbackHandler.post(new WifiEntry$$ExternalSyntheticLambda9(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyOnUpdated$8() {
        WifiEntryCallback wifiEntryCallback = this.mListener;
        if (wifiEntryCallback != null) {
            wifiEntryCallback.onUpdated();
        }
    }

    public synchronized void updateConnectionInfo(WifiInfo wifiInfo, NetworkInfo networkInfo) {
        if (!(wifiInfo == null || networkInfo == null)) {
            if (connectionInfoMatches(wifiInfo, networkInfo)) {
                this.mWifiInfo = wifiInfo;
                this.mNetworkInfo = networkInfo;
                int rssi = wifiInfo.getRssi();
                if (rssi != -127) {
                    this.mLevel = this.mWifiManager.calculateSignalLevel(rssi);
                }
                if (getConnectedState() == 2) {
                    if (this.mCalledConnect) {
                        this.mCalledConnect = false;
                        this.mCallbackHandler.post(new WifiEntry$$ExternalSyntheticLambda10(this));
                    }
                    if (this.mConnectedInfo == null) {
                        this.mConnectedInfo = new ConnectedInfo();
                    }
                    this.mConnectedInfo.frequencyMhz = wifiInfo.getFrequency();
                    this.mConnectedInfo.linkSpeedMbps = wifiInfo.getLinkSpeed();
                    this.mConnectedInfo.wifiStandard = wifiInfo.getWifiStandard();
                }
                updateSecurityTypes();
                notifyOnUpdated();
            }
        }
        this.mWifiInfo = null;
        this.mNetworkInfo = null;
        this.mNetworkCapabilities = null;
        this.mConnectedInfo = null;
        this.mIsValidated = false;
        this.mIsDefaultNetwork = false;
        this.mIsLowQuality = false;
        if (this.mCalledDisconnect) {
            this.mCalledDisconnect = false;
            this.mCallbackHandler.post(new WifiEntry$$ExternalSyntheticLambda11(this));
        }
        updateSecurityTypes();
        notifyOnUpdated();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateConnectionInfo$9() {
        ConnectCallback connectCallback = this.mConnectCallback;
        if (connectCallback != null) {
            connectCallback.onConnectResult(0);
        }
    }

    public synchronized void updateLinkProperties(LinkProperties linkProperties) {
        if (linkProperties != null) {
            if (getConnectedState() == 2) {
                if (this.mConnectedInfo == null) {
                    this.mConnectedInfo = new ConnectedInfo();
                }
                ArrayList arrayList = new ArrayList();
                for (LinkAddress next : linkProperties.getLinkAddresses()) {
                    if (next.getAddress() instanceof Inet4Address) {
                        this.mConnectedInfo.ipAddress = next.getAddress().getHostAddress();
                        try {
                            InetAddress byAddress = InetAddress.getByAddress(new byte[]{-1, -1, -1, -1});
                            this.mConnectedInfo.subnetMask = Utils.getNetworkPart(byAddress, next.getPrefixLength()).getHostAddress();
                        } catch (UnknownHostException unused) {
                        }
                    } else if (next.getAddress() instanceof Inet6Address) {
                        arrayList.add(next.getAddress().getHostAddress());
                    }
                }
                this.mConnectedInfo.ipv6Addresses = arrayList;
                Iterator<RouteInfo> it = linkProperties.getRoutes().iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    RouteInfo next2 = it.next();
                    if (next2.isDefaultRoute() && (next2.getDestination().getAddress() instanceof Inet4Address) && next2.hasGateway()) {
                        this.mConnectedInfo.gateway = next2.getGateway().getHostAddress();
                        break;
                    }
                }
                this.mConnectedInfo.dnsServers = (List) linkProperties.getDnsServers().stream().map(new WifiEntry$$ExternalSyntheticLambda8()).collect(Collectors.toList());
                notifyOnUpdated();
                return;
            }
        }
        this.mConnectedInfo = null;
        notifyOnUpdated();
    }

    public synchronized void setIsDefaultNetwork(boolean z) {
        this.mIsDefaultNetwork = z;
        notifyOnUpdated();
    }

    public synchronized void setIsLowQuality(boolean z) {
        this.mIsLowQuality = z;
    }

    public synchronized void updateNetworkCapabilities(NetworkCapabilities networkCapabilities) {
        this.mNetworkCapabilities = networkCapabilities;
        ConnectedInfo connectedInfo = this.mConnectedInfo;
        if (connectedInfo != null) {
            connectedInfo.networkCapabilities = networkCapabilities;
            this.mIsValidated = networkCapabilities != null && networkCapabilities.hasCapability(16);
            notifyOnUpdated();
        }
    }

    public synchronized String getWifiInfoDescription() {
        StringJoiner stringJoiner;
        stringJoiner = new StringJoiner(" ");
        if (getConnectedState() == 2 && this.mWifiInfo != null) {
            stringJoiner.add("f = " + this.mWifiInfo.getFrequency());
            String bssid = this.mWifiInfo.getBSSID();
            if (bssid != null) {
                stringJoiner.add(bssid);
            }
            stringJoiner.add("standard = " + getStandardString());
            stringJoiner.add("rssi = " + this.mWifiInfo.getRssi());
            stringJoiner.add("score = " + this.mWifiInfo.getScore());
            stringJoiner.add(String.format(" tx=%.1f,", new Object[]{Double.valueOf(this.mWifiInfo.getSuccessfulTxPacketsPerSecond())}));
            stringJoiner.add(String.format("%.1f,", new Object[]{Double.valueOf(this.mWifiInfo.getRetriedTxPacketsPerSecond())}));
            stringJoiner.add(String.format("%.1f ", new Object[]{Double.valueOf(this.mWifiInfo.getLostTxPacketsPerSecond())}));
            stringJoiner.add(String.format("rx=%.1f", new Object[]{Double.valueOf(this.mWifiInfo.getSuccessfulRxPacketsPerSecond())}));
        }
        return stringJoiner.toString();
    }

    public class ConnectActionListener implements WifiManager.ActionListener {
        public ConnectActionListener() {
        }

        public void onSuccess() {
            WifiEntry wifiEntry;
            synchronized (WifiEntry.this) {
                wifiEntry = WifiEntry.this;
                wifiEntry.mCalledConnect = true;
            }
            wifiEntry.mCallbackHandler.postDelayed(new WifiEntry$ConnectActionListener$$ExternalSyntheticLambda0(this), 10000);
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onSuccess$0() {
            WifiEntry wifiEntry = WifiEntry.this;
            ConnectCallback connectCallback = wifiEntry.mConnectCallback;
            if (connectCallback != null && wifiEntry.mCalledConnect && wifiEntry.getConnectedState() == 0) {
                connectCallback.onConnectResult(2);
                WifiEntry.this.mCalledConnect = false;
            }
        }

        public void onFailure(int i) {
            WifiEntry.this.mCallbackHandler.post(new WifiEntry$ConnectActionListener$$ExternalSyntheticLambda1(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onFailure$1() {
            ConnectCallback connectCallback = WifiEntry.this.mConnectCallback;
            if (connectCallback != null) {
                connectCallback.onConnectResult(2);
            }
        }
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof WifiEntry)) {
            return false;
        }
        return getKey().equals(((WifiEntry) obj).getKey());
    }

    public int hashCode() {
        return getKey().hashCode();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getKey());
        sb.append(",title:");
        sb.append(getTitle());
        sb.append(",summary:");
        sb.append(getSummary());
        sb.append(",isSaved:");
        sb.append(isSaved());
        sb.append(",isSubscription:");
        sb.append(isSubscription());
        sb.append(",isSuggestion:");
        sb.append(isSuggestion());
        sb.append(",level:");
        sb.append(getLevel());
        sb.append(shouldShowXLevelIcon() ? "X" : "");
        sb.append(",security:");
        sb.append(getSecurityTypes());
        sb.append(",standard:");
        sb.append(getWifiStandard());
        sb.append(",connected:");
        sb.append(getConnectedState() == 2 ? "true" : "false");
        sb.append(",connectedInfo:");
        sb.append(getConnectedInfo());
        sb.append(",isValidated:");
        sb.append(this.mIsValidated);
        sb.append(",isDefaultNetwork:");
        sb.append(this.mIsDefaultNetwork);
        return sb.toString();
    }

    public void updateTransitionModeCapa(ScanResult scanResult) {
        this.mIsPskSaeTransitionMode = scanResult.capabilities.contains("PSK") && scanResult.capabilities.contains("SAE");
        this.mIsOweTransitionMode = scanResult.capabilities.contains("OWE_TRANSITION");
    }

    public boolean isPskSaeTransitionMode() {
        return this.mIsPskSaeTransitionMode;
    }

    public boolean isOweTransitionMode() {
        return this.mIsOweTransitionMode;
    }

    public final void updatetDeviceWifiGenerationInfo() {
        if (this.mWifiManager.isWifiStandardSupported(6)) {
            this.mDeviceWifiStandard = 6;
        } else if (this.mWifiManager.isWifiStandardSupported(5)) {
            this.mDeviceWifiStandard = 5;
        } else if (this.mWifiManager.isWifiStandardSupported(4)) {
            this.mDeviceWifiStandard = 4;
        } else {
            this.mDeviceWifiStandard = 1;
        }
    }

    public int getWifiStandard() {
        if (getConnectedInfo() == null || this.mWifiInfo == null || getConnectedState() != 2) {
            return this.mWifiStandard;
        }
        return this.mWifiInfo.getWifiStandard();
    }

    public void updateWifiGenerationInfo(List<ScanResult> list) {
        int i = this.mDeviceWifiStandard;
        for (ScanResult next : list) {
            int wifiStandard = next.getWifiStandard();
            if (wifiStandard < i) {
                i = wifiStandard;
            } else if (next.getBand() == 1 && wifiStandard == 6 && i == 5) {
                i = 4;
            }
        }
        this.mWifiStandard = i;
    }
}
