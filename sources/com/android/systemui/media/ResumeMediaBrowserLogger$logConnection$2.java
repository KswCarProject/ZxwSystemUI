package com.android.systemui.media;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: ResumeMediaBrowserLogger.kt */
public final class ResumeMediaBrowserLogger$logConnection$2 extends Lambda implements Function1<LogMessage, String> {
    public static final ResumeMediaBrowserLogger$logConnection$2 INSTANCE = new ResumeMediaBrowserLogger$logConnection$2();

    public ResumeMediaBrowserLogger$logConnection$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "Connecting browser for component " + logMessage.getStr1() + " due to " + logMessage.getStr2();
    }
}
