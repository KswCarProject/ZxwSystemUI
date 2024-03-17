package com.android.systemui.statusbar.notification.collection.render;

import com.android.systemui.statusbar.notification.collection.GroupEntry;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotifViewRenderer.kt */
public interface NotifViewRenderer {

    /* compiled from: NotifViewRenderer.kt */
    public static final class DefaultImpls {
        public static void onDispatchComplete(@NotNull NotifViewRenderer notifViewRenderer) {
        }
    }

    @NotNull
    NotifGroupController getGroupController(@NotNull GroupEntry groupEntry);

    @NotNull
    NotifRowController getRowController(@NotNull NotificationEntry notificationEntry);

    @NotNull
    NotifStackController getStackController();

    void onDispatchComplete();

    void onRenderList(@NotNull List<? extends ListEntry> list);
}
