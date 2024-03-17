package com.android.systemui.statusbar.notification.row;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotifBindPipelineLogger.kt */
public final class NotifBindPipelineLogger$logFinishedPipeline$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotifBindPipelineLogger$logFinishedPipeline$2 INSTANCE = new NotifBindPipelineLogger$logFinishedPipeline$2();

    public NotifBindPipelineLogger$logFinishedPipeline$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "Finished pipeline for notif " + logMessage.getStr1() + " with " + logMessage.getInt1() + " callbacks";
    }
}
