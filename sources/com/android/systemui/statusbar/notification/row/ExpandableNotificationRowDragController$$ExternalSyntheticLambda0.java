package com.android.systemui.statusbar.notification.row;

import android.view.DragEvent;
import android.view.View;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ExpandableNotificationRowDragController$$ExternalSyntheticLambda0 implements View.OnDragListener {
    public final /* synthetic */ ExpandableNotificationRowDragController f$0;

    public /* synthetic */ ExpandableNotificationRowDragController$$ExternalSyntheticLambda0(ExpandableNotificationRowDragController expandableNotificationRowDragController) {
        this.f$0 = expandableNotificationRowDragController;
    }

    public final boolean onDrag(View view, DragEvent dragEvent) {
        return this.f$0.lambda$getDraggedViewDragListener$0(view, dragEvent);
    }
}
