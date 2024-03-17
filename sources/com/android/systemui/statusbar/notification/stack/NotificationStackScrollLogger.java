package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotificationStackScrollLogger.kt */
public final class NotificationStackScrollLogger {
    @NotNull
    public final LogBuffer buffer;

    public NotificationStackScrollLogger(@NotNull LogBuffer logBuffer) {
        this.buffer = logBuffer;
    }

    public final void hunAnimationSkipped(@NotNull String str, @NotNull String str2) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotificationStackScroll", LogLevel.INFO, NotificationStackScrollLogger$hunAnimationSkipped$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setStr2(str2);
        logBuffer.commit(obtain);
    }

    public final void hunAnimationEventAdded(@NotNull String str, int i) {
        String str2;
        if (i != 0) {
            switch (i) {
                case 11:
                    str2 = "HEADS_UP_APPEAR";
                    break;
                case 12:
                    str2 = "HEADS_UP_DISAPPEAR";
                    break;
                case 13:
                    str2 = "HEADS_UP_DISAPPEAR_CLICK";
                    break;
                case 14:
                    str2 = "HEADS_UP_OTHER";
                    break;
                default:
                    str2 = String.valueOf(i);
                    break;
            }
        } else {
            str2 = "ADD";
        }
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotificationStackScroll", LogLevel.INFO, NotificationStackScrollLogger$hunAnimationEventAdded$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setStr2(str2);
        logBuffer.commit(obtain);
    }

    public final void hunSkippedForUnexpectedState(@NotNull String str, boolean z, boolean z2) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotificationStackScroll", LogLevel.INFO, NotificationStackScrollLogger$hunSkippedForUnexpectedState$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setBool1(z);
        obtain.setBool2(z2);
        logBuffer.commit(obtain);
    }
}
