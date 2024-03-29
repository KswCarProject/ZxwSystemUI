package com.android.systemui.statusbar.notification.row;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotifBindPipelineLogger.kt */
public final class NotifBindPipelineLogger$logRequestPipelineRowNotSet$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotifBindPipelineLogger$logRequestPipelineRowNotSet$2 INSTANCE = new NotifBindPipelineLogger$logRequestPipelineRowNotSet$2();

    public NotifBindPipelineLogger$logRequestPipelineRowNotSet$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return Intrinsics.stringPlus("Row is not set so pipeline will not run. notif = ", logMessage.getStr1());
    }
}
