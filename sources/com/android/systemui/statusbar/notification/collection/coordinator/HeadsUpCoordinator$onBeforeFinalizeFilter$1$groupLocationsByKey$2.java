package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.ListEntry;
import java.util.List;
import java.util.Map;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: HeadsUpCoordinator.kt */
public final class HeadsUpCoordinator$onBeforeFinalizeFilter$1$groupLocationsByKey$2 extends Lambda implements Function0<Map<String, ? extends GroupLocation>> {
    public final /* synthetic */ List<ListEntry> $list;
    public final /* synthetic */ HeadsUpCoordinator this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public HeadsUpCoordinator$onBeforeFinalizeFilter$1$groupLocationsByKey$2(HeadsUpCoordinator headsUpCoordinator, List<? extends ListEntry> list) {
        super(0);
        this.this$0 = headsUpCoordinator;
        this.$list = list;
    }

    @NotNull
    public final Map<String, GroupLocation> invoke() {
        return this.this$0.getGroupLocationsByKey(this.$list);
    }
}
