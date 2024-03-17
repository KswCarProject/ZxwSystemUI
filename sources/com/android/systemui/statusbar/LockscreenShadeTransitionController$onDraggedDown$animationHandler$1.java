package com.android.systemui.statusbar;

import android.view.View;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;

/* compiled from: LockscreenShadeTransitionController.kt */
public final class LockscreenShadeTransitionController$onDraggedDown$animationHandler$1 extends Lambda implements Function1<Long, Unit> {
    public final /* synthetic */ View $startingChild;
    public final /* synthetic */ LockscreenShadeTransitionController this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public LockscreenShadeTransitionController$onDraggedDown$animationHandler$1(View view, LockscreenShadeTransitionController lockscreenShadeTransitionController) {
        super(1);
        this.$startingChild = view;
        this.this$0 = lockscreenShadeTransitionController;
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke(((Number) obj).longValue());
        return Unit.INSTANCE;
    }

    public final void invoke(long j) {
        View view = this.$startingChild;
        if (view instanceof ExpandableNotificationRow) {
            ((ExpandableNotificationRow) view).onExpandedByGesture(true);
        }
        this.this$0.getNotificationPanelController().animateToFullShade(j);
        this.this$0.getNotificationPanelController().setTransitionToFullShadeAmount(0.0f, true, j);
        this.this$0.forceApplyAmount = true;
        this.this$0.logger.logDragDownAmountReset();
        this.this$0.setDragDownAmount$frameworks__base__packages__SystemUI__android_common__SystemUI_core(0.0f);
        this.this$0.forceApplyAmount = false;
    }
}
