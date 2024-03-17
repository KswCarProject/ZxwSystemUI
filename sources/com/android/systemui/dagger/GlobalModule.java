package com.android.systemui.dagger;

import android.content.Context;
import android.util.DisplayMetrics;

public class GlobalModule {
    public DisplayMetrics provideDisplayMetrics(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        context.getDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }
}
