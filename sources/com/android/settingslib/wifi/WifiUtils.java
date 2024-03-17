package com.android.settingslib.wifi;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import com.android.settingslib.R$drawable;
import java.util.Iterator;
import java.util.Map;

public class WifiUtils {
    public static final String ACTION_WIFI_DIALOG = "com.android.settings.WIFI_DIALOG";
    public static final String EXTRA_CHOSEN_WIFI_ENTRY_KEY = "key_chosen_wifientry_key";
    public static final String EXTRA_CONNECT_FOR_CALLER = "connect_for_caller";
    public static final int[] NO_INTERNET_WIFI_PIE = {R$drawable.ic_no_internet_wifi_signal_0, R$drawable.ic_no_internet_wifi_signal_1, R$drawable.ic_no_internet_wifi_signal_2, R$drawable.ic_no_internet_wifi_signal_3, R$drawable.ic_no_internet_wifi_signal_4};
    public static final int[] WIFI_4_PIE = {17302897, 17302898, 17302899, 17302900, 17302901};
    public static final int[] WIFI_5_PIE = {17302902, 17302903, 17302904, 17302905, 17302906};
    public static final int[] WIFI_6_PIE = {17302907, 17302908, 17302909, 17302910, 17302911};
    public static final int[] WIFI_PIE = {17302912, 17302913, 17302914, 17302915, 17302916};

    public static String getVisibilityStatus(AccessPoint accessPoint) {
        String str;
        int i;
        StringBuilder sb;
        StringBuilder sb2;
        StringBuilder sb3;
        AccessPoint accessPoint2 = accessPoint;
        WifiInfo info = accessPoint.getInfo();
        StringBuilder sb4 = new StringBuilder();
        StringBuilder sb5 = new StringBuilder();
        StringBuilder sb6 = new StringBuilder();
        StringBuilder sb7 = new StringBuilder();
        StringBuilder sb8 = new StringBuilder();
        int i2 = 0;
        if (!accessPoint.isActive() || info == null) {
            str = null;
        } else {
            str = info.getBSSID();
            if (str != null) {
                sb4.append(" ");
                sb4.append(str);
            }
            sb4.append(" standard = ");
            sb4.append(info.getWifiStandard());
            sb4.append(" rssi=");
            sb4.append(info.getRssi());
            sb4.append(" ");
            sb4.append(" score=");
            sb4.append(info.getScore());
            if (accessPoint.getSpeed() != 0) {
                sb4.append(" speed=");
                sb4.append(accessPoint.getSpeedLabel());
            }
            sb4.append(String.format(" tx=%.1f,", new Object[]{Double.valueOf(info.getSuccessfulTxPacketsPerSecond())}));
            sb4.append(String.format("%.1f,", new Object[]{Double.valueOf(info.getRetriedTxPacketsPerSecond())}));
            sb4.append(String.format("%.1f ", new Object[]{Double.valueOf(info.getLostTxPacketsPerSecond())}));
            sb4.append(String.format("rx=%.1f", new Object[]{Double.valueOf(info.getSuccessfulRxPacketsPerSecond())}));
        }
        long elapsedRealtime = SystemClock.elapsedRealtime();
        Iterator<ScanResult> it = accessPoint.getScanResults().iterator();
        StringBuilder sb9 = sb4;
        StringBuilder sb10 = sb7;
        int i3 = 0;
        int i4 = 0;
        int i5 = -127;
        int i6 = -127;
        int i7 = -127;
        int i8 = -127;
        int i9 = 0;
        while (true) {
            i = i6;
            if (!it.hasNext()) {
                break;
            }
            ScanResult next = it.next();
            if (next == null) {
                i6 = i;
            } else {
                Iterator<ScanResult> it2 = it;
                int i10 = next.frequency;
                int i11 = i3;
                if (i10 >= 5935 && i10 <= 7115) {
                    i4++;
                    int i12 = next.level;
                    if (i12 > i5) {
                        i5 = i12;
                    }
                    if (i4 <= 4) {
                        sb8.append(verboseScanResultSummary(accessPoint2, next, str, elapsedRealtime));
                    }
                } else if (i10 >= 4900 && i10 <= 5900) {
                    i9++;
                    int i13 = next.level;
                    if (i13 > i8) {
                        i8 = i13;
                    }
                    if (i9 <= 4) {
                        sb6.append(verboseScanResultSummary(accessPoint2, next, str, elapsedRealtime));
                    }
                } else if (i10 >= 2400 && i10 <= 2500) {
                    i2++;
                    int i14 = next.level;
                    if (i14 > i7) {
                        i7 = i14;
                    }
                    if (i2 <= 4) {
                        sb5.append(verboseScanResultSummary(accessPoint2, next, str, elapsedRealtime));
                    }
                } else if (i10 < 58320 || i10 > 70200) {
                    sb = sb8;
                    sb2 = sb10;
                    i6 = i;
                    i3 = i11;
                    sb10 = sb2;
                    it = it2;
                    sb8 = sb;
                } else {
                    i3 = i11 + 1;
                    int i15 = next.level;
                    sb = sb8;
                    int i16 = i;
                    if (i15 <= i16) {
                        i15 = i16;
                    }
                    if (i3 <= 4) {
                        sb3 = sb10;
                        sb3.append(verboseScanResultSummary(accessPoint2, next, str, elapsedRealtime));
                    } else {
                        sb3 = sb10;
                    }
                    i6 = i15;
                    sb2 = sb3;
                    sb10 = sb2;
                    it = it2;
                    sb8 = sb;
                }
                sb = sb8;
                sb2 = sb10;
                i6 = i;
                i3 = i11;
                sb10 = sb2;
                it = it2;
                sb8 = sb;
            }
        }
        StringBuilder sb11 = sb8;
        int i17 = i3;
        StringBuilder sb12 = sb10;
        int i18 = i;
        StringBuilder sb13 = sb9;
        sb13.append(" [");
        if (i2 > 0) {
            sb13.append("(");
            sb13.append(i2);
            sb13.append(")");
            if (i2 > 4) {
                sb13.append("max=");
                sb13.append(i7);
                sb13.append(",");
            }
            sb13.append(sb5.toString());
        }
        sb13.append(";");
        if (i9 > 0) {
            sb13.append("(");
            sb13.append(i9);
            sb13.append(")");
            if (i9 > 4) {
                sb13.append("max=");
                sb13.append(i8);
                sb13.append(",");
            }
            sb13.append(sb6.toString());
        }
        sb13.append(";");
        if (i17 > 0) {
            sb13.append("(");
            int i19 = i17;
            sb13.append(i19);
            sb13.append(")");
            if (i19 > 4) {
                sb13.append("max=");
                sb13.append(i18);
                sb13.append(",");
            }
            sb13.append(sb12.toString());
        }
        sb13.append(";");
        if (i4 > 0) {
            sb13.append("(");
            sb13.append(i4);
            sb13.append(")");
            if (i4 > 4) {
                sb13.append("max=");
                sb13.append(i5);
                sb13.append(",");
            }
            sb13.append(sb11.toString());
        }
        sb13.append("]");
        return sb13.toString();
    }

    public static String verboseScanResultSummary(AccessPoint accessPoint, ScanResult scanResult, String str, long j) {
        StringBuilder sb = new StringBuilder();
        sb.append(" \n{");
        sb.append(scanResult.BSSID);
        if (scanResult.BSSID.equals(str)) {
            sb.append("*");
        }
        sb.append("=");
        sb.append(scanResult.frequency);
        sb.append(",");
        sb.append(scanResult.level);
        int specificApSpeed = getSpecificApSpeed(scanResult, accessPoint.getScoredNetworkCache());
        if (specificApSpeed != 0) {
            sb.append(",");
            sb.append(accessPoint.getSpeedLabel(specificApSpeed));
        }
        sb.append(",");
        sb.append(((int) (j - (scanResult.timestamp / 1000))) / 1000);
        sb.append("s");
        sb.append("}");
        return sb.toString();
    }

    public static int getSpecificApSpeed(ScanResult scanResult, Map<String, TimestampedScoredNetwork> map) {
        TimestampedScoredNetwork timestampedScoredNetwork = map.get(scanResult.BSSID);
        if (timestampedScoredNetwork == null) {
            return 0;
        }
        return timestampedScoredNetwork.getScore().calculateBadge(scanResult.level);
    }

    public static int getInternetIconResource(int i, boolean z) {
        return getInternetIconResource(i, z, 0);
    }

    public static int getInternetIconResource(int i, boolean z, int i2) {
        if (i < 0) {
            Log.e("WifiUtils", "Wi-Fi level is out of range! level:" + i);
            i = 0;
        } else {
            int[] iArr = WIFI_PIE;
            if (i >= iArr.length) {
                Log.e("WifiUtils", "Wi-Fi level is out of range! level:" + i);
                i = iArr.length + -1;
            }
        }
        if (z) {
            return NO_INTERNET_WIFI_PIE[i];
        }
        if (i2 == 4) {
            return WIFI_4_PIE[i];
        }
        if (i2 == 5) {
            return WIFI_5_PIE[i];
        }
        if (i2 != 6) {
            return WIFI_PIE[i];
        }
        return WIFI_6_PIE[i];
    }

    public static class InternetIconInjector {
        public final Context mContext;

        public InternetIconInjector(Context context) {
            this.mContext = context;
        }

        public Drawable getIcon(boolean z, int i) {
            return this.mContext.getDrawable(WifiUtils.getInternetIconResource(i, z));
        }
    }

    public static Intent getWifiDialogIntent(String str, boolean z) {
        Intent intent = new Intent(ACTION_WIFI_DIALOG);
        intent.putExtra(EXTRA_CHOSEN_WIFI_ENTRY_KEY, str);
        intent.putExtra(EXTRA_CONNECT_FOR_CALLER, z);
        return intent;
    }

    public static Intent getWifiDetailsSettingsIntent(String str) {
        Intent intent = new Intent("android.settings.WIFI_DETAILS_SETTINGS");
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_CHOSEN_WIFI_ENTRY_KEY, str);
        intent.putExtra(":settings:show_fragment_args", bundle);
        return intent;
    }
}
