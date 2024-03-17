package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.GroupEntry;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: _Sequences.kt */
public final class GroupCountCoordinator$onBeforeFinalizeFilter$$inlined$filterIsInstance$1 extends Lambda implements Function1<Object, Boolean> {
    public static final GroupCountCoordinator$onBeforeFinalizeFilter$$inlined$filterIsInstance$1 INSTANCE = new GroupCountCoordinator$onBeforeFinalizeFilter$$inlined$filterIsInstance$1();

    public GroupCountCoordinator$onBeforeFinalizeFilter$$inlined$filterIsInstance$1() {
        super(1);
    }

    @NotNull
    public final Boolean invoke(@Nullable Object obj) {
        return Boolean.valueOf(obj instanceof GroupEntry);
    }
}
