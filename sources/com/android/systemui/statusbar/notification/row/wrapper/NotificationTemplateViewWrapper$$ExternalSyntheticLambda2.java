package com.android.systemui.statusbar.notification.row.wrapper;

import android.app.PendingIntent;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class NotificationTemplateViewWrapper$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ PendingIntent f$0;
    public final /* synthetic */ PendingIntent.CancelListener f$1;

    public /* synthetic */ NotificationTemplateViewWrapper$$ExternalSyntheticLambda2(PendingIntent pendingIntent, PendingIntent.CancelListener cancelListener) {
        this.f$0 = pendingIntent;
        this.f$1 = cancelListener;
    }

    public final void run() {
        this.f$0.registerCancelListener(this.f$1);
    }
}
