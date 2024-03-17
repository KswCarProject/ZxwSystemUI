package com.android.systemui.media;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: MediaTimeoutLogger.kt */
public final class MediaTimeoutLogger$logSessionDestroyed$2 extends Lambda implements Function1<LogMessage, String> {
    public static final MediaTimeoutLogger$logSessionDestroyed$2 INSTANCE = new MediaTimeoutLogger$logSessionDestroyed$2();

    public MediaTimeoutLogger$logSessionDestroyed$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return Intrinsics.stringPlus("session destroyed ", logMessage.getStr1());
    }
}
