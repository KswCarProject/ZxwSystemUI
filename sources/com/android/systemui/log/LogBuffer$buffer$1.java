package com.android.systemui.log;

import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: LogBuffer.kt */
public final class LogBuffer$buffer$1 extends Lambda implements Function0<LogMessageImpl> {
    public static final LogBuffer$buffer$1 INSTANCE = new LogBuffer$buffer$1();

    public LogBuffer$buffer$1() {
        super(0);
    }

    @NotNull
    public final LogMessageImpl invoke() {
        return LogMessageImpl.Factory.create();
    }
}
