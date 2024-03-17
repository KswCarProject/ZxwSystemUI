package com.android.systemui.statusbar.notification.row;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.BindStage;
import com.android.systemui.statusbar.notification.row.NotificationRowContentBinder;

public class RowContentBindStage extends BindStage<RowContentBindParams> {
    public final NotificationRowContentBinder mBinder;
    public final RowContentBindStageLogger mLogger;
    public final NotifInflationErrorManager mNotifInflationErrorManager;

    public RowContentBindStage(NotificationRowContentBinder notificationRowContentBinder, NotifInflationErrorManager notifInflationErrorManager, RowContentBindStageLogger rowContentBindStageLogger) {
        this.mBinder = notificationRowContentBinder;
        this.mNotifInflationErrorManager = notifInflationErrorManager;
        this.mLogger = rowContentBindStageLogger;
    }

    public void executeStage(NotificationEntry notificationEntry, ExpandableNotificationRow expandableNotificationRow, final BindStage.StageCallback stageCallback) {
        RowContentBindParams rowContentBindParams = (RowContentBindParams) getStageParams(notificationEntry);
        this.mLogger.logStageParams(notificationEntry.getKey(), rowContentBindParams.toString());
        int contentViews = rowContentBindParams.getContentViews();
        int dirtyContentViews = rowContentBindParams.getDirtyContentViews() & contentViews;
        this.mBinder.unbindContent(notificationEntry, expandableNotificationRow, contentViews ^ 15);
        NotificationRowContentBinder.BindParams bindParams = new NotificationRowContentBinder.BindParams();
        bindParams.isLowPriority = rowContentBindParams.useLowPriority();
        bindParams.usesIncreasedHeight = rowContentBindParams.useIncreasedHeight();
        bindParams.usesIncreasedHeadsUpHeight = rowContentBindParams.useIncreasedHeadsUpHeight();
        boolean needsReinflation = rowContentBindParams.needsReinflation();
        AnonymousClass1 r9 = new NotificationRowContentBinder.InflationCallback() {
            public void handleInflationException(NotificationEntry notificationEntry, Exception exc) {
                RowContentBindStage.this.mNotifInflationErrorManager.setInflationError(notificationEntry, exc);
            }

            public void onAsyncInflationFinished(NotificationEntry notificationEntry) {
                RowContentBindStage.this.mNotifInflationErrorManager.clearInflationError(notificationEntry);
                ((RowContentBindParams) RowContentBindStage.this.getStageParams(notificationEntry)).clearDirtyContentViews();
                stageCallback.onStageFinished(notificationEntry);
            }
        };
        this.mBinder.cancelBind(notificationEntry, expandableNotificationRow);
        this.mBinder.bindContent(notificationEntry, expandableNotificationRow, dirtyContentViews, bindParams, needsReinflation, r9);
    }

    public void abortStage(NotificationEntry notificationEntry, ExpandableNotificationRow expandableNotificationRow) {
        this.mBinder.cancelBind(notificationEntry, expandableNotificationRow);
    }

    public RowContentBindParams newStageParams() {
        return new RowContentBindParams();
    }
}
