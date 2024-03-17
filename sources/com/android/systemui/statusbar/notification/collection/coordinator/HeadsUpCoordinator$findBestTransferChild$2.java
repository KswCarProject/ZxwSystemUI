package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: HeadsUpCoordinator.kt */
public final class HeadsUpCoordinator$findBestTransferChild$2 extends Lambda implements Function1<NotificationEntry, Boolean> {
    public final /* synthetic */ Function1<String, GroupLocation> $locationLookupByKey;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public HeadsUpCoordinator$findBestTransferChild$2(Function1<? super String, ? extends GroupLocation> function1) {
        super(1);
        this.$locationLookupByKey = function1;
    }

    @NotNull
    public final Boolean invoke(@NotNull NotificationEntry notificationEntry) {
        return Boolean.valueOf(this.$locationLookupByKey.invoke(notificationEntry.getKey()) != GroupLocation.Detached);
    }
}
