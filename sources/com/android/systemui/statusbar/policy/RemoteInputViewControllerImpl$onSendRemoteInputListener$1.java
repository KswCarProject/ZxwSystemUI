package com.android.systemui.statusbar.policy;

import android.app.PendingIntent;
import android.app.RemoteInput;
import android.util.Log;

/* compiled from: RemoteInputViewController.kt */
public final class RemoteInputViewControllerImpl$onSendRemoteInputListener$1 implements Runnable {
    public final /* synthetic */ RemoteInputViewControllerImpl this$0;

    public RemoteInputViewControllerImpl$onSendRemoteInputListener$1(RemoteInputViewControllerImpl remoteInputViewControllerImpl) {
        this.this$0 = remoteInputViewControllerImpl;
    }

    public final void run() {
        RemoteInput remoteInput = this.this$0.getRemoteInput();
        if (remoteInput == null) {
            Log.e("RemoteInput", "cannot send remote input, RemoteInput data is null");
            return;
        }
        PendingIntent pendingIntent = this.this$0.getPendingIntent();
        if (pendingIntent == null) {
            Log.e("RemoteInput", "cannot send remote input, PendingIntent is null");
            return;
        }
        this.this$0.sendRemoteInput(pendingIntent, this.this$0.prepareRemoteInput(remoteInput));
    }
}
