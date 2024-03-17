package com.android.systemui.log;

import java.text.SimpleDateFormat;
import java.util.Locale;
import org.jetbrains.annotations.NotNull;

/* compiled from: LogBuffer.kt */
public final class LogBufferKt {
    @NotNull
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.US);
    @NotNull
    public static final LogMessageImpl FROZEN_MESSAGE = LogMessageImpl.Factory.create();
}
