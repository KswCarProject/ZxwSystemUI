package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator;
import java.util.function.BiFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: HeadsUpCoordinator.kt */
public final class HeadsUpCoordinator$mNotifCollectionListener$1$onEntryUpdated$posted$1<T, U, R> implements BiFunction {
    public final /* synthetic */ NotificationEntry $entry;
    public final /* synthetic */ boolean $isAlerting;
    public final /* synthetic */ boolean $isBinding;
    public final /* synthetic */ boolean $shouldHeadsUpAgain;
    public final /* synthetic */ boolean $shouldHeadsUpEver;

    public HeadsUpCoordinator$mNotifCollectionListener$1$onEntryUpdated$posted$1(NotificationEntry notificationEntry, boolean z, boolean z2, boolean z3, boolean z4) {
        this.$entry = notificationEntry;
        this.$shouldHeadsUpEver = z;
        this.$shouldHeadsUpAgain = z2;
        this.$isAlerting = z3;
        this.$isBinding = z4;
    }

    @Nullable
    public final HeadsUpCoordinator.PostedEntry apply(@NotNull String str, @Nullable HeadsUpCoordinator.PostedEntry postedEntry) {
        if (postedEntry == null) {
            postedEntry = null;
        } else {
            boolean z = this.$shouldHeadsUpEver;
            boolean z2 = this.$shouldHeadsUpAgain;
            boolean z3 = this.$isAlerting;
            boolean z4 = this.$isBinding;
            boolean z5 = true;
            postedEntry.setWasUpdated(true);
            postedEntry.setShouldHeadsUpEver(postedEntry.getShouldHeadsUpEver() || z);
            if (!postedEntry.getShouldHeadsUpAgain() && !z2) {
                z5 = false;
            }
            postedEntry.setShouldHeadsUpAgain(z5);
            postedEntry.setAlerting(z3);
            postedEntry.setBinding(z4);
        }
        return postedEntry == null ? new HeadsUpCoordinator.PostedEntry(this.$entry, false, true, this.$shouldHeadsUpEver, this.$shouldHeadsUpAgain, this.$isAlerting, this.$isBinding) : postedEntry;
    }
}
