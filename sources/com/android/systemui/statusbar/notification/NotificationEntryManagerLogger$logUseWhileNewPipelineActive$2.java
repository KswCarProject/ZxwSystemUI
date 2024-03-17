package com.android.systemui.statusbar.notification;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotificationEntryManagerLogger.kt */
public final class NotificationEntryManagerLogger$logUseWhileNewPipelineActive$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationEntryManagerLogger$logUseWhileNewPipelineActive$2 INSTANCE = new NotificationEntryManagerLogger$logUseWhileNewPipelineActive$2();

    public NotificationEntryManagerLogger$logUseWhileNewPipelineActive$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "While running New Pipeline: " + logMessage.getStr1() + "(reason=" + logMessage.getStr2() + ')';
    }
}
