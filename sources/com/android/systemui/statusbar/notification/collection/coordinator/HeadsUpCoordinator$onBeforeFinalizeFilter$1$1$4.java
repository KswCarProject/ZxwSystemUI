package com.android.systemui.statusbar.notification.collection.coordinator;

import java.util.Map;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
import org.jetbrains.annotations.NotNull;

/* compiled from: HeadsUpCoordinator.kt */
public /* synthetic */ class HeadsUpCoordinator$onBeforeFinalizeFilter$1$1$4 extends FunctionReferenceImpl implements Function1<String, GroupLocation> {
    public HeadsUpCoordinator$onBeforeFinalizeFilter$1$1$4(Object obj) {
        super(1, obj, HeadsUpCoordinatorKt.class, "getLocation", "getLocation(Ljava/util/Map;Ljava/lang/String;)Lcom/android/systemui/statusbar/notification/collection/coordinator/GroupLocation;", 1);
    }

    @NotNull
    public final GroupLocation invoke(@NotNull String str) {
        return HeadsUpCoordinatorKt.getLocation((Map) this.receiver, str);
    }
}
