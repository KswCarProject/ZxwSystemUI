package com.android.systemui.statusbar.notification.row;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotifBindPipelineLogger.kt */
public final class NotifBindPipelineLogger$logStageSet$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotifBindPipelineLogger$logStageSet$2 INSTANCE = new NotifBindPipelineLogger$logStageSet$2();

    public NotifBindPipelineLogger$logStageSet$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return Intrinsics.stringPlus("Stage set: ", logMessage.getStr1());
    }
}
