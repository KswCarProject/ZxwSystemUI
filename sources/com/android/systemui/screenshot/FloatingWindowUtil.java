package com.android.systemui.screenshot;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.android.internal.policy.PhoneWindow;

public class FloatingWindowUtil {
    public static float dpToPx(DisplayMetrics displayMetrics, float f) {
        return (f * ((float) displayMetrics.densityDpi)) / 160.0f;
    }

    public static WindowManager.LayoutParams getFloatingWindowParams() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-1, -1, 0, 0, 2036, 918816, -3);
        layoutParams.layoutInDisplayCutoutMode = 3;
        layoutParams.setFitInsetsTypes(0);
        layoutParams.privateFlags |= 536870912;
        return layoutParams;
    }

    public static PhoneWindow getFloatingWindow(Context context) {
        PhoneWindow phoneWindow = new PhoneWindow(context);
        phoneWindow.requestFeature(1);
        phoneWindow.requestFeature(13);
        phoneWindow.setBackgroundDrawableResource(17170445);
        return phoneWindow;
    }
}
