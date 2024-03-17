package com.android.systemui.media;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: MediaTimeoutLogger.kt */
public final class MediaTimeoutLogger$logTimeout$2 extends Lambda implements Function1<LogMessage, String> {
    public static final MediaTimeoutLogger$logTimeout$2 INSTANCE = new MediaTimeoutLogger$logTimeout$2();

    public MediaTimeoutLogger$logTimeout$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return Intrinsics.stringPlus("execute timeout for ", logMessage.getStr1());
    }
}
