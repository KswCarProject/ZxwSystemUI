package com.android.systemui.statusbar.notification;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotificationEntryManagerLogger.kt */
public final class NotificationEntryManagerLogger$logNotifRemoved$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationEntryManagerLogger$logNotifRemoved$2 INSTANCE = new NotificationEntryManagerLogger$logNotifRemoved$2();

    public NotificationEntryManagerLogger$logNotifRemoved$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "NOTIF REMOVED " + logMessage.getStr1() + " removedByUser=" + logMessage.getBool1();
    }
}
