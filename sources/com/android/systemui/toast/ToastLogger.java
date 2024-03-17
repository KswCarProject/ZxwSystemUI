package com.android.systemui.toast;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import org.jetbrains.annotations.NotNull;

/* compiled from: ToastLogger.kt */
public final class ToastLogger {
    @NotNull
    public final LogBuffer buffer;

    public ToastLogger(@NotNull LogBuffer logBuffer) {
        this.buffer = logBuffer;
    }

    public final void logOnShowToast(int i, @NotNull String str, @NotNull String str2, @NotNull String str3) {
        LogLevel logLevel = LogLevel.DEBUG;
        ToastLogger$logOnShowToast$2 toastLogger$logOnShowToast$2 = ToastLogger$logOnShowToast$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("ToastLog", logLevel, toastLogger$logOnShowToast$2);
        obtain.setInt1(i);
        obtain.setStr1(str);
        obtain.setStr2(str2);
        obtain.setStr3(str3);
        logBuffer.commit(obtain);
    }

    public final void logOnHideToast(@NotNull String str, @NotNull String str2) {
        LogLevel logLevel = LogLevel.DEBUG;
        ToastLogger$logOnHideToast$2 toastLogger$logOnHideToast$2 = ToastLogger$logOnHideToast$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("ToastLog", logLevel, toastLogger$logOnHideToast$2);
        obtain.setStr1(str);
        obtain.setStr2(str2);
        logBuffer.commit(obtain);
    }

    public final void logOrientationChange(@NotNull String str, boolean z) {
        LogLevel logLevel = LogLevel.DEBUG;
        ToastLogger$logOrientationChange$2 toastLogger$logOrientationChange$2 = ToastLogger$logOrientationChange$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("ToastLog", logLevel, toastLogger$logOrientationChange$2);
        obtain.setStr1(str);
        obtain.setBool1(z);
        logBuffer.commit(obtain);
    }
}
