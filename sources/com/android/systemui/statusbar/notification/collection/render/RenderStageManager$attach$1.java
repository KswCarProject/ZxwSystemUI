package com.android.systemui.statusbar.notification.collection.render;

import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.ShadeListBuilder;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/* compiled from: RenderStageManager.kt */
public /* synthetic */ class RenderStageManager$attach$1 implements ShadeListBuilder.OnRenderListListener {
    public final /* synthetic */ RenderStageManager $tmp0;

    public RenderStageManager$attach$1(RenderStageManager renderStageManager) {
        this.$tmp0 = renderStageManager;
    }

    public final void onRenderList(@NotNull List<? extends ListEntry> list) {
        this.$tmp0.onRenderList(list);
    }
}
