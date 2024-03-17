package com.android.systemui.media;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: MediaTimeoutLogger.kt */
public final class MediaTimeoutLogger$logTimeoutCancelled$2 extends Lambda implements Function1<LogMessage, String> {
    public static final MediaTimeoutLogger$logTimeoutCancelled$2 INSTANCE = new MediaTimeoutLogger$logTimeoutCancelled$2();

    public MediaTimeoutLogger$logTimeoutCancelled$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "media timeout cancelled for " + logMessage.getStr1() + ", reason: " + logMessage.getStr2();
    }
}
