package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotificationStackScrollLogger.kt */
public final class NotificationStackScrollLogger$hunAnimationSkipped$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationStackScrollLogger$hunAnimationSkipped$2 INSTANCE = new NotificationStackScrollLogger$hunAnimationSkipped$2();

    public NotificationStackScrollLogger$hunAnimationSkipped$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "heads up animation skipped: key: " + logMessage.getStr1() + " reason: " + logMessage.getStr2();
    }
}
