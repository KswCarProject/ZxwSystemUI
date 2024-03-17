package com.android.systemui.statusbar.notification.collection.render;

import android.view.View;
import com.android.systemui.statusbar.notification.collection.render.NodeController;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: RootNodeController.kt */
public final class RootNodeController implements NodeController {
    @NotNull
    public final NotificationListContainer listContainer;
    @NotNull
    public final String nodeLabel = "<root>";
    @NotNull
    public final View view;

    public RootNodeController(@NotNull NotificationListContainer notificationListContainer, @NotNull View view2) {
        this.listContainer = notificationListContainer;
        this.view = view2;
    }

    public void onViewAdded() {
        NodeController.DefaultImpls.onViewAdded(this);
    }

    public void onViewMoved() {
        NodeController.DefaultImpls.onViewMoved(this);
    }

    public void onViewRemoved() {
        NodeController.DefaultImpls.onViewRemoved(this);
    }

    @NotNull
    public View getView() {
        return this.view;
    }

    @NotNull
    public String getNodeLabel() {
        return this.nodeLabel;
    }

    @Nullable
    public View getChildAt(int i) {
        return this.listContainer.getContainerChildAt(i);
    }

    public int getChildCount() {
        return this.listContainer.getContainerChildCount();
    }

    public void addChildAt(@NotNull NodeController nodeController, int i) {
        this.listContainer.addContainerViewAt(nodeController.getView(), i);
        this.listContainer.onNotificationViewUpdateFinished();
        View view2 = nodeController.getView();
        ExpandableNotificationRow expandableNotificationRow = view2 instanceof ExpandableNotificationRow ? (ExpandableNotificationRow) view2 : null;
        if (expandableNotificationRow != null) {
            expandableNotificationRow.setChangingPosition(false);
        }
    }

    public void moveChildTo(@NotNull NodeController nodeController, int i) {
        this.listContainer.changeViewPosition((ExpandableView) nodeController.getView(), i);
    }

    public void removeChild(@NotNull NodeController nodeController, boolean z) {
        if (z) {
            this.listContainer.setChildTransferInProgress(true);
            View view2 = nodeController.getView();
            ExpandableNotificationRow expandableNotificationRow = view2 instanceof ExpandableNotificationRow ? (ExpandableNotificationRow) view2 : null;
            if (expandableNotificationRow != null) {
                expandableNotificationRow.setChangingPosition(true);
            }
        }
        this.listContainer.removeContainerView(nodeController.getView());
        if (z) {
            this.listContainer.setChildTransferInProgress(false);
        }
    }
}
