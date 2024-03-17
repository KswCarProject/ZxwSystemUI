package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.List;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt__SequenceBuilderKt;
import kotlin.sequences.SequencesKt___SequencesKt;

/* compiled from: SensitiveContentCoordinator.kt */
public final class SensitiveContentCoordinatorKt {
    public static final Sequence<NotificationEntry> extractAllRepresentativeEntries(List<? extends ListEntry> list) {
        return SequencesKt___SequencesKt.flatMap(CollectionsKt___CollectionsKt.asSequence(list), SensitiveContentCoordinatorKt$extractAllRepresentativeEntries$1.INSTANCE);
    }

    public static final Sequence<NotificationEntry> extractAllRepresentativeEntries(ListEntry listEntry) {
        return SequencesKt__SequenceBuilderKt.sequence(new SensitiveContentCoordinatorKt$extractAllRepresentativeEntries$2(listEntry, (Continuation<? super SensitiveContentCoordinatorKt$extractAllRepresentativeEntries$2>) null));
    }
}
