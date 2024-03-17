package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import org.jetbrains.annotations.NotNull;

/* compiled from: StackStateLogger.kt */
public final class StackStateLogger {
    @NotNull
    public final LogBuffer buffer;

    public StackStateLogger(@NotNull LogBuffer logBuffer) {
        this.buffer = logBuffer;
    }

    public final void logHUNViewDisappearing(@NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("StackScroll", LogLevel.INFO, StackStateLogger$logHUNViewDisappearing$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logHUNViewAppearing(@NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("StackScroll", LogLevel.INFO, StackStateLogger$logHUNViewAppearing$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logHUNViewDisappearingWithRemoveEvent(@NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("StackScroll", LogLevel.ERROR, StackStateLogger$logHUNViewDisappearingWithRemoveEvent$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logHUNViewAppearingWithAddEvent(@NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("StackScroll", LogLevel.ERROR, StackStateLogger$logHUNViewAppearingWithAddEvent$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void disappearAnimationEnded(@NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("StackScroll", LogLevel.INFO, StackStateLogger$disappearAnimationEnded$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void appearAnimationEnded(@NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("StackScroll", LogLevel.INFO, StackStateLogger$appearAnimationEnded$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }
}
