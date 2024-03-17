package com.android.systemui.statusbar.policy;

import android.app.Notification;
import android.view.View;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.policy.SmartReplyView;

/* compiled from: SmartReplyStateInflater.kt */
public final class SmartActionInflaterImpl$inflateActionButton$1$onClickListener$1 implements View.OnClickListener {
    public final /* synthetic */ Notification.Action $action;
    public final /* synthetic */ int $actionIndex;
    public final /* synthetic */ NotificationEntry $entry;
    public final /* synthetic */ SmartReplyView.SmartActions $smartActions;
    public final /* synthetic */ SmartActionInflaterImpl this$0;

    public SmartActionInflaterImpl$inflateActionButton$1$onClickListener$1(SmartActionInflaterImpl smartActionInflaterImpl, NotificationEntry notificationEntry, SmartReplyView.SmartActions smartActions, int i, Notification.Action action) {
        this.this$0 = smartActionInflaterImpl;
        this.$entry = notificationEntry;
        this.$smartActions = smartActions;
        this.$actionIndex = i;
        this.$action = action;
    }

    public final void onClick(View view) {
        this.this$0.onSmartActionClick(this.$entry, this.$smartActions, this.$actionIndex, this.$action);
    }
}
