package com.android.systemui.statusbar.notification.collection;

import com.android.systemui.statusbar.notification.InflationException;
import com.android.systemui.statusbar.notification.collection.inflation.NotifInflater;
import com.android.systemui.statusbar.notification.collection.inflation.NotificationRowBinderImpl;
import com.android.systemui.statusbar.notification.row.NotifInflationErrorManager;
import com.android.systemui.statusbar.notification.row.NotificationRowContentBinder;

public class NotifInflaterImpl implements NotifInflater {
    public final NotifInflationErrorManager mNotifErrorManager;
    public NotificationRowBinderImpl mNotificationRowBinder;

    public NotifInflaterImpl(NotifInflationErrorManager notifInflationErrorManager) {
        this.mNotifErrorManager = notifInflationErrorManager;
    }

    public void setRowBinder(NotificationRowBinderImpl notificationRowBinderImpl) {
        this.mNotificationRowBinder = notificationRowBinderImpl;
    }

    public void rebindViews(NotificationEntry notificationEntry, NotifInflater.Params params, NotifInflater.InflationCallback inflationCallback) {
        inflateViews(notificationEntry, params, inflationCallback);
    }

    public void inflateViews(NotificationEntry notificationEntry, NotifInflater.Params params, NotifInflater.InflationCallback inflationCallback) {
        try {
            requireBinder().inflateViews(notificationEntry, params, wrapInflationCallback(inflationCallback));
        } catch (InflationException e) {
            this.mNotifErrorManager.setInflationError(notificationEntry, e);
        }
    }

    public void abortInflation(NotificationEntry notificationEntry) {
        notificationEntry.abortTask();
    }

    public final NotificationRowContentBinder.InflationCallback wrapInflationCallback(final NotifInflater.InflationCallback inflationCallback) {
        return new NotificationRowContentBinder.InflationCallback() {
            public void handleInflationException(NotificationEntry notificationEntry, Exception exc) {
                NotifInflaterImpl.this.mNotifErrorManager.setInflationError(notificationEntry, exc);
            }

            public void onAsyncInflationFinished(NotificationEntry notificationEntry) {
                NotifInflaterImpl.this.mNotifErrorManager.clearInflationError(notificationEntry);
                NotifInflater.InflationCallback inflationCallback = inflationCallback;
                if (inflationCallback != null) {
                    inflationCallback.onInflationFinished(notificationEntry, notificationEntry.getRowController());
                }
            }
        };
    }

    public final NotificationRowBinderImpl requireBinder() {
        NotificationRowBinderImpl notificationRowBinderImpl = this.mNotificationRowBinder;
        if (notificationRowBinderImpl != null) {
            return notificationRowBinderImpl;
        }
        throw new RuntimeException("NotificationRowBinder must be attached before using NotifInflaterImpl.");
    }
}
