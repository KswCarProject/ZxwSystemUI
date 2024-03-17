package com.android.systemui.statusbar;

import android.app.Notification;
import android.os.RemoteException;
import com.android.internal.statusbar.IStatusBarService;
import com.android.internal.statusbar.NotificationVisibility;
import com.android.systemui.util.Assert;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotificationClickNotifier.kt */
public final class NotificationClickNotifier {
    @NotNull
    public final IStatusBarService barService;
    @NotNull
    public final List<NotificationInteractionListener> listeners = new ArrayList();
    @NotNull
    public final Executor mainExecutor;

    public NotificationClickNotifier(@NotNull IStatusBarService iStatusBarService, @NotNull Executor executor) {
        this.barService = iStatusBarService;
        this.mainExecutor = executor;
    }

    public final void addNotificationInteractionListener(@NotNull NotificationInteractionListener notificationInteractionListener) {
        Assert.isMainThread();
        this.listeners.add(notificationInteractionListener);
    }

    public final void notifyListenersAboutInteraction(String str) {
        for (NotificationInteractionListener onNotificationInteraction : this.listeners) {
            onNotificationInteraction.onNotificationInteraction(str);
        }
    }

    public final void onNotificationActionClick(@NotNull String str, int i, @NotNull Notification.Action action, @NotNull NotificationVisibility notificationVisibility, boolean z) {
        try {
            this.barService.onNotificationActionClick(str, i, action, notificationVisibility, z);
        } catch (RemoteException unused) {
        }
        this.mainExecutor.execute(new NotificationClickNotifier$onNotificationActionClick$1(this, str));
    }

    public final void onNotificationClick(@NotNull String str, @NotNull NotificationVisibility notificationVisibility) {
        try {
            this.barService.onNotificationClick(str, notificationVisibility);
        } catch (RemoteException unused) {
        }
        this.mainExecutor.execute(new NotificationClickNotifier$onNotificationClick$1(this, str));
    }
}
