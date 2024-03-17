package com.android.wifitrackerlib;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiEnterpriseConfig;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.DateUtils;
import com.android.systemui.theme.ThemeOverlayApplier;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringJoiner;

public class Utils {
    public static int convertSecurityTypeToDpmWifiSecurity(int i) {
        switch (i) {
            case 0:
            case 6:
                return 0;
            case 1:
            case 2:
            case 4:
            case 7:
                return 1;
            case 3:
            case 8:
            case 9:
            case 11:
            case 12:
                return 2;
            case 5:
                return 3;
            default:
                return -1;
        }
    }

    public static ScanResult getBestScanResultByLevel(List<ScanResult> list) {
        if (list.isEmpty()) {
            return null;
        }
        return (ScanResult) Collections.max(list, Comparator.comparingInt(new Utils$$ExternalSyntheticLambda0()));
    }

    public static List<Integer> getSecurityTypesFromScanResult(ScanResult scanResult) {
        ArrayList arrayList = new ArrayList();
        for (int valueOf : scanResult.getSecurityTypes()) {
            arrayList.add(Integer.valueOf(valueOf));
        }
        return arrayList;
    }

    public static List<Integer> getSecurityTypesFromWifiConfiguration(WifiConfiguration wifiConfiguration) {
        if (wifiConfiguration.allowedKeyManagement.get(14)) {
            return Arrays.asList(new Integer[]{8});
        } else if (wifiConfiguration.allowedKeyManagement.get(13)) {
            return Arrays.asList(new Integer[]{7});
        } else if (wifiConfiguration.allowedKeyManagement.get(10)) {
            return Arrays.asList(new Integer[]{5});
        } else if (wifiConfiguration.allowedKeyManagement.get(9)) {
            return Arrays.asList(new Integer[]{6});
        } else if (wifiConfiguration.allowedKeyManagement.get(8)) {
            return Arrays.asList(new Integer[]{4});
        } else if (wifiConfiguration.allowedKeyManagement.get(4)) {
            return Arrays.asList(new Integer[]{2});
        } else if (wifiConfiguration.allowedKeyManagement.get(2)) {
            if (!wifiConfiguration.requirePmf || wifiConfiguration.allowedPairwiseCiphers.get(1) || !wifiConfiguration.allowedProtocols.get(1)) {
                return Arrays.asList(new Integer[]{3, 9});
            }
            return Arrays.asList(new Integer[]{9});
        } else if (wifiConfiguration.allowedKeyManagement.get(1)) {
            return Arrays.asList(new Integer[]{2});
        } else {
            if (wifiConfiguration.allowedKeyManagement.get(0) && wifiConfiguration.wepKeys != null) {
                int i = 0;
                while (true) {
                    String[] strArr = wifiConfiguration.wepKeys;
                    if (i >= strArr.length) {
                        break;
                    } else if (strArr[i] != null) {
                        return Arrays.asList(new Integer[]{1});
                    } else {
                        i++;
                    }
                }
            }
            return Arrays.asList(new Integer[]{0});
        }
    }

    public static int getSingleSecurityTypeFromMultipleSecurityTypes(List<Integer> list) {
        if (list.size() == 1) {
            return list.get(0).intValue();
        }
        if (list.size() != 2) {
            return -1;
        }
        if (list.contains(0)) {
            return 0;
        }
        if (list.contains(2)) {
            return 2;
        }
        if (list.contains(3)) {
            return 3;
        }
        return -1;
    }

    public static int toDigit(char[] cArr, int i) throws IllegalArgumentException {
        char c = cArr[i];
        if ('0' <= c && c <= '9') {
            return c - '0';
        }
        char c2 = 'a';
        if ('a' > c || c > 'f') {
            c2 = 'A';
            if ('A' > c || c > 'F') {
                throw new IllegalArgumentException("Illegal char: " + cArr[i] + " at offset " + i);
            }
        }
        return (c - c2) + 10;
    }

    /* JADX WARNING: Removed duplicated region for block: B:9:0x001f A[LOOP:0: B:8:0x001d->B:9:0x001f, LOOP_END] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] decode(char[] r6, boolean r7) throws java.lang.IllegalArgumentException {
        /*
            int r0 = r6.length
            int r1 = r0 + 1
            int r1 = r1 / 2
            byte[] r1 = new byte[r1]
            r2 = 0
            r3 = 1
            if (r7 == 0) goto L_0x0018
            int r7 = r0 % 2
            if (r7 == 0) goto L_0x001c
            int r7 = toDigit(r6, r2)
            byte r7 = (byte) r7
            r1[r2] = r7
            r2 = r3
            goto L_0x001d
        L_0x0018:
            int r7 = r0 % 2
            if (r7 != 0) goto L_0x0036
        L_0x001c:
            r3 = r2
        L_0x001d:
            if (r2 >= r0) goto L_0x0035
            int r7 = r3 + 1
            int r4 = toDigit(r6, r2)
            int r4 = r4 << 4
            int r5 = r2 + 1
            int r5 = toDigit(r6, r5)
            r4 = r4 | r5
            byte r4 = (byte) r4
            r1[r3] = r4
            int r2 = r2 + 2
            r3 = r7
            goto L_0x001d
        L_0x0035:
            return r1
        L_0x0036:
            java.lang.IllegalArgumentException r6 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r1 = "Invalid input length: "
            r7.append(r1)
            r7.append(r0)
            java.lang.String r7 = r7.toString()
            r6.<init>(r7)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.Utils.decode(char[], boolean):byte[]");
    }

    public static String decodeSsid(byte[] bArr, Charset charset) {
        CharsetDecoder onUnmappableCharacter = charset.newDecoder().onMalformedInput(CodingErrorAction.REPORT).onUnmappableCharacter(CodingErrorAction.REPORT);
        CharBuffer allocate = CharBuffer.allocate(32);
        CoderResult decode = onUnmappableCharacter.decode(ByteBuffer.wrap(bArr), allocate, true);
        allocate.flip();
        if (decode.isError()) {
            return null;
        }
        return allocate.toString();
    }

    public static String removeEnclosingQuotes(String str) {
        int length = str.length();
        if (length < 2 || str.charAt(0) != '\"') {
            return str;
        }
        int i = length - 1;
        return str.charAt(i) == '\"' ? str.substring(1, i) : str;
    }

    public static String getReadableText(String str) {
        if (!TextUtils.isEmpty(str) && str.charAt(0) != '\"') {
            try {
                byte[] decode = decode(str.toCharArray(), false);
                Charset forName = Charset.forName("UTF-8");
                if (forName != null) {
                    String decodeSsid = decodeSsid(decode, forName);
                    if (!TextUtils.isEmpty(decodeSsid)) {
                        return decodeSsid;
                    }
                }
                Charset forName2 = Charset.forName("GBK");
                if (forName2 != null) {
                    String decodeSsid2 = decodeSsid(decode, forName2);
                    if (!TextUtils.isEmpty(decodeSsid2)) {
                        return decodeSsid2;
                    }
                }
            } catch (IllegalArgumentException unused) {
            }
        }
        return removeEnclosingQuotes(str);
    }

    public static String getAppLabel(Context context, String str) {
        try {
            return context.getPackageManager().getApplicationInfo(str, 0).loadLabel(context.getPackageManager()).toString();
        } catch (PackageManager.NameNotFoundException unused) {
            return "";
        }
    }

    public static String getConnectedDescription(Context context, WifiConfiguration wifiConfiguration, NetworkCapabilities networkCapabilities, boolean z, boolean z2) {
        StringJoiner stringJoiner = new StringJoiner(context.getString(R$string.wifitrackerlib_summary_separator));
        if (wifiConfiguration != null && (wifiConfiguration.fromWifiNetworkSuggestion || wifiConfiguration.fromWifiNetworkSpecifier)) {
            String suggestionOrSpecifierLabel = getSuggestionOrSpecifierLabel(context, wifiConfiguration);
            if (!TextUtils.isEmpty(suggestionOrSpecifierLabel)) {
                if (!z) {
                    stringJoiner.add(context.getString(R$string.wifitrackerlib_available_via_app, new Object[]{suggestionOrSpecifierLabel}));
                } else {
                    stringJoiner.add(context.getString(R$string.wifitrackerlib_connected_via_app, new Object[]{suggestionOrSpecifierLabel}));
                }
            }
        }
        if (z2) {
            stringJoiner.add(context.getString(R$string.wifi_connected_low_quality));
        }
        String currentNetworkCapabilitiesInformation = getCurrentNetworkCapabilitiesInformation(context, networkCapabilities);
        if (!TextUtils.isEmpty(currentNetworkCapabilitiesInformation)) {
            stringJoiner.add(currentNetworkCapabilitiesInformation);
        }
        if (stringJoiner.length() != 0 || !z) {
            return stringJoiner.toString();
        }
        return context.getResources().getStringArray(R$array.wifitrackerlib_wifi_status)[NetworkInfo.DetailedState.CONNECTED.ordinal()];
    }

    public static String getConnectingDescription(Context context, NetworkInfo networkInfo) {
        NetworkInfo.DetailedState detailedState;
        if (context == null || networkInfo == null || (detailedState = networkInfo.getDetailedState()) == null) {
            return "";
        }
        String[] stringArray = context.getResources().getStringArray(R$array.wifitrackerlib_wifi_status);
        int ordinal = detailedState.ordinal();
        if (ordinal >= stringArray.length) {
            return "";
        }
        return stringArray[ordinal];
    }

    public static String getDisconnectedDescription(WifiTrackerInjector wifiTrackerInjector, Context context, WifiConfiguration wifiConfiguration, boolean z, boolean z2) {
        if (context == null || wifiConfiguration == null) {
            return "";
        }
        StringJoiner stringJoiner = new StringJoiner(context.getString(R$string.wifitrackerlib_summary_separator));
        if (z2) {
            stringJoiner.add(context.getString(R$string.wifitrackerlib_wifi_disconnected));
        } else if (!z || wifiConfiguration.isPasspoint()) {
            if (wifiConfiguration.fromWifiNetworkSuggestion) {
                String suggestionOrSpecifierLabel = getSuggestionOrSpecifierLabel(context, wifiConfiguration);
                if (!TextUtils.isEmpty(suggestionOrSpecifierLabel)) {
                    stringJoiner.add(context.getString(R$string.wifitrackerlib_available_via_app, new Object[]{suggestionOrSpecifierLabel}));
                }
            } else {
                stringJoiner.add(context.getString(R$string.wifitrackerlib_wifi_remembered));
            }
        } else if (!wifiTrackerInjector.getNoAttributionAnnotationPackages().contains(wifiConfiguration.creatorName)) {
            String appLabel = getAppLabel(context, wifiConfiguration.creatorName);
            if (!TextUtils.isEmpty(appLabel)) {
                stringJoiner.add(context.getString(R$string.wifitrackerlib_saved_network, new Object[]{appLabel}));
            }
        }
        String wifiConfigurationFailureMessage = getWifiConfigurationFailureMessage(context, wifiConfiguration);
        if (!TextUtils.isEmpty(wifiConfigurationFailureMessage)) {
            stringJoiner.add(wifiConfigurationFailureMessage);
        }
        return stringJoiner.toString();
    }

    public static String getSuggestionOrSpecifierLabel(Context context, WifiConfiguration wifiConfiguration) {
        if (context == null || wifiConfiguration == null) {
            return "";
        }
        String carrierNameForSubId = getCarrierNameForSubId(context, getSubIdForConfig(context, wifiConfiguration));
        if (!TextUtils.isEmpty(carrierNameForSubId)) {
            return carrierNameForSubId;
        }
        String appLabel = getAppLabel(context, wifiConfiguration.creatorName);
        if (!TextUtils.isEmpty(appLabel)) {
            return appLabel;
        }
        return wifiConfiguration.creatorName;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0049, code lost:
        if (r1 != 9) goto L_0x006f;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getWifiConfigurationFailureMessage(android.content.Context r4, android.net.wifi.WifiConfiguration r5) {
        /*
            java.lang.String r0 = ""
            if (r4 == 0) goto L_0x00a5
            if (r5 != 0) goto L_0x0008
            goto L_0x00a5
        L_0x0008:
            boolean r1 = r5.hasNoInternetAccess()
            r2 = 2
            if (r1 == 0) goto L_0x0023
            android.net.wifi.WifiConfiguration$NetworkSelectionStatus r5 = r5.getNetworkSelectionStatus()
            int r5 = r5.getNetworkSelectionStatus()
            if (r5 != r2) goto L_0x001c
            int r5 = com.android.wifitrackerlib.R$string.wifitrackerlib_wifi_no_internet_no_reconnect
            goto L_0x001e
        L_0x001c:
            int r5 = com.android.wifitrackerlib.R$string.wifitrackerlib_wifi_no_internet
        L_0x001e:
            java.lang.String r4 = r4.getString(r5)
            return r4
        L_0x0023:
            android.net.wifi.WifiConfiguration$NetworkSelectionStatus r1 = r5.getNetworkSelectionStatus()
            int r1 = r1.getNetworkSelectionStatus()
            if (r1 == 0) goto L_0x006f
            android.net.wifi.WifiConfiguration$NetworkSelectionStatus r1 = r5.getNetworkSelectionStatus()
            int r1 = r1.getNetworkSelectionDisableReason()
            r3 = 1
            if (r1 == r3) goto L_0x0068
            if (r1 == r2) goto L_0x0061
            r2 = 3
            if (r1 == r2) goto L_0x005a
            r2 = 4
            if (r1 == r2) goto L_0x0053
            r2 = 6
            if (r1 == r2) goto L_0x0053
            r2 = 8
            if (r1 == r2) goto L_0x004c
            r2 = 9
            if (r1 == r2) goto L_0x0061
            goto L_0x006f
        L_0x004c:
            int r5 = com.android.wifitrackerlib.R$string.wifitrackerlib_wifi_check_password_try_again
            java.lang.String r4 = r4.getString(r5)
            return r4
        L_0x0053:
            int r5 = com.android.wifitrackerlib.R$string.wifitrackerlib_wifi_no_internet_no_reconnect
            java.lang.String r4 = r4.getString(r5)
            return r4
        L_0x005a:
            int r5 = com.android.wifitrackerlib.R$string.wifitrackerlib_wifi_disabled_network_failure
            java.lang.String r4 = r4.getString(r5)
            return r4
        L_0x0061:
            int r5 = com.android.wifitrackerlib.R$string.wifitrackerlib_wifi_disabled_password_failure
            java.lang.String r4 = r4.getString(r5)
            return r4
        L_0x0068:
            int r5 = com.android.wifitrackerlib.R$string.wifitrackerlib_wifi_disabled_generic
            java.lang.String r4 = r4.getString(r5)
            return r4
        L_0x006f:
            int r5 = r5.getRecentFailureReason()
            r1 = 17
            if (r5 == r1) goto L_0x009e
            switch(r5) {
                case 1002: goto L_0x009e;
                case 1003: goto L_0x0097;
                case 1004: goto L_0x009e;
                case 1005: goto L_0x0090;
                case 1006: goto L_0x0089;
                case 1007: goto L_0x0090;
                case 1008: goto L_0x0090;
                case 1009: goto L_0x0082;
                case 1010: goto L_0x0082;
                case 1011: goto L_0x007b;
                default: goto L_0x007a;
            }
        L_0x007a:
            return r0
        L_0x007b:
            int r5 = com.android.wifitrackerlib.R$string.wifitrackerlib_wifi_network_not_found
            java.lang.String r4 = r4.getString(r5)
            return r4
        L_0x0082:
            int r5 = com.android.wifitrackerlib.R$string.wifitrackerlib_wifi_mbo_oce_assoc_disallowed_insufficient_rssi
            java.lang.String r4 = r4.getString(r5)
            return r4
        L_0x0089:
            int r5 = com.android.wifitrackerlib.R$string.wifitrackerlib_wifi_mbo_assoc_disallowed_max_num_sta_associated
            java.lang.String r4 = r4.getString(r5)
            return r4
        L_0x0090:
            int r5 = com.android.wifitrackerlib.R$string.wifitrackerlib_wifi_mbo_assoc_disallowed_cannot_connect
            java.lang.String r4 = r4.getString(r5)
            return r4
        L_0x0097:
            int r5 = com.android.wifitrackerlib.R$string.wifitrackerlib_wifi_poor_channel_conditions
            java.lang.String r4 = r4.getString(r5)
            return r4
        L_0x009e:
            int r5 = com.android.wifitrackerlib.R$string.wifitrackerlib_wifi_ap_unable_to_handle_new_sta
            java.lang.String r4 = r4.getString(r5)
            return r4
        L_0x00a5:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wifitrackerlib.Utils.getWifiConfigurationFailureMessage(android.content.Context, android.net.wifi.WifiConfiguration):java.lang.String");
    }

    public static String getAutoConnectDescription(Context context, WifiEntry wifiEntry) {
        if (context == null || wifiEntry == null || !wifiEntry.canSetAutoJoinEnabled() || wifiEntry.isAutoJoinEnabled()) {
            return "";
        }
        return context.getString(R$string.wifitrackerlib_auto_connect_disable);
    }

    public static String getMeteredDescription(Context context, WifiEntry wifiEntry) {
        if (context == null || wifiEntry == null) {
            return "";
        }
        if (!wifiEntry.canSetMeteredChoice() && wifiEntry.getMeteredChoice() != 1) {
            return "";
        }
        if (wifiEntry.getMeteredChoice() == 1) {
            return context.getString(R$string.wifitrackerlib_wifi_metered_label);
        }
        if (wifiEntry.getMeteredChoice() == 2) {
            return context.getString(R$string.wifitrackerlib_wifi_unmetered_label);
        }
        if (wifiEntry.isMetered()) {
            return context.getString(R$string.wifitrackerlib_wifi_metered_label);
        }
        return "";
    }

    public static String getVerboseLoggingDescription(WifiEntry wifiEntry) {
        if (!BaseWifiTracker.isVerboseLoggingEnabled() || wifiEntry == null) {
            return "";
        }
        StringJoiner stringJoiner = new StringJoiner(" ");
        String wifiInfoDescription = wifiEntry.getWifiInfoDescription();
        if (!TextUtils.isEmpty(wifiInfoDescription)) {
            stringJoiner.add(wifiInfoDescription);
        }
        String networkCapabilityDescription = wifiEntry.getNetworkCapabilityDescription();
        if (!TextUtils.isEmpty(networkCapabilityDescription)) {
            stringJoiner.add(networkCapabilityDescription);
        }
        String scanResultDescription = wifiEntry.getScanResultDescription();
        if (!TextUtils.isEmpty(scanResultDescription)) {
            stringJoiner.add(scanResultDescription);
        }
        String networkSelectionDescription = wifiEntry.getNetworkSelectionDescription();
        if (!TextUtils.isEmpty(networkSelectionDescription)) {
            stringJoiner.add(networkSelectionDescription);
        }
        return stringJoiner.toString();
    }

    public static String getNetworkSelectionDescription(WifiConfiguration wifiConfiguration) {
        if (wifiConfiguration == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        WifiConfiguration.NetworkSelectionStatus networkSelectionStatus = wifiConfiguration.getNetworkSelectionStatus();
        if (networkSelectionStatus.getNetworkSelectionStatus() != 0) {
            sb.append(" (" + networkSelectionStatus.getNetworkStatusString());
            if (networkSelectionStatus.getDisableTime() > 0) {
                sb.append(" " + DateUtils.formatElapsedTime((System.currentTimeMillis() - networkSelectionStatus.getDisableTime()) / 1000));
            }
            sb.append(")");
        }
        int maxNetworkSelectionDisableReason = WifiConfiguration.NetworkSelectionStatus.getMaxNetworkSelectionDisableReason();
        for (int i = 0; i <= maxNetworkSelectionDisableReason; i++) {
            int disableReasonCounter = networkSelectionStatus.getDisableReasonCounter(i);
            if (disableReasonCounter != 0) {
                sb.append(" ");
                sb.append(WifiConfiguration.NetworkSelectionStatus.getNetworkSelectionDisableReasonString(i));
                sb.append("=");
                sb.append(disableReasonCounter);
            }
        }
        return sb.toString();
    }

    public static String getCurrentNetworkCapabilitiesInformation(Context context, NetworkCapabilities networkCapabilities) {
        if (!(context == null || networkCapabilities == null)) {
            if (networkCapabilities.hasCapability(17)) {
                return context.getString(context.getResources().getIdentifier("network_available_sign_in", "string", ThemeOverlayApplier.ANDROID_PACKAGE));
            }
            if (networkCapabilities.hasCapability(24)) {
                return context.getString(R$string.wifitrackerlib_wifi_limited_connection);
            }
            if (!networkCapabilities.hasCapability(16)) {
                if (networkCapabilities.isPrivateDnsBroken()) {
                    return context.getString(R$string.wifitrackerlib_private_dns_broken);
                }
                return context.getString(R$string.wifitrackerlib_wifi_connected_cannot_provide_internet);
            }
        }
        return "";
    }

    public static boolean isSimPresent(Context context, int i) {
        List<SubscriptionInfo> activeSubscriptionInfoList;
        SubscriptionManager subscriptionManager = (SubscriptionManager) context.getSystemService("telephony_subscription_service");
        if (subscriptionManager == null || (activeSubscriptionInfoList = subscriptionManager.getActiveSubscriptionInfoList()) == null || activeSubscriptionInfoList.isEmpty()) {
            return false;
        }
        if (i == -1) {
            return true;
        }
        return activeSubscriptionInfoList.stream().anyMatch(new Utils$$ExternalSyntheticLambda1(i));
    }

    public static /* synthetic */ boolean lambda$isSimPresent$1(int i, SubscriptionInfo subscriptionInfo) {
        return subscriptionInfo.getCarrierId() == i;
    }

    public static String getCarrierNameForSubId(Context context, int i) {
        TelephonyManager telephonyManager;
        TelephonyManager createForSubscriptionId;
        CharSequence simCarrierIdName;
        if (i == -1 || (telephonyManager = (TelephonyManager) context.getSystemService("phone")) == null || (createForSubscriptionId = telephonyManager.createForSubscriptionId(i)) == null || (simCarrierIdName = createForSubscriptionId.getSimCarrierIdName()) == null) {
            return null;
        }
        return simCarrierIdName.toString();
    }

    public static boolean isSimCredential(WifiConfiguration wifiConfiguration) {
        WifiEnterpriseConfig wifiEnterpriseConfig = wifiConfiguration.enterpriseConfig;
        return wifiEnterpriseConfig != null && wifiEnterpriseConfig.isAuthenticationSimBased();
    }

    public static int getSubIdForConfig(Context context, WifiConfiguration wifiConfiguration) {
        SubscriptionManager subscriptionManager;
        int i = -1;
        if (wifiConfiguration.carrierId == -1 || (subscriptionManager = (SubscriptionManager) context.getSystemService("telephony_subscription_service")) == null) {
            return -1;
        }
        List<SubscriptionInfo> activeSubscriptionInfoList = subscriptionManager.getActiveSubscriptionInfoList();
        if (activeSubscriptionInfoList != null && !activeSubscriptionInfoList.isEmpty()) {
            int defaultDataSubscriptionId = SubscriptionManager.getDefaultDataSubscriptionId();
            for (SubscriptionInfo next : activeSubscriptionInfoList) {
                if (next.getCarrierId() == wifiConfiguration.carrierId && (i = next.getSubscriptionId()) == defaultDataSubscriptionId) {
                    break;
                }
            }
        }
        return i;
    }

    public static InetAddress getNetworkPart(InetAddress inetAddress, int i) {
        byte[] address = inetAddress.getAddress();
        maskRawAddress(address, i);
        try {
            return InetAddress.getByAddress(address);
        } catch (UnknownHostException e) {
            throw new RuntimeException("getNetworkPart error - " + e.toString());
        }
    }

    public static void maskRawAddress(byte[] bArr, int i) {
        if (i < 0 || i > bArr.length * 8) {
            throw new RuntimeException("IP address with " + bArr.length + " bytes has invalid prefix length " + i);
        }
        int i2 = i / 8;
        byte b = (byte) (255 << (8 - (i % 8)));
        if (i2 < bArr.length) {
            bArr[i2] = (byte) (b & bArr[i2]);
        }
        while (true) {
            i2++;
            if (i2 < bArr.length) {
                bArr[i2] = 0;
            } else {
                return;
            }
        }
    }

    public static String getStandardString(Context context, int i) {
        if (i == 1) {
            return context.getString(R$string.wifitrackerlib_wifi_standard_legacy);
        }
        switch (i) {
            case 4:
                return context.getString(R$string.wifitrackerlib_wifi_standard_11n);
            case 5:
                return context.getString(R$string.wifitrackerlib_wifi_standard_11ac);
            case 6:
                return context.getString(R$string.wifitrackerlib_wifi_standard_11ax);
            case 7:
                return context.getString(R$string.wifitrackerlib_wifi_standard_11ad);
            case 8:
                return context.getString(R$string.wifitrackerlib_wifi_standard_11be);
            default:
                return context.getString(R$string.wifitrackerlib_wifi_standard_unknown);
        }
    }
}
