package com.android.systemui.statusbar.phone;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: StatusBarNotificationActivityStarterLogger.kt */
public final class StatusBarNotificationActivityStarterLogger$logSendingFullScreenIntent$2 extends Lambda implements Function1<LogMessage, String> {
    public static final StatusBarNotificationActivityStarterLogger$logSendingFullScreenIntent$2 INSTANCE = new StatusBarNotificationActivityStarterLogger$logSendingFullScreenIntent$2();

    public StatusBarNotificationActivityStarterLogger$logSendingFullScreenIntent$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "Notification " + logMessage.getStr1() + " has fullScreenIntent; sending fullScreenIntent " + logMessage.getStr2();
    }
}
