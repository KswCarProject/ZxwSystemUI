package com.android.systemui.statusbar.notification.collection.render;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ShadeViewDifferLogger.kt */
public final class ShadeViewDifferLogger {
    @NotNull
    public final LogBuffer buffer;

    public ShadeViewDifferLogger(@NotNull LogBuffer logBuffer) {
        this.buffer = logBuffer;
    }

    public final void logDetachingChild(@NotNull String str, boolean z, boolean z2, @Nullable String str2, @Nullable String str3) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotifViewManager", LogLevel.DEBUG, ShadeViewDifferLogger$logDetachingChild$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setBool1(z);
        obtain.setBool2(z2);
        obtain.setStr2(str2);
        obtain.setStr3(str3);
        logBuffer.commit(obtain);
    }

    public final void logAttachingChild(@NotNull String str, @NotNull String str2) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotifViewManager", LogLevel.DEBUG, ShadeViewDifferLogger$logAttachingChild$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setStr2(str2);
        logBuffer.commit(obtain);
    }

    public final void logMovingChild(@NotNull String str, @NotNull String str2, int i) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotifViewManager", LogLevel.DEBUG, ShadeViewDifferLogger$logMovingChild$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setStr2(str2);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logDuplicateNodeInTree(@NotNull NodeSpec nodeSpec, @NotNull RuntimeException runtimeException) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotifViewManager", LogLevel.ERROR, ShadeViewDifferLogger$logDuplicateNodeInTree$2.INSTANCE);
        obtain.setStr1(runtimeException.toString());
        obtain.setStr2(NodeControllerKt.treeSpecToStr(nodeSpec));
        logBuffer.commit(obtain);
    }
}
