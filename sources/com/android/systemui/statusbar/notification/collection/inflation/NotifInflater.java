package com.android.systemui.statusbar.notification.collection.inflation;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.render.NotifViewController;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotifInflater.kt */
public interface NotifInflater {

    /* compiled from: NotifInflater.kt */
    public interface InflationCallback {
        void onInflationFinished(@NotNull NotificationEntry notificationEntry, @NotNull NotifViewController notifViewController);
    }

    void abortInflation(@NotNull NotificationEntry notificationEntry);

    void inflateViews(@NotNull NotificationEntry notificationEntry, @NotNull Params params, @NotNull InflationCallback inflationCallback);

    void rebindViews(@NotNull NotificationEntry notificationEntry, @NotNull Params params, @NotNull InflationCallback inflationCallback);

    /* compiled from: NotifInflater.kt */
    public static final class Params {
        public final boolean isLowPriority;
        @NotNull
        public final String reason;

        public Params(boolean z, @NotNull String str) {
            this.isLowPriority = z;
            this.reason = str;
        }

        public final boolean isLowPriority() {
            return this.isLowPriority;
        }
    }
}
