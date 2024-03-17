package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnBeforeRenderListListener;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/* compiled from: ShadeEventCoordinator.kt */
public /* synthetic */ class ShadeEventCoordinator$attach$1 implements OnBeforeRenderListListener {
    public final /* synthetic */ ShadeEventCoordinator $tmp0;

    public ShadeEventCoordinator$attach$1(ShadeEventCoordinator shadeEventCoordinator) {
        this.$tmp0 = shadeEventCoordinator;
    }

    public final void onBeforeRenderList(@NotNull List<? extends ListEntry> list) {
        this.$tmp0.onBeforeRenderList(list);
    }
}
