package com.android.systemui.statusbar.notification.interruption;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import org.jetbrains.annotations.NotNull;

/* compiled from: HeadsUpViewBinderLogger.kt */
public final class HeadsUpViewBinderLogger {
    @NotNull
    public final LogBuffer buffer;

    public HeadsUpViewBinderLogger(@NotNull LogBuffer logBuffer) {
        this.buffer = logBuffer;
    }

    public final void startBindingHun(@NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("HeadsUpViewBinder", LogLevel.INFO, HeadsUpViewBinderLogger$startBindingHun$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void currentOngoingBindingAborted(@NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("HeadsUpViewBinder", LogLevel.INFO, HeadsUpViewBinderLogger$currentOngoingBindingAborted$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void entryBoundSuccessfully(@NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("HeadsUpViewBinder", LogLevel.INFO, HeadsUpViewBinderLogger$entryBoundSuccessfully$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void entryUnbound(@NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("HeadsUpViewBinder", LogLevel.INFO, HeadsUpViewBinderLogger$entryUnbound$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void entryContentViewMarkedFreeable(@NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("HeadsUpViewBinder", LogLevel.INFO, HeadsUpViewBinderLogger$entryContentViewMarkedFreeable$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }
}
