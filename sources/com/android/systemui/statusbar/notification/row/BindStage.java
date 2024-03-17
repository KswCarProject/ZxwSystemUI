package com.android.systemui.statusbar.notification.row;

import android.util.ArrayMap;
import android.util.Log;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.Map;

public abstract class BindStage<Params> extends BindRequester {
    public Map<NotificationEntry, Params> mContentParams = new ArrayMap();

    public interface StageCallback {
        void onStageFinished(NotificationEntry notificationEntry);
    }

    public abstract void abortStage(NotificationEntry notificationEntry, ExpandableNotificationRow expandableNotificationRow);

    public abstract void executeStage(NotificationEntry notificationEntry, ExpandableNotificationRow expandableNotificationRow, StageCallback stageCallback);

    public abstract Params newStageParams();

    public final Params getStageParams(NotificationEntry notificationEntry) {
        Params params = this.mContentParams.get(notificationEntry);
        if (params != null) {
            return params;
        }
        Log.wtf("BindStage", String.format("Entry does not have any stage parameters. key: %s", new Object[]{notificationEntry.getKey()}));
        return newStageParams();
    }

    public final void createStageParams(NotificationEntry notificationEntry) {
        this.mContentParams.put(notificationEntry, newStageParams());
    }

    public final void deleteStageParams(NotificationEntry notificationEntry) {
        this.mContentParams.remove(notificationEntry);
    }
}
