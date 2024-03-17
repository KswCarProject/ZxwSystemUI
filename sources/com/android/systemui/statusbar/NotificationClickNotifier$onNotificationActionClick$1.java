package com.android.systemui.statusbar;

/* compiled from: NotificationClickNotifier.kt */
public final class NotificationClickNotifier$onNotificationActionClick$1 implements Runnable {
    public final /* synthetic */ String $key;
    public final /* synthetic */ NotificationClickNotifier this$0;

    public NotificationClickNotifier$onNotificationActionClick$1(NotificationClickNotifier notificationClickNotifier, String str) {
        this.this$0 = notificationClickNotifier;
        this.$key = str;
    }

    public final void run() {
        this.this$0.notifyListenersAboutInteraction(this.$key);
    }
}
