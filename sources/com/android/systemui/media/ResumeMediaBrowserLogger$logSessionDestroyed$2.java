package com.android.systemui.media;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: ResumeMediaBrowserLogger.kt */
public final class ResumeMediaBrowserLogger$logSessionDestroyed$2 extends Lambda implements Function1<LogMessage, String> {
    public static final ResumeMediaBrowserLogger$logSessionDestroyed$2 INSTANCE = new ResumeMediaBrowserLogger$logSessionDestroyed$2();

    public ResumeMediaBrowserLogger$logSessionDestroyed$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "Session destroyed. Active browser = " + logMessage.getBool1() + ". Browser component = " + logMessage.getStr1() + '.';
    }
}
