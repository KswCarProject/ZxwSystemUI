package com.android.systemui.statusbar.notification.collection.render;

import android.os.Trace;
import com.android.systemui.statusbar.notification.collection.GroupEntry;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.render.NotifViewRenderer;
import java.util.List;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: ShadeViewManager.kt */
public final class ShadeViewManager$viewRenderer$1 implements NotifViewRenderer {
    public final /* synthetic */ ShadeViewManager this$0;

    public ShadeViewManager$viewRenderer$1(ShadeViewManager shadeViewManager) {
        this.this$0 = shadeViewManager;
    }

    public void onDispatchComplete() {
        NotifViewRenderer.DefaultImpls.onDispatchComplete(this);
    }

    public void onRenderList(@NotNull List<? extends ListEntry> list) {
        ShadeViewManager shadeViewManager = this.this$0;
        Trace.beginSection("ShadeViewManager.onRenderList");
        try {
            shadeViewManager.viewDiffer.applySpec(shadeViewManager.specBuilder.buildNodeSpec(shadeViewManager.rootController, list));
            Unit unit = Unit.INSTANCE;
        } finally {
            Trace.endSection();
        }
    }

    @NotNull
    public NotifStackController getStackController() {
        return this.this$0.stackController;
    }

    @NotNull
    public NotifGroupController getGroupController(@NotNull GroupEntry groupEntry) {
        NotifViewBarn access$getViewBarn$p = this.this$0.viewBarn;
        NotificationEntry summary = groupEntry.getSummary();
        if (summary != null) {
            return access$getViewBarn$p.requireGroupController(summary);
        }
        throw new IllegalStateException(Intrinsics.stringPlus("No Summary: ", groupEntry).toString());
    }

    @NotNull
    public NotifRowController getRowController(@NotNull NotificationEntry notificationEntry) {
        return this.this$0.viewBarn.requireRowController(notificationEntry);
    }
}
