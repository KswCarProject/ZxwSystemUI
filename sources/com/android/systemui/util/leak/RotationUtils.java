package com.android.systemui.util.leak;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

public class RotationUtils {
    public static int getRotation(Context context) {
        int rotation = context.getDisplay().getRotation();
        if (rotation == 1) {
            return 1;
        }
        return rotation == 3 ? 3 : 0;
    }

    public static int getExactRotation(Context context) {
        int rotation = context.getDisplay().getRotation();
        if (rotation == 1) {
            return 1;
        }
        if (rotation == 3) {
            return 3;
        }
        return rotation == 2 ? 2 : 0;
    }

    public static Resources getResourcesForRotation(int i, Context context) {
        int i2 = 2;
        if (i != 0) {
            if (i != 1) {
                if (i != 2) {
                    if (i != 3) {
                        throw new IllegalArgumentException("Unknown rotation: " + i);
                    }
                }
            }
            Configuration configuration = new Configuration(context.getResources().getConfiguration());
            configuration.orientation = i2;
            return context.createConfigurationContext(configuration).getResources();
        }
        i2 = 1;
        Configuration configuration2 = new Configuration(context.getResources().getConfiguration());
        configuration2.orientation = i2;
        return context.createConfigurationContext(configuration2).getResources();
    }
}
