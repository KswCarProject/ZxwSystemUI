package com.android.systemui.media;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: MediaTimeoutLogger.kt */
public final class MediaTimeoutLogger$logPlaybackState$2 extends Lambda implements Function1<LogMessage, String> {
    public static final MediaTimeoutLogger$logPlaybackState$2 INSTANCE = new MediaTimeoutLogger$logPlaybackState$2();

    public MediaTimeoutLogger$logPlaybackState$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "state update: key=" + logMessage.getStr1() + " state=" + logMessage.getStr2();
    }
}
