package com.android.systemui.statusbar.connectivity;

import com.android.settingslib.AccessibilityContentDescriptions;
import com.android.settingslib.R$drawable;
import com.android.settingslib.SignalIcon$IconGroup;

public class WifiIcons {
    public static final int[][] QS_WIFI_4_SIGNAL_STRENGTH;
    public static final int[][] QS_WIFI_5_SIGNAL_STRENGTH;
    public static final int[][] QS_WIFI_6_SIGNAL_STRENGTH;
    public static final int[][] QS_WIFI_SIGNAL_STRENGTH;
    public static final SignalIcon$IconGroup UNMERGED_WIFI;
    public static final int[] WIFI_4_FULL_ICONS;
    public static final int[] WIFI_4_NO_INTERNET_ICONS;
    public static final int[][] WIFI_4_SIGNAL_STRENGTH;
    public static final int[] WIFI_5_FULL_ICONS;
    public static final int[] WIFI_5_NO_INTERNET_ICONS;
    public static final int[][] WIFI_5_SIGNAL_STRENGTH;
    public static final int[] WIFI_6_FULL_ICONS;
    public static final int[] WIFI_6_NO_INTERNET_ICONS;
    public static final int[][] WIFI_6_SIGNAL_STRENGTH;
    public static final int[] WIFI_FULL_ICONS;
    public static final int WIFI_LEVEL_COUNT;
    public static final int[] WIFI_NO_INTERNET_ICONS;
    public static final int[][] WIFI_SIGNAL_STRENGTH;

    static {
        int[] iArr = {17302912, 17302913, 17302914, 17302915, 17302916};
        WIFI_FULL_ICONS = iArr;
        int[] iArr2 = {R$drawable.ic_no_internet_wifi_signal_0, R$drawable.ic_no_internet_wifi_signal_1, R$drawable.ic_no_internet_wifi_signal_2, R$drawable.ic_no_internet_wifi_signal_3, R$drawable.ic_no_internet_wifi_signal_4};
        WIFI_NO_INTERNET_ICONS = iArr2;
        int[] iArr3 = {17302897, 17302898, 17302899, 17302900, 17302901};
        WIFI_4_FULL_ICONS = iArr3;
        int[] iArr4 = {R$drawable.ic_qs_wifi_4_0, R$drawable.ic_qs_wifi_4_1, R$drawable.ic_qs_wifi_4_2, R$drawable.ic_qs_wifi_4_3, R$drawable.ic_qs_wifi_4_4};
        WIFI_4_NO_INTERNET_ICONS = iArr4;
        int[] iArr5 = {17302902, 17302903, 17302904, 17302905, 17302906};
        WIFI_5_FULL_ICONS = iArr5;
        int[] iArr6 = {R$drawable.ic_qs_wifi_5_0, R$drawable.ic_qs_wifi_5_1, R$drawable.ic_qs_wifi_5_2, R$drawable.ic_qs_wifi_5_3, R$drawable.ic_qs_wifi_5_4};
        WIFI_5_NO_INTERNET_ICONS = iArr6;
        int[] iArr7 = {17302907, 17302908, 17302909, 17302910, 17302911};
        WIFI_6_FULL_ICONS = iArr7;
        int[] iArr8 = {R$drawable.ic_qs_wifi_6_0, R$drawable.ic_qs_wifi_6_1, R$drawable.ic_qs_wifi_6_2, R$drawable.ic_qs_wifi_6_3, R$drawable.ic_qs_wifi_6_4};
        WIFI_6_NO_INTERNET_ICONS = iArr8;
        int[][] iArr9 = {iArr2, iArr};
        QS_WIFI_SIGNAL_STRENGTH = iArr9;
        WIFI_SIGNAL_STRENGTH = iArr9;
        int[][] iArr10 = {iArr4, iArr3};
        QS_WIFI_4_SIGNAL_STRENGTH = iArr10;
        WIFI_4_SIGNAL_STRENGTH = iArr10;
        int[][] iArr11 = {iArr6, iArr5};
        QS_WIFI_5_SIGNAL_STRENGTH = iArr11;
        WIFI_5_SIGNAL_STRENGTH = iArr11;
        int[][] iArr12 = {iArr8, iArr7};
        QS_WIFI_6_SIGNAL_STRENGTH = iArr12;
        WIFI_6_SIGNAL_STRENGTH = iArr12;
        WIFI_LEVEL_COUNT = iArr9[0].length;
        UNMERGED_WIFI = new SignalIcon$IconGroup("Wi-Fi Icons", iArr9, iArr9, AccessibilityContentDescriptions.WIFI_CONNECTION_STRENGTH, 17302912, 17302912, 17302912, 17302912, AccessibilityContentDescriptions.WIFI_NO_CONNECTION);
    }
}
