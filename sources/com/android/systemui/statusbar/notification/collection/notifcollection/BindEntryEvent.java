package com.android.systemui.statusbar.notification.collection.notifcollection;

import android.service.notification.StatusBarNotification;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: NotifEvent.kt */
public final class BindEntryEvent extends NotifEvent {
    @NotNull
    public final NotificationEntry entry;
    @NotNull
    public final StatusBarNotification sbn;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof BindEntryEvent)) {
            return false;
        }
        BindEntryEvent bindEntryEvent = (BindEntryEvent) obj;
        return Intrinsics.areEqual((Object) this.entry, (Object) bindEntryEvent.entry) && Intrinsics.areEqual((Object) this.sbn, (Object) bindEntryEvent.sbn);
    }

    public int hashCode() {
        return (this.entry.hashCode() * 31) + this.sbn.hashCode();
    }

    @NotNull
    public String toString() {
        return "BindEntryEvent(entry=" + this.entry + ", sbn=" + this.sbn + ')';
    }

    public BindEntryEvent(@NotNull NotificationEntry notificationEntry, @NotNull StatusBarNotification statusBarNotification) {
        super((DefaultConstructorMarker) null);
        this.entry = notificationEntry;
        this.sbn = statusBarNotification;
    }

    public void dispatchToListener(@NotNull NotifCollectionListener notifCollectionListener) {
        notifCollectionListener.onEntryBind(this.entry, this.sbn);
    }
}
