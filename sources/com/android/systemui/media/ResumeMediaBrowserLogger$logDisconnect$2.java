package com.android.systemui.media;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: ResumeMediaBrowserLogger.kt */
public final class ResumeMediaBrowserLogger$logDisconnect$2 extends Lambda implements Function1<LogMessage, String> {
    public static final ResumeMediaBrowserLogger$logDisconnect$2 INSTANCE = new ResumeMediaBrowserLogger$logDisconnect$2();

    public ResumeMediaBrowserLogger$logDisconnect$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return Intrinsics.stringPlus("Disconnecting browser for component ", logMessage.getStr1());
    }
}
