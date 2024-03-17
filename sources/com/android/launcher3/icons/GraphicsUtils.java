package com.android.launcher3.icons;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.RegionIterator;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.ColorDrawable;

public class GraphicsUtils {
    public static Runnable sOnNewBitmapRunnable = new GraphicsUtils$$ExternalSyntheticLambda0();

    public static /* synthetic */ void lambda$static$0() {
    }

    public static int setColorAlphaBound(int i, int i2) {
        if (i2 < 0) {
            i2 = 0;
        } else if (i2 > 255) {
            i2 = 255;
        }
        return (i & 16777215) | (i2 << 24);
    }

    public static int getArea(Region region) {
        RegionIterator regionIterator = new RegionIterator(region);
        Rect rect = new Rect();
        int i = 0;
        while (regionIterator.next(rect)) {
            i += rect.width() * rect.height();
        }
        return i;
    }

    public static void noteNewBitmapCreated() {
        sOnNewBitmapRunnable.run();
    }

    public static Path getShapePath(int i) {
        AdaptiveIconDrawable adaptiveIconDrawable = new AdaptiveIconDrawable(new ColorDrawable(-16777216), new ColorDrawable(-16777216));
        adaptiveIconDrawable.setBounds(0, 0, i, i);
        return new Path(adaptiveIconDrawable.getIconMask());
    }

    public static int getAttrColor(Context context, int i) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{i});
        int color = obtainStyledAttributes.getColor(0, 0);
        obtainStyledAttributes.recycle();
        return color;
    }

    public static float getFloat(Context context, int i, float f) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{i});
        float f2 = obtainStyledAttributes.getFloat(0, f);
        obtainStyledAttributes.recycle();
        return f2;
    }
}
