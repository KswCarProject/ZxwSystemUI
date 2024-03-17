package com.android.systemui.statusbar.notification;

import com.android.systemui.statusbar.notification.collection.ListEntry;
import org.jetbrains.annotations.Nullable;

/* compiled from: NotificationUtils.kt */
public final class NotificationUtilsKt {
    @Nullable
    public static final String getLogKey(@Nullable ListEntry listEntry) {
        if (listEntry == null) {
            return null;
        }
        return NotificationUtils.logKey(listEntry);
    }
}
