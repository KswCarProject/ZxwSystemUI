package com.android.systemui.log;

import org.jetbrains.annotations.NotNull;

/* compiled from: LogcatEchoTracker.kt */
public interface LogcatEchoTracker {
    boolean getLogInBackgroundThread();

    boolean isBufferLoggable(@NotNull String str, @NotNull LogLevel logLevel);

    boolean isTagLoggable(@NotNull String str, @NotNull LogLevel logLevel);
}
