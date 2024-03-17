package com.android.systemui.dreams;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.policy.CallbackController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;

public class DreamOverlayNotificationCountProvider implements CallbackController<Callback> {
    public final List<Callback> mCallbacks = new ArrayList();
    public final NotificationListener.NotificationHandler mNotificationHandler;
    public final Set<String> mNotificationKeys = new HashSet();

    public interface Callback {
        void onNotificationCountChanged(int i);
    }

    public DreamOverlayNotificationCountProvider(NotificationListener notificationListener, Executor executor) {
        AnonymousClass1 r0 = new NotificationListener.NotificationHandler() {
            public void onNotificationRankingUpdate(NotificationListenerService.RankingMap rankingMap) {
            }

            public void onNotificationsInitialized() {
            }

            public void onNotificationPosted(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap) {
                DreamOverlayNotificationCountProvider.this.mNotificationKeys.add(statusBarNotification.getKey());
                DreamOverlayNotificationCountProvider.this.reportNotificationCountChanged();
            }

            public void onNotificationRemoved(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap, int i) {
                DreamOverlayNotificationCountProvider.this.mNotificationKeys.remove(statusBarNotification.getKey());
                DreamOverlayNotificationCountProvider.this.reportNotificationCountChanged();
            }
        };
        this.mNotificationHandler = r0;
        notificationListener.addNotificationHandler(r0);
        executor.execute(new DreamOverlayNotificationCountProvider$$ExternalSyntheticLambda0(this, notificationListener));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(NotificationListener notificationListener) {
        Arrays.stream(notificationListener.getActiveNotifications()).forEach(new DreamOverlayNotificationCountProvider$$ExternalSyntheticLambda1(this));
        reportNotificationCountChanged();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(StatusBarNotification statusBarNotification) {
        this.mNotificationKeys.add(statusBarNotification.getKey());
    }

    public void addCallback(Callback callback) {
        if (!this.mCallbacks.contains(callback)) {
            this.mCallbacks.add(callback);
            callback.onNotificationCountChanged(this.mNotificationKeys.size());
        }
    }

    public void removeCallback(Callback callback) {
        this.mCallbacks.remove(callback);
    }

    public final void reportNotificationCountChanged() {
        this.mCallbacks.forEach(new DreamOverlayNotificationCountProvider$$ExternalSyntheticLambda2(this.mNotificationKeys.size()));
    }
}
