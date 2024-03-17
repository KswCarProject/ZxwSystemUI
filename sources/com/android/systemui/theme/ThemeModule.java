package com.android.systemui.theme;

import android.content.res.Resources;
import com.android.systemui.R$string;

public class ThemeModule {
    public static String provideLauncherPackage(Resources resources) {
        return resources.getString(R$string.launcher_overlayable_package);
    }

    public static String provideThemePickerPackage(Resources resources) {
        return resources.getString(R$string.themepicker_overlayable_package);
    }
}
