package com.android.systemui.statusbar.gesture;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import org.jetbrains.annotations.NotNull;

/* compiled from: SwipeStatusBarAwayGestureLogger.kt */
public final class SwipeStatusBarAwayGestureLogger {
    @NotNull
    public final LogBuffer buffer;

    public SwipeStatusBarAwayGestureLogger(@NotNull LogBuffer logBuffer) {
        this.buffer = logBuffer;
    }

    public final void logGestureDetectionStarted(int i) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("SwipeStatusBarAwayGestureHandler", LogLevel.DEBUG, SwipeStatusBarAwayGestureLogger$logGestureDetectionStarted$2.INSTANCE);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logGestureDetectionEndedWithoutTriggering(int i) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("SwipeStatusBarAwayGestureHandler", LogLevel.DEBUG, SwipeStatusBarAwayGestureLogger$logGestureDetectionEndedWithoutTriggering$2.INSTANCE);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logGestureDetected(int i) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("SwipeStatusBarAwayGestureHandler", LogLevel.INFO, SwipeStatusBarAwayGestureLogger$logGestureDetected$2.INSTANCE);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logInputListeningStarted() {
        LogBuffer logBuffer = this.buffer;
        logBuffer.commit(logBuffer.obtain("SwipeStatusBarAwayGestureHandler", LogLevel.VERBOSE, SwipeStatusBarAwayGestureLogger$logInputListeningStarted$2.INSTANCE));
    }

    public final void logInputListeningStopped() {
        LogBuffer logBuffer = this.buffer;
        logBuffer.commit(logBuffer.obtain("SwipeStatusBarAwayGestureHandler", LogLevel.VERBOSE, SwipeStatusBarAwayGestureLogger$logInputListeningStopped$2.INSTANCE));
    }
}
