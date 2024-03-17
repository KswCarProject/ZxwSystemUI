package com.android.systemui.statusbar.notification.interruption;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotificationInterruptLogger.kt */
public final class NotificationInterruptLogger$logHeadsUp$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationInterruptLogger$logHeadsUp$2 INSTANCE = new NotificationInterruptLogger$logHeadsUp$2();

    public NotificationInterruptLogger$logHeadsUp$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return Intrinsics.stringPlus("Heads up: ", logMessage.getStr1());
    }
}
