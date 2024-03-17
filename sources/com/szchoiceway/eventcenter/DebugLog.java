package com.szchoiceway.eventcenter;

import android.util.Log;

public class DebugLog {
    public static boolean prnLog = false;

    public static void e(String str, String str2) {
        if (prnLog) {
            Log.e(str, str2);
        }
    }
}
