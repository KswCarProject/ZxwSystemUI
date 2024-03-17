package com.android.systemui.qs;

import android.content.Context;
import com.android.internal.policy.SystemBarUtils;
import com.android.systemui.util.LargeScreenUtils;
import org.jetbrains.annotations.NotNull;

/* compiled from: QSUtils.kt */
public final class QSUtils {
    @NotNull
    public static final QSUtils INSTANCE = new QSUtils();

    public static final int getQsHeaderSystemIconsAreaHeight(@NotNull Context context) {
        if (LargeScreenUtils.shouldUseLargeScreenShadeHeader(context.getResources())) {
            return 0;
        }
        return SystemBarUtils.getQuickQsOffsetHeight(context);
    }
}
