package com.android.systemui.media;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: MediaTimeoutLogger.kt */
public final class MediaTimeoutLogger$logCancelIgnored$2 extends Lambda implements Function1<LogMessage, String> {
    public static final MediaTimeoutLogger$logCancelIgnored$2 INSTANCE = new MediaTimeoutLogger$logCancelIgnored$2();

    public MediaTimeoutLogger$logCancelIgnored$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return Intrinsics.stringPlus("cancellation already exists for ", logMessage.getStr1());
    }
}
