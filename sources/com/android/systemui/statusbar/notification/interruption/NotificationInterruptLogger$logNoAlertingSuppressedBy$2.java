package com.android.systemui.statusbar.notification.interruption;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotificationInterruptLogger.kt */
public final class NotificationInterruptLogger$logNoAlertingSuppressedBy$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationInterruptLogger$logNoAlertingSuppressedBy$2 INSTANCE = new NotificationInterruptLogger$logNoAlertingSuppressedBy$2();

    public NotificationInterruptLogger$logNoAlertingSuppressedBy$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "No alerting: aborted by suppressor: " + logMessage.getStr2() + " awake=" + logMessage.getBool1() + " sbnKey=" + logMessage.getStr1();
    }
}
