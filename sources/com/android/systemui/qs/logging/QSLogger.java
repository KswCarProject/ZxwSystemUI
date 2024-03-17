package com.android.systemui.qs.logging;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.statusbar.StatusBarState;
import org.jetbrains.annotations.NotNull;

/* compiled from: QSLogger.kt */
public final class QSLogger {
    @NotNull
    public final LogBuffer buffer;

    public final String toStateString(int i) {
        return i != 0 ? i != 1 ? i != 2 ? "wrong state" : "active" : "inactive" : "unavailable";
    }

    public QSLogger(@NotNull LogBuffer logBuffer) {
        this.buffer = logBuffer;
    }

    public final void logTileAdded(@NotNull String str) {
        LogLevel logLevel = LogLevel.DEBUG;
        QSLogger$logTileAdded$2 qSLogger$logTileAdded$2 = QSLogger$logTileAdded$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("QSLog", logLevel, qSLogger$logTileAdded$2);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logTileDestroyed(@NotNull String str, @NotNull String str2) {
        LogLevel logLevel = LogLevel.DEBUG;
        QSLogger$logTileDestroyed$2 qSLogger$logTileDestroyed$2 = QSLogger$logTileDestroyed$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("QSLog", logLevel, qSLogger$logTileDestroyed$2);
        obtain.setStr1(str);
        obtain.setStr2(str2);
        logBuffer.commit(obtain);
    }

    public final void logTileChangeListening(@NotNull String str, boolean z) {
        LogLevel logLevel = LogLevel.VERBOSE;
        QSLogger$logTileChangeListening$2 qSLogger$logTileChangeListening$2 = QSLogger$logTileChangeListening$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("QSLog", logLevel, qSLogger$logTileChangeListening$2);
        obtain.setBool1(z);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logAllTilesChangeListening(boolean z, @NotNull String str, @NotNull String str2) {
        LogLevel logLevel = LogLevel.DEBUG;
        QSLogger$logAllTilesChangeListening$2 qSLogger$logAllTilesChangeListening$2 = QSLogger$logAllTilesChangeListening$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("QSLog", logLevel, qSLogger$logAllTilesChangeListening$2);
        obtain.setBool1(z);
        obtain.setStr1(str);
        obtain.setStr2(str2);
        logBuffer.commit(obtain);
    }

    public final void logTileClick(@NotNull String str, int i, int i2) {
        LogLevel logLevel = LogLevel.DEBUG;
        QSLogger$logTileClick$2 qSLogger$logTileClick$2 = QSLogger$logTileClick$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("QSLog", logLevel, qSLogger$logTileClick$2);
        obtain.setStr1(str);
        obtain.setInt1(i);
        obtain.setStr2(StatusBarState.toString(i));
        obtain.setStr3(toStateString(i2));
        logBuffer.commit(obtain);
    }

    public final void logTileSecondaryClick(@NotNull String str, int i, int i2) {
        LogLevel logLevel = LogLevel.DEBUG;
        QSLogger$logTileSecondaryClick$2 qSLogger$logTileSecondaryClick$2 = QSLogger$logTileSecondaryClick$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("QSLog", logLevel, qSLogger$logTileSecondaryClick$2);
        obtain.setStr1(str);
        obtain.setInt1(i);
        obtain.setStr2(StatusBarState.toString(i));
        obtain.setStr3(toStateString(i2));
        logBuffer.commit(obtain);
    }

    public final void logTileLongClick(@NotNull String str, int i, int i2) {
        LogLevel logLevel = LogLevel.DEBUG;
        QSLogger$logTileLongClick$2 qSLogger$logTileLongClick$2 = QSLogger$logTileLongClick$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("QSLog", logLevel, qSLogger$logTileLongClick$2);
        obtain.setStr1(str);
        obtain.setInt1(i);
        obtain.setStr2(StatusBarState.toString(i));
        obtain.setStr3(toStateString(i2));
        logBuffer.commit(obtain);
    }

    public final void logTileUpdated(@NotNull String str, @NotNull QSTile.State state) {
        LogLevel logLevel = LogLevel.VERBOSE;
        QSLogger$logTileUpdated$2 qSLogger$logTileUpdated$2 = QSLogger$logTileUpdated$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("QSLog", logLevel, qSLogger$logTileUpdated$2);
        obtain.setStr1(str);
        CharSequence charSequence = state.label;
        String str2 = null;
        obtain.setStr2(charSequence == null ? null : charSequence.toString());
        QSTile.Icon icon = state.icon;
        if (icon != null) {
            str2 = icon.toString();
        }
        obtain.setStr3(str2);
        obtain.setInt1(state.state);
        if (state instanceof QSTile.SignalState) {
            obtain.setBool1(true);
            QSTile.SignalState signalState = (QSTile.SignalState) state;
            obtain.setBool2(signalState.activityIn);
            obtain.setBool3(signalState.activityOut);
        }
        logBuffer.commit(obtain);
    }

    public final void logPanelExpanded(boolean z, @NotNull String str) {
        LogLevel logLevel = LogLevel.DEBUG;
        QSLogger$logPanelExpanded$2 qSLogger$logPanelExpanded$2 = QSLogger$logPanelExpanded$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("QSLog", logLevel, qSLogger$logPanelExpanded$2);
        obtain.setStr1(str);
        obtain.setBool1(z);
        logBuffer.commit(obtain);
    }
}
