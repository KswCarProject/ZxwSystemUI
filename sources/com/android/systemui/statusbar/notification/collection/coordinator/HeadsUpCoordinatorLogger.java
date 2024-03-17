package com.android.systemui.statusbar.notification.collection.coordinator;

import android.util.Log;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator;
import org.jetbrains.annotations.NotNull;

/* compiled from: HeadsUpCoordinatorLogger.kt */
public final class HeadsUpCoordinatorLogger {
    @NotNull
    public final LogBuffer buffer;
    public final boolean verbose;

    public HeadsUpCoordinatorLogger(@NotNull LogBuffer logBuffer, boolean z) {
        this.buffer = logBuffer;
        this.verbose = z;
    }

    public HeadsUpCoordinatorLogger(@NotNull LogBuffer logBuffer) {
        this(logBuffer, Log.isLoggable("HeadsUpCoordinator", 2));
    }

    public final void logPostedEntryWillEvaluate(@NotNull HeadsUpCoordinator.PostedEntry postedEntry, @NotNull String str) {
        if (this.verbose) {
            LogBuffer logBuffer = this.buffer;
            LogMessageImpl obtain = logBuffer.obtain("HeadsUpCoordinator", LogLevel.VERBOSE, HeadsUpCoordinatorLogger$logPostedEntryWillEvaluate$2.INSTANCE);
            obtain.setStr1(postedEntry.getKey());
            obtain.setStr2(str);
            obtain.setBool1(postedEntry.getShouldHeadsUpEver());
            obtain.setBool2(postedEntry.getShouldHeadsUpAgain());
            logBuffer.commit(obtain);
        }
    }

    public final void logPostedEntryWillNotEvaluate(@NotNull HeadsUpCoordinator.PostedEntry postedEntry, @NotNull String str) {
        if (this.verbose) {
            LogBuffer logBuffer = this.buffer;
            LogMessageImpl obtain = logBuffer.obtain("HeadsUpCoordinator", LogLevel.VERBOSE, HeadsUpCoordinatorLogger$logPostedEntryWillNotEvaluate$2.INSTANCE);
            obtain.setStr1(postedEntry.getKey());
            obtain.setStr2(str);
            logBuffer.commit(obtain);
        }
    }

    public final void logEvaluatingGroups(int i) {
        if (this.verbose) {
            LogBuffer logBuffer = this.buffer;
            LogMessageImpl obtain = logBuffer.obtain("HeadsUpCoordinator", LogLevel.VERBOSE, HeadsUpCoordinatorLogger$logEvaluatingGroups$2.INSTANCE);
            obtain.setInt1(i);
            logBuffer.commit(obtain);
        }
    }

    public final void logEvaluatingGroup(@NotNull String str, int i, int i2) {
        if (this.verbose) {
            LogBuffer logBuffer = this.buffer;
            LogMessageImpl obtain = logBuffer.obtain("HeadsUpCoordinator", LogLevel.VERBOSE, HeadsUpCoordinatorLogger$logEvaluatingGroup$2.INSTANCE);
            obtain.setStr1(str);
            obtain.setInt1(i);
            obtain.setInt2(i2);
            logBuffer.commit(obtain);
        }
    }
}
