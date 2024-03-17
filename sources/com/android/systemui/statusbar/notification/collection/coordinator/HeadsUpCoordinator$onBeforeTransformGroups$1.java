package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator;
import kotlin.Unit;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: HeadsUpCoordinator.kt */
public final class HeadsUpCoordinator$onBeforeTransformGroups$1 extends Lambda implements Function1<HunMutator, Unit> {
    public final /* synthetic */ HeadsUpCoordinator this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public HeadsUpCoordinator$onBeforeTransformGroups$1(HeadsUpCoordinator headsUpCoordinator) {
        super(1);
        this.this$0 = headsUpCoordinator;
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke((HunMutator) obj);
        return Unit.INSTANCE;
    }

    public final void invoke(@NotNull HunMutator hunMutator) {
        HeadsUpCoordinator headsUpCoordinator = this.this$0;
        for (HeadsUpCoordinator.PostedEntry postedEntry : CollectionsKt___CollectionsKt.toList(this.this$0.mPostedEntries.values())) {
            if (!postedEntry.getEntry().getSbn().isGroup()) {
                headsUpCoordinator.handlePostedEntry(postedEntry, hunMutator, "non-group");
                headsUpCoordinator.mPostedEntries.remove(postedEntry.getKey());
            }
        }
    }
}
