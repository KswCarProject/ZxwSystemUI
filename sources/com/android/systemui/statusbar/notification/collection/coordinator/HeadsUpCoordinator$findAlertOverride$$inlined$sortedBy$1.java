package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator;
import java.util.Comparator;
import kotlin.comparisons.ComparisonsKt__ComparisonsKt;

/* compiled from: Comparisons.kt */
public final class HeadsUpCoordinator$findAlertOverride$$inlined$sortedBy$1<T> implements Comparator {
    public final int compare(T t, T t2) {
        return ComparisonsKt__ComparisonsKt.compareValues(Long.valueOf(-((HeadsUpCoordinator.PostedEntry) t).getEntry().getSbn().getNotification().when), Long.valueOf(-((HeadsUpCoordinator.PostedEntry) t2).getEntry().getSbn().getNotification().when));
    }
}
