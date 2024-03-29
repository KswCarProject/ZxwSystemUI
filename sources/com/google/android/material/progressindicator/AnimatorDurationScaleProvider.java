package com.google.android.material.progressindicator;

import android.content.ContentResolver;
import android.provider.Settings;

public class AnimatorDurationScaleProvider {
    public static float defaultSystemAnimatorDurationScale = 1.0f;

    public float getSystemAnimatorDurationScale(ContentResolver contentResolver) {
        return Settings.Global.getFloat(contentResolver, "animator_duration_scale", 1.0f);
    }

    public static void setDefaultSystemAnimatorDurationScale(float f) {
        defaultSystemAnimatorDurationScale = f;
    }
}
