package com.android.systemui.statusbar.notification;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotificationClickerLogger.kt */
public final class NotificationClickerLogger$logGutsExposed$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationClickerLogger$logGutsExposed$2 INSTANCE = new NotificationClickerLogger$logGutsExposed$2();

    public NotificationClickerLogger$logGutsExposed$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "Ignoring click on " + logMessage.getStr1() + "; guts are exposed";
    }
}
