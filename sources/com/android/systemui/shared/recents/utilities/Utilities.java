package com.android.systemui.shared.recents.utilities;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.view.WindowManager;

public class Utilities {
    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0009, code lost:
        if (r3 != 3) goto L_0x0013;
     */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x0015  */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x0017  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x001b  */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x001e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int calculateBackDispositionHints(int r2, int r3, boolean r4, boolean r5) {
        /*
            r0 = 2
            if (r3 == 0) goto L_0x000f
            r1 = 1
            if (r3 == r1) goto L_0x000f
            if (r3 == r0) goto L_0x000f
            r1 = 3
            if (r3 == r1) goto L_0x000c
            goto L_0x0013
        L_0x000c:
            r2 = r2 & -2
            goto L_0x0013
        L_0x000f:
            if (r4 == 0) goto L_0x000c
            r2 = r2 | 1
        L_0x0013:
            if (r4 == 0) goto L_0x0017
            r2 = r2 | r0
            goto L_0x0019
        L_0x0017:
            r2 = r2 & -3
        L_0x0019:
            if (r5 == 0) goto L_0x001e
            r2 = r2 | 4
            goto L_0x0020
        L_0x001e:
            r2 = r2 & -5
        L_0x0020:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.shared.recents.utilities.Utilities.calculateBackDispositionHints(int, int, boolean, boolean):int");
    }

    public static float dpiFromPx(float f, int i) {
        return f / (((float) i) / 160.0f);
    }

    public static boolean isRotationAnimationCCW(int i, int i2) {
        if (i == 0 && i2 == 1) {
            return false;
        }
        if (i == 0 && i2 == 2) {
            return true;
        }
        if (i == 0 && i2 == 3) {
            return true;
        }
        if (i == 1 && i2 == 0) {
            return true;
        }
        if (i == 1 && i2 == 2) {
            return false;
        }
        if (i == 1 && i2 == 3) {
            return true;
        }
        if (i == 2 && i2 == 0) {
            return true;
        }
        if (i == 2 && i2 == 1) {
            return true;
        }
        if (i == 2 && i2 == 3) {
            return false;
        }
        if (i == 3 && i2 == 0) {
            return false;
        }
        if (i == 3 && i2 == 1) {
            return true;
        }
        return i == 3 && i2 == 2;
    }

    @TargetApi(30)
    public static boolean isTablet(Context context) {
        Rect bounds = ((WindowManager) context.getSystemService(WindowManager.class)).getCurrentWindowMetrics().getBounds();
        return dpiFromPx((float) Math.min(bounds.width(), bounds.height()), context.getResources().getConfiguration().densityDpi) >= 600.0f;
    }
}
