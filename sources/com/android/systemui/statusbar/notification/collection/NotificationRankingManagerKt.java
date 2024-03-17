package com.android.systemui.statusbar.notification.collection;

import android.app.Notification;
import android.service.notification.StatusBarNotification;
import com.android.systemui.theme.ThemeOverlayApplier;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: NotificationRankingManager.kt */
public final class NotificationRankingManagerKt {
    public static final boolean isSystemMax(NotificationEntry notificationEntry) {
        return notificationEntry.getImportance() >= 4 && isSystemNotification(notificationEntry.getSbn());
    }

    public static final boolean isSystemNotification(StatusBarNotification statusBarNotification) {
        return Intrinsics.areEqual((Object) ThemeOverlayApplier.ANDROID_PACKAGE, (Object) statusBarNotification.getPackageName()) || Intrinsics.areEqual((Object) ThemeOverlayApplier.SYSUI_PACKAGE, (Object) statusBarNotification.getPackageName());
    }

    public static final boolean isImportantCall(NotificationEntry notificationEntry) {
        return notificationEntry.getSbn().getNotification().isStyle(Notification.CallStyle.class) && notificationEntry.getImportance() > 1;
    }

    public static final boolean isColorizedForegroundService(NotificationEntry notificationEntry) {
        Notification notification = notificationEntry.getSbn().getNotification();
        return notification.isForegroundService() && notification.isColorized() && notificationEntry.getImportance() > 1;
    }
}
