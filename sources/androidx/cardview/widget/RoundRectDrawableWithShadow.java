package androidx.cardview.widget;

import android.graphics.drawable.Drawable;

public class RoundRectDrawableWithShadow extends Drawable {
    public static final double COS_45 = Math.cos(Math.toRadians(45.0d));

    public static float calculateVerticalPadding(float f, float f2, boolean z) {
        return z ? (float) (((double) (f * 1.5f)) + ((1.0d - COS_45) * ((double) f2))) : f * 1.5f;
    }

    public static float calculateHorizontalPadding(float f, float f2, boolean z) {
        return z ? (float) (((double) f) + ((1.0d - COS_45) * ((double) f2))) : f;
    }
}
