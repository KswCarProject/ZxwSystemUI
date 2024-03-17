package com.android.systemui.statusbar.notification.row;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotifBindPipelineLogger.kt */
public final class NotifBindPipelineLogger$logRequestPipelineRun$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotifBindPipelineLogger$logRequestPipelineRun$2 INSTANCE = new NotifBindPipelineLogger$logRequestPipelineRun$2();

    public NotifBindPipelineLogger$logRequestPipelineRun$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return Intrinsics.stringPlus("Request pipeline run for notif: ", logMessage.getStr1());
    }
}
