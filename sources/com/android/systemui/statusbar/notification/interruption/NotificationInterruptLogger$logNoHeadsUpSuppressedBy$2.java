package com.android.systemui.statusbar.notification.interruption;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotificationInterruptLogger.kt */
public final class NotificationInterruptLogger$logNoHeadsUpSuppressedBy$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationInterruptLogger$logNoHeadsUpSuppressedBy$2 INSTANCE = new NotificationInterruptLogger$logNoHeadsUpSuppressedBy$2();

    public NotificationInterruptLogger$logNoHeadsUpSuppressedBy$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "No heads up: aborted by suppressor: " + logMessage.getStr2() + " sbnKey=" + logMessage.getStr1();
    }
}
