package com.android.systemui.statusbar.notification.collection.notifcollection;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotifCollectionLogger.kt */
public final class NotifCollectionLogger$logNotifRemoved$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotifCollectionLogger$logNotifRemoved$2 INSTANCE = new NotifCollectionLogger$logNotifRemoved$2();

    public NotifCollectionLogger$logNotifRemoved$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "REMOVED " + logMessage.getStr1() + " reason=" + NotifCollectionLoggerKt.cancellationReasonDebugString(logMessage.getInt1());
    }
}
