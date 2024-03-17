package com.android.systemui.media;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: MediaTimeoutLogger.kt */
public final class MediaTimeoutLogger$logMigrateListener$2 extends Lambda implements Function1<LogMessage, String> {
    public static final MediaTimeoutLogger$logMigrateListener$2 INSTANCE = new MediaTimeoutLogger$logMigrateListener$2();

    public MediaTimeoutLogger$logMigrateListener$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "migrate from " + logMessage.getStr1() + " to " + logMessage.getStr2() + ", had listener? " + logMessage.getBool1();
    }
}
