package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifComparator;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: ConversationCoordinator.kt */
public final class ConversationCoordinator$sectioner$1$getComparator$1 extends NotifComparator {
    public final /* synthetic */ ConversationCoordinator this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public ConversationCoordinator$sectioner$1$getComparator$1(ConversationCoordinator conversationCoordinator) {
        super("People");
        this.this$0 = conversationCoordinator;
    }

    public int compare(@NotNull ListEntry listEntry, @NotNull ListEntry listEntry2) {
        return Intrinsics.compare(this.this$0.getPeopleType(listEntry2), this.this$0.getPeopleType(listEntry));
    }
}
