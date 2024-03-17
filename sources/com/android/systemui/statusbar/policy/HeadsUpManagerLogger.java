package com.android.systemui.statusbar.policy;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import org.jetbrains.annotations.NotNull;

/* compiled from: HeadsUpManagerLogger.kt */
public final class HeadsUpManagerLogger {
    @NotNull
    public final LogBuffer buffer;

    public HeadsUpManagerLogger(@NotNull LogBuffer logBuffer) {
        this.buffer = logBuffer;
    }

    public final void logPackageSnoozed(@NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("HeadsUpManager", LogLevel.INFO, HeadsUpManagerLogger$logPackageSnoozed$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logPackageUnsnoozed(@NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("HeadsUpManager", LogLevel.INFO, HeadsUpManagerLogger$logPackageUnsnoozed$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logIsSnoozedReturned(@NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("HeadsUpManager", LogLevel.INFO, HeadsUpManagerLogger$logIsSnoozedReturned$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logReleaseAllImmediately() {
        LogBuffer logBuffer = this.buffer;
        logBuffer.commit(logBuffer.obtain("HeadsUpManager", LogLevel.INFO, HeadsUpManagerLogger$logReleaseAllImmediately$2.INSTANCE));
    }

    public final void logShowNotification(@NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("HeadsUpManager", LogLevel.INFO, HeadsUpManagerLogger$logShowNotification$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logRemoveNotification(@NotNull String str, boolean z) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("HeadsUpManager", LogLevel.INFO, HeadsUpManagerLogger$logRemoveNotification$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setBool1(z);
        logBuffer.commit(obtain);
    }

    public final void logNotificationActuallyRemoved(@NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("HeadsUpManager", LogLevel.INFO, HeadsUpManagerLogger$logNotificationActuallyRemoved$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logUpdateNotification(@NotNull String str, boolean z, boolean z2) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("HeadsUpManager", LogLevel.INFO, HeadsUpManagerLogger$logUpdateNotification$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setBool1(z);
        obtain.setBool2(z2);
        logBuffer.commit(obtain);
    }

    public final void logUpdateEntry(@NotNull String str, boolean z) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("HeadsUpManager", LogLevel.INFO, new HeadsUpManagerLogger$logUpdateEntry$2(str));
        obtain.setStr1(str);
        obtain.setBool1(z);
        logBuffer.commit(obtain);
    }

    public final void logSnoozeLengthChange(int i) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("HeadsUpManager", LogLevel.INFO, HeadsUpManagerLogger$logSnoozeLengthChange$2.INSTANCE);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logSetEntryPinned(@NotNull String str, boolean z) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("HeadsUpManager", LogLevel.VERBOSE, HeadsUpManagerLogger$logSetEntryPinned$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setBool1(z);
        logBuffer.commit(obtain);
    }

    public final void logUpdatePinnedMode(boolean z) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("HeadsUpManager", LogLevel.INFO, HeadsUpManagerLogger$logUpdatePinnedMode$2.INSTANCE);
        obtain.setBool1(z);
        logBuffer.commit(obtain);
    }
}
