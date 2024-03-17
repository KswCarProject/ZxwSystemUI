package com.android.systemui.log;

import org.jetbrains.annotations.NotNull;

/* compiled from: LogcatEchoTrackerProd.kt */
public final class LogcatEchoTrackerProd implements LogcatEchoTracker {
    public final boolean logInBackgroundThread;

    public boolean getLogInBackgroundThread() {
        return this.logInBackgroundThread;
    }

    public boolean isBufferLoggable(@NotNull String str, @NotNull LogLevel logLevel) {
        return logLevel.compareTo(LogLevel.WARNING) >= 0;
    }

    public boolean isTagLoggable(@NotNull String str, @NotNull LogLevel logLevel) {
        return logLevel.compareTo(LogLevel.WARNING) >= 0;
    }
}
