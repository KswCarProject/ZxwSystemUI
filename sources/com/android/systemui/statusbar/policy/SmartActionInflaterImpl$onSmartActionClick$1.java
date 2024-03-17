package com.android.systemui.statusbar.policy;

import android.app.Notification;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.policy.SmartReplyView;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

/* compiled from: SmartReplyStateInflater.kt */
public final class SmartActionInflaterImpl$onSmartActionClick$1 extends Lambda implements Function0<Unit> {
    public final /* synthetic */ Notification.Action $action;
    public final /* synthetic */ int $actionIndex;
    public final /* synthetic */ NotificationEntry $entry;
    public final /* synthetic */ SmartReplyView.SmartActions $smartActions;
    public final /* synthetic */ SmartActionInflaterImpl this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public SmartActionInflaterImpl$onSmartActionClick$1(SmartActionInflaterImpl smartActionInflaterImpl, NotificationEntry notificationEntry, int i, Notification.Action action, SmartReplyView.SmartActions smartActions) {
        super(0);
        this.this$0 = smartActionInflaterImpl;
        this.$entry = notificationEntry;
        this.$actionIndex = i;
        this.$action = action;
        this.$smartActions = smartActions;
    }

    public final void invoke() {
        this.this$0.smartReplyController.smartActionClicked(this.$entry, this.$actionIndex, this.$action, this.$smartActions.fromAssistant);
    }
}
