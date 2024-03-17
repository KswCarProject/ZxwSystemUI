package com.android.systemui.shared.system;

import android.content.Context;
import android.view.ViewConfiguration;
import java.util.StringJoiner;

public class QuickStepContract {
    public static boolean isAssistantGestureDisabled(int i) {
        if ((131072 & i) != 0) {
            i &= -3;
        }
        if ((i & 3083) != 0) {
            return true;
        }
        return (i & 4) != 0 && (i & 64) == 0;
    }

    public static boolean isBackGestureDisabled(int i) {
        if ((i & 8) != 0 || (32768 & i) != 0 || (33554432 & i) != 0) {
            return false;
        }
        if ((131072 & i) != 0) {
            i &= -3;
        }
        return (i & 70) != 0;
    }

    public static boolean isGesturalMode(int i) {
        return i == 2;
    }

    public static boolean isLegacyMode(int i) {
        return i == 0;
    }

    public static boolean isSwipeUpMode(int i) {
        return i == 1;
    }

    public static String getSystemUiStateString(int i) {
        StringJoiner stringJoiner = new StringJoiner("|");
        String str = "";
        stringJoiner.add((i & 1) != 0 ? "screen_pinned" : str);
        stringJoiner.add((i & 128) != 0 ? "overview_disabled" : str);
        stringJoiner.add((i & 256) != 0 ? "home_disabled" : str);
        stringJoiner.add((i & 1024) != 0 ? "search_disabled" : str);
        stringJoiner.add((i & 2) != 0 ? "navbar_hidden" : str);
        stringJoiner.add((i & 4) != 0 ? "notif_visible" : str);
        stringJoiner.add((i & 2048) != 0 ? "qs_visible" : str);
        stringJoiner.add((i & 64) != 0 ? "keygrd_visible" : str);
        stringJoiner.add((i & 512) != 0 ? "keygrd_occluded" : str);
        stringJoiner.add((i & 8) != 0 ? "bouncer_visible" : str);
        stringJoiner.add((32768 & i) != 0 ? "dialog_showing" : str);
        stringJoiner.add((i & 16) != 0 ? "a11y_click" : str);
        stringJoiner.add((i & 32) != 0 ? "a11y_long_click" : str);
        stringJoiner.add((i & 4096) != 0 ? "tracing" : str);
        stringJoiner.add((i & 8192) != 0 ? "asst_gesture_constrain" : str);
        stringJoiner.add((i & 16384) != 0 ? "bubbles_expanded" : str);
        stringJoiner.add((65536 & i) != 0 ? "one_handed_active" : str);
        stringJoiner.add((131072 & i) != 0 ? "allow_gesture" : str);
        stringJoiner.add((262144 & i) != 0 ? "ime_visible" : str);
        stringJoiner.add((524288 & i) != 0 ? "magnification_overlap" : str);
        stringJoiner.add((1048576 & i) != 0 ? "ime_switcher_showing" : str);
        stringJoiner.add((2097152 & i) != 0 ? "device_dozing" : str);
        stringJoiner.add((4194304 & i) != 0 ? "back_disabled" : str);
        stringJoiner.add((8388608 & i) != 0 ? "bubbles_mange_menu_expanded" : str);
        stringJoiner.add((16777216 & i) != 0 ? "immersive_mode" : str);
        if ((i & 33554432) != 0) {
            str = "vis_win_showing";
        }
        stringJoiner.add(str);
        return stringJoiner.toString();
    }

    public static final float getQuickStepTouchSlopPx(Context context) {
        return ((float) ViewConfiguration.get(context).getScaledTouchSlop()) * 3.0f;
    }
}
