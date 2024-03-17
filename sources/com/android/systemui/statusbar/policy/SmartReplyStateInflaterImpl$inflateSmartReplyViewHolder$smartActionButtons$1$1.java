package com.android.systemui.statusbar.policy;

import android.app.Notification;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: SmartReplyStateInflater.kt */
public final class SmartReplyStateInflaterImpl$inflateSmartReplyViewHolder$smartActionButtons$1$1 extends Lambda implements Function1<Notification.Action, Boolean> {
    public static final SmartReplyStateInflaterImpl$inflateSmartReplyViewHolder$smartActionButtons$1$1 INSTANCE = new SmartReplyStateInflaterImpl$inflateSmartReplyViewHolder$smartActionButtons$1$1();

    public SmartReplyStateInflaterImpl$inflateSmartReplyViewHolder$smartActionButtons$1$1() {
        super(1);
    }

    @NotNull
    public final Boolean invoke(Notification.Action action) {
        return Boolean.valueOf(action.actionIntent != null);
    }
}
