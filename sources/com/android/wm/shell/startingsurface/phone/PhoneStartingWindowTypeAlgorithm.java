package com.android.wm.shell.startingsurface.phone;

import android.window.StartingWindowInfo;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import com.android.wm.shell.startingsurface.StartingWindowTypeAlgorithm;

public class PhoneStartingWindowTypeAlgorithm implements StartingWindowTypeAlgorithm {
    public static int getSplashscreenType(boolean z, boolean z2) {
        if (z) {
            return 3;
        }
        return z2 ? 4 : 1;
    }

    public int getSuggestedWindowType(StartingWindowInfo startingWindowInfo) {
        StartingWindowInfo startingWindowInfo2 = startingWindowInfo;
        int i = startingWindowInfo2.startingWindowTypeParameter;
        boolean z = (i & 1) != 0;
        boolean z2 = (i & 2) != 0;
        boolean z3 = (i & 4) != 0;
        boolean z4 = (i & 8) != 0;
        boolean z5 = (i & 16) != 0;
        boolean z6 = (i & 32) != 0;
        boolean z7 = (Integer.MIN_VALUE & i) != 0;
        boolean z8 = (i & 64) != 0;
        boolean z9 = startingWindowInfo2.taskInfo.topActivityType == 2;
        if (ShellProtoLogCache.WM_SHELL_STARTING_WINDOW_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW, 299651213, 262143, (String) null, Boolean.valueOf(z), Boolean.valueOf(z2), Boolean.valueOf(z3), Boolean.valueOf(z4), Boolean.valueOf(z5), Boolean.valueOf(z6), Boolean.valueOf(z7), Boolean.valueOf(z8), Boolean.valueOf(z9));
        }
        if (!z9 && (!z3 || z || (z2 && !z5))) {
            return getSplashscreenType(z6, z7);
        }
        if (!z2) {
            return 0;
        }
        if (z4) {
            if (startingWindowInfo2.taskSnapshot != null) {
                return 2;
            }
            if (!z9) {
                return 3;
            }
        }
        if (z8 || z9) {
            return 0;
        }
        return getSplashscreenType(z6, z7);
    }
}
