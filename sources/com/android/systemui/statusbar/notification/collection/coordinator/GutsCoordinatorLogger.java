package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.statusbar.notification.row.NotificationGuts;
import kotlin.jvm.internal.Reflection;
import org.jetbrains.annotations.NotNull;

/* compiled from: GutsCoordinatorLogger.kt */
public final class GutsCoordinatorLogger {
    @NotNull
    public final LogBuffer buffer;

    public GutsCoordinatorLogger(@NotNull LogBuffer logBuffer) {
        this.buffer = logBuffer;
    }

    public final void logGutsOpened(@NotNull String str, @NotNull NotificationGuts notificationGuts) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("GutsCoordinator", LogLevel.DEBUG, GutsCoordinatorLogger$logGutsOpened$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setStr2(Reflection.getOrCreateKotlinClass(notificationGuts.getGutsContent().getClass()).getSimpleName());
        obtain.setBool1(notificationGuts.isLeavebehind());
        logBuffer.commit(obtain);
    }

    public final void logGutsClosed(@NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("GutsCoordinator", LogLevel.DEBUG, GutsCoordinatorLogger$logGutsClosed$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }
}
