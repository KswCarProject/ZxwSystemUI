package com.android.systemui.media.taptotransfer.common;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: MediaTttLogger.kt */
public final class MediaTttLogger$logStateChange$2 extends Lambda implements Function1<LogMessage, String> {
    public static final MediaTttLogger$logStateChange$2 INSTANCE = new MediaTttLogger$logStateChange$2();

    public MediaTttLogger$logStateChange$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "State changed to " + logMessage.getStr1() + " for ID=" + logMessage.getStr2();
    }
}
