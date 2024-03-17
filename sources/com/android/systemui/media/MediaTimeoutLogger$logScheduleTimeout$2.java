package com.android.systemui.media;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: MediaTimeoutLogger.kt */
public final class MediaTimeoutLogger$logScheduleTimeout$2 extends Lambda implements Function1<LogMessage, String> {
    public static final MediaTimeoutLogger$logScheduleTimeout$2 INSTANCE = new MediaTimeoutLogger$logScheduleTimeout$2();

    public MediaTimeoutLogger$logScheduleTimeout$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "schedule timeout " + logMessage.getStr1() + ", playing=" + logMessage.getBool1() + " resumption=" + logMessage.getBool2();
    }
}
