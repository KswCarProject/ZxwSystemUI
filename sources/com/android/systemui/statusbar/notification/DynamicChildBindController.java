package com.android.systemui.statusbar.notification;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.NotifBindPipeline;
import com.android.systemui.statusbar.notification.row.RowContentBindParams;
import com.android.systemui.statusbar.notification.row.RowContentBindStage;
import java.util.List;
import java.util.Map;

public class DynamicChildBindController {
    public final int mChildBindCutoff;
    public final RowContentBindStage mStage;

    public DynamicChildBindController(RowContentBindStage rowContentBindStage) {
        this(rowContentBindStage, 9);
    }

    public DynamicChildBindController(RowContentBindStage rowContentBindStage, int i) {
        this.mStage = rowContentBindStage;
        this.mChildBindCutoff = i;
    }

    public void updateContentViews(Map<NotificationEntry, List<NotificationEntry>> map) {
        for (NotificationEntry next : map.keySet()) {
            List list = map.get(next);
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    NotificationEntry notificationEntry = (NotificationEntry) list.get(i);
                    if (i >= this.mChildBindCutoff) {
                        if (hasContent(notificationEntry)) {
                            freeContent(notificationEntry);
                        }
                    } else if (!hasContent(notificationEntry)) {
                        bindContent(notificationEntry);
                    }
                }
            } else if (!hasContent(next)) {
                bindContent(next);
            }
        }
    }

    public final boolean hasContent(NotificationEntry notificationEntry) {
        ExpandableNotificationRow row = notificationEntry.getRow();
        return (row.getPrivateLayout().getContractedChild() == null && row.getPrivateLayout().getExpandedChild() == null) ? false : true;
    }

    public final void freeContent(NotificationEntry notificationEntry) {
        RowContentBindParams rowContentBindParams = (RowContentBindParams) this.mStage.getStageParams(notificationEntry);
        rowContentBindParams.markContentViewsFreeable(1);
        rowContentBindParams.markContentViewsFreeable(2);
        this.mStage.requestRebind(notificationEntry, (NotifBindPipeline.BindCallback) null);
    }

    public final void bindContent(NotificationEntry notificationEntry) {
        RowContentBindParams rowContentBindParams = (RowContentBindParams) this.mStage.getStageParams(notificationEntry);
        rowContentBindParams.requireContentViews(1);
        rowContentBindParams.requireContentViews(2);
        this.mStage.requestRebind(notificationEntry, (NotifBindPipeline.BindCallback) null);
    }
}
