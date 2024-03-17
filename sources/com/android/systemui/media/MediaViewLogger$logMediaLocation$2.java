package com.android.systemui.media;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: MediaViewLogger.kt */
public final class MediaViewLogger$logMediaLocation$2 extends Lambda implements Function1<LogMessage, String> {
    public static final MediaViewLogger$logMediaLocation$2 INSTANCE = new MediaViewLogger$logMediaLocation$2();

    public MediaViewLogger$logMediaLocation$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "location (" + logMessage.getStr1() + "): " + logMessage.getInt1() + " -> " + logMessage.getInt2();
    }
}
