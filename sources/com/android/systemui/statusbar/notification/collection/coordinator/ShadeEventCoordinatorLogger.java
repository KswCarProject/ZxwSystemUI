package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import org.jetbrains.annotations.NotNull;

/* compiled from: ShadeEventCoordinatorLogger.kt */
public final class ShadeEventCoordinatorLogger {
    @NotNull
    public final LogBuffer buffer;

    public ShadeEventCoordinatorLogger(@NotNull LogBuffer logBuffer) {
        this.buffer = logBuffer;
    }

    public final void logShadeEmptied() {
        LogBuffer logBuffer = this.buffer;
        logBuffer.commit(logBuffer.obtain("ShadeEventCoordinator", LogLevel.DEBUG, ShadeEventCoordinatorLogger$logShadeEmptied$2.INSTANCE));
    }

    public final void logNotifRemovedByUser() {
        LogBuffer logBuffer = this.buffer;
        logBuffer.commit(logBuffer.obtain("ShadeEventCoordinator", LogLevel.DEBUG, ShadeEventCoordinatorLogger$logNotifRemovedByUser$2.INSTANCE));
    }
}
