package com.android.systemui.statusbar;

public class StatusBarState {
    public static String toString(int i) {
        if (i == 0) {
            return "SHADE";
        }
        if (i == 1) {
            return "KEYGUARD";
        }
        if (i == 2) {
            return "SHADE_LOCKED";
        }
        return "UNKNOWN: " + i;
    }
}
