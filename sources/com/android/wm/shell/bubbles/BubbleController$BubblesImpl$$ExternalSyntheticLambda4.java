package com.android.wm.shell.bubbles;

import android.app.NotificationChannel;
import android.os.UserHandle;
import com.android.wm.shell.bubbles.BubbleController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BubbleController$BubblesImpl$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ BubbleController.BubblesImpl f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ UserHandle f$2;
    public final /* synthetic */ NotificationChannel f$3;
    public final /* synthetic */ int f$4;

    public /* synthetic */ BubbleController$BubblesImpl$$ExternalSyntheticLambda4(BubbleController.BubblesImpl bubblesImpl, String str, UserHandle userHandle, NotificationChannel notificationChannel, int i) {
        this.f$0 = bubblesImpl;
        this.f$1 = str;
        this.f$2 = userHandle;
        this.f$3 = notificationChannel;
        this.f$4 = i;
    }

    public final void run() {
        this.f$0.lambda$onNotificationChannelModified$18(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
