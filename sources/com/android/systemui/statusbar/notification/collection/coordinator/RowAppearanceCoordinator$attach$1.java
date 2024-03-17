package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnBeforeRenderListListener;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/* compiled from: RowAppearanceCoordinator.kt */
public /* synthetic */ class RowAppearanceCoordinator$attach$1 implements OnBeforeRenderListListener {
    public final /* synthetic */ RowAppearanceCoordinator $tmp0;

    public RowAppearanceCoordinator$attach$1(RowAppearanceCoordinator rowAppearanceCoordinator) {
        this.$tmp0 = rowAppearanceCoordinator;
    }

    public final void onBeforeRenderList(@NotNull List<? extends ListEntry> list) {
        this.$tmp0.onBeforeRenderList(list);
    }
}
