package com.android.systemui.statusbar.notification;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotificationEntryManagerLogger.kt */
public final class NotificationEntryManagerLogger$logRemovalIntercepted$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationEntryManagerLogger$logRemovalIntercepted$2 INSTANCE = new NotificationEntryManagerLogger$logRemovalIntercepted$2();

    public NotificationEntryManagerLogger$logRemovalIntercepted$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return Intrinsics.stringPlus("NOTIF REMOVE INTERCEPTED for ", logMessage.getStr1());
    }
}
