package com.android.systemui.statusbar.phone;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: StatusBarNotificationActivityStarterLogger.kt */
public final class StatusBarNotificationActivityStarterLogger$logNonClickableNotification$2 extends Lambda implements Function1<LogMessage, String> {
    public static final StatusBarNotificationActivityStarterLogger$logNonClickableNotification$2 INSTANCE = new StatusBarNotificationActivityStarterLogger$logNonClickableNotification$2();

    public StatusBarNotificationActivityStarterLogger$logNonClickableNotification$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return Intrinsics.stringPlus("onNotificationClicked called for non-clickable notification! ", logMessage.getStr1());
    }
}
