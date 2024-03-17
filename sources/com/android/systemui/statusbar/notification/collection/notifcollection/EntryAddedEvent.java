package com.android.systemui.statusbar.notification.collection.notifcollection;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: NotifEvent.kt */
public final class EntryAddedEvent extends NotifEvent {
    @NotNull
    public final NotificationEntry entry;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof EntryAddedEvent) && Intrinsics.areEqual((Object) this.entry, (Object) ((EntryAddedEvent) obj).entry);
    }

    public int hashCode() {
        return this.entry.hashCode();
    }

    @NotNull
    public String toString() {
        return "EntryAddedEvent(entry=" + this.entry + ')';
    }

    public EntryAddedEvent(@NotNull NotificationEntry notificationEntry) {
        super((DefaultConstructorMarker) null);
        this.entry = notificationEntry;
    }

    public void dispatchToListener(@NotNull NotifCollectionListener notifCollectionListener) {
        notifCollectionListener.onEntryAdded(this.entry);
    }
}
