package com.android.systemui.statusbar.notification.collection.notifcollection;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: NotifEvent.kt */
public final class InitEntryEvent extends NotifEvent {
    @NotNull
    public final NotificationEntry entry;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof InitEntryEvent) && Intrinsics.areEqual((Object) this.entry, (Object) ((InitEntryEvent) obj).entry);
    }

    public int hashCode() {
        return this.entry.hashCode();
    }

    @NotNull
    public String toString() {
        return "InitEntryEvent(entry=" + this.entry + ')';
    }

    public InitEntryEvent(@NotNull NotificationEntry notificationEntry) {
        super((DefaultConstructorMarker) null);
        this.entry = notificationEntry;
    }

    public void dispatchToListener(@NotNull NotifCollectionListener notifCollectionListener) {
        notifCollectionListener.onEntryInit(this.entry);
    }
}
