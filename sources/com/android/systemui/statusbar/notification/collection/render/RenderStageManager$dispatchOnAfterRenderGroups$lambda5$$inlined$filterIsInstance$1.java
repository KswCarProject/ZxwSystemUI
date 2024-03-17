package com.android.systemui.statusbar.notification.collection.render;

import com.android.systemui.statusbar.notification.collection.GroupEntry;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* renamed from: com.android.systemui.statusbar.notification.collection.render.RenderStageManager$dispatchOnAfterRenderGroups$lambda-5$$inlined$filterIsInstance$1  reason: invalid class name */
/* compiled from: _Sequences.kt */
public final class RenderStageManager$dispatchOnAfterRenderGroups$lambda5$$inlined$filterIsInstance$1 extends Lambda implements Function1<Object, Boolean> {
    public static final RenderStageManager$dispatchOnAfterRenderGroups$lambda5$$inlined$filterIsInstance$1 INSTANCE = new RenderStageManager$dispatchOnAfterRenderGroups$lambda5$$inlined$filterIsInstance$1();

    public RenderStageManager$dispatchOnAfterRenderGroups$lambda5$$inlined$filterIsInstance$1() {
        super(1);
    }

    @NotNull
    public final Boolean invoke(@Nullable Object obj) {
        return Boolean.valueOf(obj instanceof GroupEntry);
    }
}
