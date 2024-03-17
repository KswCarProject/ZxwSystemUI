package com.android.systemui.statusbar.notification.collection.notifcollection;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: NotifEvent.kt */
public final class EntryUpdatedEvent extends NotifEvent {
    @NotNull
    public final NotificationEntry entry;
    public final boolean fromSystem;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof EntryUpdatedEvent)) {
            return false;
        }
        EntryUpdatedEvent entryUpdatedEvent = (EntryUpdatedEvent) obj;
        return Intrinsics.areEqual((Object) this.entry, (Object) entryUpdatedEvent.entry) && this.fromSystem == entryUpdatedEvent.fromSystem;
    }

    public int hashCode() {
        int hashCode = this.entry.hashCode() * 31;
        boolean z = this.fromSystem;
        if (z) {
            z = true;
        }
        return hashCode + (z ? 1 : 0);
    }

    @NotNull
    public String toString() {
        return "EntryUpdatedEvent(entry=" + this.entry + ", fromSystem=" + this.fromSystem + ')';
    }

    public EntryUpdatedEvent(@NotNull NotificationEntry notificationEntry, boolean z) {
        super((DefaultConstructorMarker) null);
        this.entry = notificationEntry;
        this.fromSystem = z;
    }

    public void dispatchToListener(@NotNull NotifCollectionListener notifCollectionListener) {
        notifCollectionListener.onEntryUpdated(this.entry, this.fromSystem);
    }
}
