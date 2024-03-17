package com.android.systemui.statusbar.policy;

import android.icu.text.DateFormat;
import android.icu.text.DisplayContext;
import android.text.TextUtils;
import java.util.Date;
import java.util.Locale;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: VariableDateViewController.kt */
public final class VariableDateViewControllerKt {
    @NotNull
    public static final DateFormat EMPTY_FORMAT = new VariableDateViewControllerKt$EMPTY_FORMAT$1();

    @NotNull
    public static final String getTextForFormat(@Nullable Date date, @NotNull DateFormat dateFormat) {
        if (dateFormat == EMPTY_FORMAT) {
            return "";
        }
        return dateFormat.format(date);
    }

    @NotNull
    public static final DateFormat getFormatFromPattern(@Nullable String str) {
        if (TextUtils.equals(str, "")) {
            return EMPTY_FORMAT;
        }
        DateFormat instanceForSkeleton = DateFormat.getInstanceForSkeleton(str, Locale.getDefault());
        instanceForSkeleton.setContext(DisplayContext.CAPITALIZATION_FOR_STANDALONE);
        return instanceForSkeleton;
    }
}
