package com.android.systemui.statusbar.notification.icon;

import android.app.Notification;
import android.content.Context;
import com.android.systemui.statusbar.StatusBarIconView;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import org.jetbrains.annotations.NotNull;

/* compiled from: IconBuilder.kt */
public final class IconBuilder {
    @NotNull
    public final Context context;

    public IconBuilder(@NotNull Context context2) {
        this.context = context2;
    }

    @NotNull
    public final StatusBarIconView createIconView(@NotNull NotificationEntry notificationEntry) {
        Context context2 = this.context;
        return new StatusBarIconView(context2, notificationEntry.getSbn().getPackageName() + "/0x" + Integer.toHexString(notificationEntry.getSbn().getId()), notificationEntry.getSbn());
    }

    @NotNull
    public final CharSequence getIconContentDescription(@NotNull Notification notification) {
        return StatusBarIconView.contentDescForNotification(this.context, notification);
    }
}
