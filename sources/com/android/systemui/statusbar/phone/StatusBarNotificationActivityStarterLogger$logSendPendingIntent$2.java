package com.android.systemui.statusbar.phone;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: StatusBarNotificationActivityStarterLogger.kt */
public final class StatusBarNotificationActivityStarterLogger$logSendPendingIntent$2 extends Lambda implements Function1<LogMessage, String> {
    public static final StatusBarNotificationActivityStarterLogger$logSendPendingIntent$2 INSTANCE = new StatusBarNotificationActivityStarterLogger$logSendPendingIntent$2();

    public StatusBarNotificationActivityStarterLogger$logSendPendingIntent$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "(5/5) Started intent " + logMessage.getStr2() + " for notification " + logMessage.getStr1() + " with result code " + logMessage.getInt1();
    }
}
