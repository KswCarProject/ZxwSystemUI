package com.android.systemui.media;

import android.app.Notification;
import com.android.systemui.plugins.ActivityStarter;

/* compiled from: MediaDataManager.kt */
public final class MediaDataManager$createActionsFromNotification$runnable$1 implements Runnable {
    public final /* synthetic */ Notification.Action $action;
    public final /* synthetic */ MediaDataManager this$0;

    public MediaDataManager$createActionsFromNotification$runnable$1(Notification.Action action, MediaDataManager mediaDataManager) {
        this.$action = action;
        this.this$0 = mediaDataManager;
    }

    public final void run() {
        if (this.$action.actionIntent.isActivity()) {
            this.this$0.activityStarter.startPendingIntentDismissingKeyguard(this.$action.actionIntent);
        } else if (this.$action.isAuthenticationRequired()) {
            ActivityStarter access$getActivityStarter$p = this.this$0.activityStarter;
            final MediaDataManager mediaDataManager = this.this$0;
            final Notification.Action action = this.$action;
            access$getActivityStarter$p.dismissKeyguardThenExecute(new ActivityStarter.OnDismissAction() {
                public final boolean onDismiss() {
                    return mediaDataManager.sendPendingIntent(action.actionIntent);
                }
            }, AnonymousClass2.INSTANCE, true);
        } else {
            boolean unused = this.this$0.sendPendingIntent(this.$action.actionIntent);
        }
    }
}
