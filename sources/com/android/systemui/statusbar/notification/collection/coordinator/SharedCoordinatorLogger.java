package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import org.jetbrains.annotations.NotNull;

/* compiled from: SharedCoordinatorLogger.kt */
public final class SharedCoordinatorLogger {
    @NotNull
    public final LogBuffer buffer;

    public SharedCoordinatorLogger(@NotNull LogBuffer logBuffer) {
        this.buffer = logBuffer;
    }

    public final void logUserOrProfileChanged(int i, @NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotCurrentUserFilter", LogLevel.INFO, SharedCoordinatorLogger$logUserOrProfileChanged$2.INSTANCE);
        obtain.setInt1(i);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logKeyguardCoordinatorInvalidated(@NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("KeyguardCoordinator", LogLevel.DEBUG, SharedCoordinatorLogger$logKeyguardCoordinatorInvalidated$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }
}
