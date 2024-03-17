package com.android.systemui.statusbar.notification.collection.inflation;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.util.ListenerSet;
import org.jetbrains.annotations.NotNull;

/* compiled from: BindEventManager.kt */
public class BindEventManager {
    @NotNull
    public final ListenerSet<Listener> listeners = new ListenerSet<>();

    /* compiled from: BindEventManager.kt */
    public interface Listener {
        void onViewBound(@NotNull NotificationEntry notificationEntry);
    }

    @NotNull
    public final ListenerSet<Listener> getListeners() {
        return this.listeners;
    }

    public final boolean addListener(@NotNull Listener listener) {
        return this.listeners.addIfAbsent(listener);
    }
}
