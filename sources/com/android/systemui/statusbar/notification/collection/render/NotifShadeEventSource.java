package com.android.systemui.statusbar.notification.collection.render;

import org.jetbrains.annotations.NotNull;

/* compiled from: NotifShadeEventSource.kt */
public interface NotifShadeEventSource {
    void setNotifRemovedByUserCallback(@NotNull Runnable runnable);

    void setShadeEmptiedCallback(@NotNull Runnable runnable);
}
