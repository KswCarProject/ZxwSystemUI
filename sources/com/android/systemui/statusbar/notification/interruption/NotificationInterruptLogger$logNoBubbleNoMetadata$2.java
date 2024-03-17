package com.android.systemui.statusbar.notification.interruption;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotificationInterruptLogger.kt */
public final class NotificationInterruptLogger$logNoBubbleNoMetadata$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationInterruptLogger$logNoBubbleNoMetadata$2 INSTANCE = new NotificationInterruptLogger$logNoBubbleNoMetadata$2();

    public NotificationInterruptLogger$logNoBubbleNoMetadata$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "No bubble up: notification: " + logMessage.getStr1() + " doesn't have valid metadata";
    }
}
