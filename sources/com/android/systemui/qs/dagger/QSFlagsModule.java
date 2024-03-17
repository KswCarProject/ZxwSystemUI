package com.android.systemui.qs.dagger;

import android.content.Context;
import android.hardware.display.ColorDisplayManager;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.util.settings.GlobalSettings;

public interface QSFlagsModule {
    static boolean isReduceBrightColorsAvailable(Context context) {
        return ColorDisplayManager.isReduceBrightColorsAvailable(context);
    }

    static boolean isPMLiteEnabled(FeatureFlags featureFlags, GlobalSettings globalSettings) {
        if (!featureFlags.isEnabled(Flags.POWER_MENU_LITE) || globalSettings.getInt("sysui_pm_lite", 1) == 0) {
            return false;
        }
        return true;
    }
}
