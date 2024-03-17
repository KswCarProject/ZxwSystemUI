package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.sequences.Sequence;
import org.jetbrains.annotations.NotNull;

/* compiled from: SensitiveContentCoordinator.kt */
public /* synthetic */ class SensitiveContentCoordinatorKt$extractAllRepresentativeEntries$1 extends FunctionReferenceImpl implements Function1<ListEntry, Sequence<? extends NotificationEntry>> {
    public static final SensitiveContentCoordinatorKt$extractAllRepresentativeEntries$1 INSTANCE = new SensitiveContentCoordinatorKt$extractAllRepresentativeEntries$1();

    public SensitiveContentCoordinatorKt$extractAllRepresentativeEntries$1() {
        super(1, SensitiveContentCoordinatorKt.class, "extractAllRepresentativeEntries", "extractAllRepresentativeEntries(Lcom/android/systemui/statusbar/notification/collection/ListEntry;)Lkotlin/sequences/Sequence;", 1);
    }

    @NotNull
    public final Sequence<NotificationEntry> invoke(@NotNull ListEntry listEntry) {
        return SensitiveContentCoordinatorKt.extractAllRepresentativeEntries(listEntry);
    }
}
