package com.android.systemui.util;

import android.content.res.Resources;
import com.android.systemui.R$bool;
import org.jetbrains.annotations.NotNull;

/* compiled from: LargeScreenUtils.kt */
public final class LargeScreenUtils {
    @NotNull
    public static final LargeScreenUtils INSTANCE = new LargeScreenUtils();

    public static final boolean shouldUseSplitNotificationShade(@NotNull Resources resources) {
        return resources.getBoolean(R$bool.config_use_split_notification_shade);
    }

    public static final boolean shouldUseLargeScreenShadeHeader(@NotNull Resources resources) {
        return resources.getBoolean(R$bool.config_use_large_screen_shade_header);
    }
}
