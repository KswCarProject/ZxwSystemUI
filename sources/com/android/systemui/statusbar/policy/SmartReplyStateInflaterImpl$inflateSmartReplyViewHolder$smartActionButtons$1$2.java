package com.android.systemui.statusbar.policy;

import android.app.Notification;
import android.view.ContextThemeWrapper;
import android.widget.Button;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.policy.SmartReplyView;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: SmartReplyStateInflater.kt */
public final class SmartReplyStateInflaterImpl$inflateSmartReplyViewHolder$smartActionButtons$1$2 extends Lambda implements Function2<Integer, Notification.Action, Button> {
    public final /* synthetic */ boolean $delayOnClickListener;
    public final /* synthetic */ NotificationEntry $entry;
    public final /* synthetic */ SmartReplyView.SmartActions $smartActions;
    public final /* synthetic */ SmartReplyView $smartReplyView;
    public final /* synthetic */ ContextThemeWrapper $themedPackageContext;
    public final /* synthetic */ SmartReplyStateInflaterImpl this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public SmartReplyStateInflaterImpl$inflateSmartReplyViewHolder$smartActionButtons$1$2(SmartReplyStateInflaterImpl smartReplyStateInflaterImpl, SmartReplyView smartReplyView, NotificationEntry notificationEntry, SmartReplyView.SmartActions smartActions, boolean z, ContextThemeWrapper contextThemeWrapper) {
        super(2);
        this.this$0 = smartReplyStateInflaterImpl;
        this.$smartReplyView = smartReplyView;
        this.$entry = notificationEntry;
        this.$smartActions = smartActions;
        this.$delayOnClickListener = z;
        this.$themedPackageContext = contextThemeWrapper;
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj, Object obj2) {
        return invoke(((Number) obj).intValue(), (Notification.Action) obj2);
    }

    @NotNull
    public final Button invoke(int i, Notification.Action action) {
        return this.this$0.smartActionsInflater.inflateActionButton(this.$smartReplyView, this.$entry, this.$smartActions, i, action, this.$delayOnClickListener, this.$themedPackageContext);
    }
}
