package com.android.settingslib;

import android.content.Context;
import android.os.SystemProperties;
import android.provider.Settings;

public class WirelessUtils {
    public static boolean isAirplaneModeOn(Context context) {
        if (Settings.Global.getInt(context.getContentResolver(), "airplane_mode_on", 0) != 0 || isFTModeAndSleepTest()) {
            return true;
        }
        return false;
    }

    public static boolean isFTModeAndSleepTest() {
        return SystemProperties.get("ro.boot.ftmode", "false").equals("true") && SystemProperties.get("sys.sleep.test.ready", "false").equals("true");
    }
}
