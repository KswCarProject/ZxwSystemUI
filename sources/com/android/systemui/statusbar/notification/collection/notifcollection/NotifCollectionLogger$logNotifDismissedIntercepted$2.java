package com.android.systemui.statusbar.notification.collection.notifcollection;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotifCollectionLogger.kt */
public final class NotifCollectionLogger$logNotifDismissedIntercepted$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotifCollectionLogger$logNotifDismissedIntercepted$2 INSTANCE = new NotifCollectionLogger$logNotifDismissedIntercepted$2();

    public NotifCollectionLogger$logNotifDismissedIntercepted$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return Intrinsics.stringPlus("DISMISS INTERCEPTED ", logMessage.getStr1());
    }
}
