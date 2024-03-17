package com.android.systemui.log;

import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: LogMessageImpl.kt */
public final class LogMessageImplKt$DEFAULT_RENDERER$1 extends Lambda implements Function1<LogMessage, String> {
    public static final LogMessageImplKt$DEFAULT_RENDERER$1 INSTANCE = new LogMessageImplKt$DEFAULT_RENDERER$1();

    public LogMessageImplKt$DEFAULT_RENDERER$1() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return Intrinsics.stringPlus("Unknown message: ", logMessage);
    }
}
