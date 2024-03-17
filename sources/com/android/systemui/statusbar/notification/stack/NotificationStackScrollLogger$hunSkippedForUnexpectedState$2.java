package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotificationStackScrollLogger.kt */
public final class NotificationStackScrollLogger$hunSkippedForUnexpectedState$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationStackScrollLogger$hunSkippedForUnexpectedState$2 INSTANCE = new NotificationStackScrollLogger$hunSkippedForUnexpectedState$2();

    public NotificationStackScrollLogger$hunSkippedForUnexpectedState$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "HUN animation skipped for unexpected hun state: key: " + logMessage.getStr1() + " expected: " + logMessage.getBool1() + " actual: " + logMessage.getBool2();
    }
}
