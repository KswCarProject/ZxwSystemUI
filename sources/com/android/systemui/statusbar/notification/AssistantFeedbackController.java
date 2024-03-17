package com.android.systemui.statusbar.notification;

import android.content.Context;
import android.os.Handler;
import android.provider.DeviceConfig;
import android.service.notification.NotificationListenerService;
import android.util.SparseArray;
import com.android.systemui.R$string;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.util.DeviceConfigProxy;

public class AssistantFeedbackController {
    public final Context mContext;
    public final DeviceConfigProxy mDeviceConfigProxy;
    public volatile boolean mFeedbackEnabled;
    public final Handler mHandler;
    public final SparseArray<FeedbackIcon> mIcons;
    public final DeviceConfig.OnPropertiesChangedListener mPropertiesChangedListener;

    public AssistantFeedbackController(Handler handler, Context context, DeviceConfigProxy deviceConfigProxy) {
        AnonymousClass1 r0 = new DeviceConfig.OnPropertiesChangedListener() {
            public void onPropertiesChanged(DeviceConfig.Properties properties) {
                if (properties.getKeyset().contains("enable_nas_feedback")) {
                    AssistantFeedbackController.this.mFeedbackEnabled = properties.getBoolean("enable_nas_feedback", false);
                }
            }
        };
        this.mPropertiesChangedListener = r0;
        this.mHandler = handler;
        this.mContext = context;
        this.mDeviceConfigProxy = deviceConfigProxy;
        this.mFeedbackEnabled = deviceConfigProxy.getBoolean("systemui", "enable_nas_feedback", false);
        deviceConfigProxy.addOnPropertiesChangedListener("systemui", new AssistantFeedbackController$$ExternalSyntheticLambda0(this), r0);
        SparseArray<FeedbackIcon> sparseArray = new SparseArray<>(4);
        this.mIcons = sparseArray;
        sparseArray.set(1, new FeedbackIcon(17302448, 17040911));
        sparseArray.set(2, new FeedbackIcon(17302451, 17040914));
        sparseArray.set(3, new FeedbackIcon(17302452, 17040913));
        sparseArray.set(4, new FeedbackIcon(17302449, 17040912));
    }

    public final void postToHandler(Runnable runnable) {
        this.mHandler.post(runnable);
    }

    public boolean isFeedbackEnabled() {
        return this.mFeedbackEnabled;
    }

    public int getFeedbackStatus(NotificationEntry notificationEntry) {
        if (!isFeedbackEnabled()) {
            return 0;
        }
        NotificationListenerService.Ranking ranking = notificationEntry.getRanking();
        int importance = ranking.getChannel().getImportance();
        int importance2 = ranking.getImportance();
        if (importance < 3 && importance2 >= 3) {
            return 1;
        }
        if (importance >= 3 && importance2 < 3) {
            return 2;
        }
        if (importance < importance2 || ranking.getRankingAdjustment() == 1) {
            return 3;
        }
        if (importance > importance2 || ranking.getRankingAdjustment() == -1) {
            return 4;
        }
        return 0;
    }

    public FeedbackIcon getFeedbackIcon(NotificationEntry notificationEntry) {
        return this.mIcons.get(getFeedbackStatus(notificationEntry));
    }

    public int getInlineDescriptionResource(NotificationEntry notificationEntry) {
        int feedbackStatus = getFeedbackStatus(notificationEntry);
        if (feedbackStatus == 1) {
            return R$string.notification_channel_summary_automatic_alerted;
        }
        if (feedbackStatus == 2) {
            return R$string.notification_channel_summary_automatic_silenced;
        }
        if (feedbackStatus == 3) {
            return R$string.notification_channel_summary_automatic_promoted;
        }
        if (feedbackStatus != 4) {
            return R$string.notification_channel_summary_automatic;
        }
        return R$string.notification_channel_summary_automatic_demoted;
    }
}
