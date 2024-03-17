package com.android.systemui.media;

import android.media.session.PlaybackState;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: MediaTimeoutLogger.kt */
public final class MediaTimeoutLogger {
    @NotNull
    public final LogBuffer buffer;

    public MediaTimeoutLogger(@NotNull LogBuffer logBuffer) {
        this.buffer = logBuffer;
    }

    public final void logReuseListener(@NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("MediaTimeout", LogLevel.DEBUG, MediaTimeoutLogger$logReuseListener$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logMigrateListener(@Nullable String str, @Nullable String str2, boolean z) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("MediaTimeout", LogLevel.DEBUG, MediaTimeoutLogger$logMigrateListener$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setStr2(str2);
        obtain.setBool1(z);
        logBuffer.commit(obtain);
    }

    public final void logUpdateListener(@NotNull String str, boolean z) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("MediaTimeout", LogLevel.DEBUG, MediaTimeoutLogger$logUpdateListener$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setBool1(z);
        logBuffer.commit(obtain);
    }

    public final void logDelayedUpdate(@NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("MediaTimeout", LogLevel.DEBUG, MediaTimeoutLogger$logDelayedUpdate$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logSessionDestroyed(@NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("MediaTimeout", LogLevel.DEBUG, MediaTimeoutLogger$logSessionDestroyed$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logPlaybackState(@NotNull String str, @Nullable PlaybackState playbackState) {
        String str2;
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("MediaTimeout", LogLevel.VERBOSE, MediaTimeoutLogger$logPlaybackState$2.INSTANCE);
        obtain.setStr1(str);
        if (playbackState == null) {
            str2 = null;
        } else {
            str2 = playbackState.toString();
        }
        obtain.setStr2(str2);
        logBuffer.commit(obtain);
    }

    public final void logStateCallback(@NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("MediaTimeout", LogLevel.VERBOSE, new MediaTimeoutLogger$logStateCallback$2(str));
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logScheduleTimeout(@NotNull String str, boolean z, boolean z2) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("MediaTimeout", LogLevel.DEBUG, MediaTimeoutLogger$logScheduleTimeout$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setBool1(z);
        obtain.setBool2(z2);
        logBuffer.commit(obtain);
    }

    public final void logCancelIgnored(@NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("MediaTimeout", LogLevel.DEBUG, MediaTimeoutLogger$logCancelIgnored$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logTimeout(@NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("MediaTimeout", LogLevel.DEBUG, MediaTimeoutLogger$logTimeout$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logTimeoutCancelled(@NotNull String str, @NotNull String str2) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("MediaTimeout", LogLevel.VERBOSE, MediaTimeoutLogger$logTimeoutCancelled$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setStr2(str2);
        logBuffer.commit(obtain);
    }
}
