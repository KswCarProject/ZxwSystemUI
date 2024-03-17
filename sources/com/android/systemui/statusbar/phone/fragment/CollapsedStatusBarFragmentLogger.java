package com.android.systemui.statusbar.phone.fragment;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.statusbar.DisableFlagsLogger;
import org.jetbrains.annotations.NotNull;

/* compiled from: CollapsedStatusBarFragmentLogger.kt */
public final class CollapsedStatusBarFragmentLogger {
    @NotNull
    public final LogBuffer buffer;
    @NotNull
    public final DisableFlagsLogger disableFlagsLogger;

    public CollapsedStatusBarFragmentLogger(@NotNull LogBuffer logBuffer, @NotNull DisableFlagsLogger disableFlagsLogger2) {
        this.buffer = logBuffer;
        this.disableFlagsLogger = disableFlagsLogger2;
    }

    public final void logDisableFlagChange(@NotNull DisableFlagsLogger.DisableState disableState, @NotNull DisableFlagsLogger.DisableState disableState2) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("CollapsedSbFragment", LogLevel.INFO, new CollapsedStatusBarFragmentLogger$logDisableFlagChange$2(this));
        obtain.setInt1(disableState.getDisable1());
        obtain.setInt2(disableState.getDisable2());
        obtain.setLong1((long) disableState2.getDisable1());
        obtain.setLong2((long) disableState2.getDisable2());
        logBuffer.commit(obtain);
    }
}