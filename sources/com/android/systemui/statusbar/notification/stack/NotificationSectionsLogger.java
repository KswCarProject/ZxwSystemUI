package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotificationSectionsLogger.kt */
public final class NotificationSectionsLogger {
    @NotNull
    public final LogBuffer logBuffer;

    public NotificationSectionsLogger(@NotNull LogBuffer logBuffer2) {
        this.logBuffer = logBuffer2;
    }

    public final void logStartSectionUpdate(@NotNull String str) {
        LogBuffer logBuffer2 = this.logBuffer;
        LogMessageImpl obtain = logBuffer2.obtain("NotifSections", LogLevel.DEBUG, new NotificationSectionsLogger$logStartSectionUpdate$2(str));
        obtain.setStr1(str);
        logBuffer2.commit(obtain);
    }

    public final void logIncomingHeader(int i) {
        logPosition(i, "INCOMING HEADER");
    }

    public final void logMediaControls(int i) {
        logPosition(i, "MEDIA CONTROLS");
    }

    public final void logConversationsHeader(int i) {
        logPosition(i, "CONVERSATIONS HEADER");
    }

    public final void logAlertingHeader(int i) {
        logPosition(i, "ALERTING HEADER");
    }

    public final void logSilentHeader(int i) {
        logPosition(i, "SILENT HEADER");
    }

    public final void logOther(int i, @NotNull Class<?> cls) {
        LogBuffer logBuffer2 = this.logBuffer;
        LogMessageImpl obtain = logBuffer2.obtain("NotifSections", LogLevel.DEBUG, NotificationSectionsLogger$logOther$2.INSTANCE);
        obtain.setInt1(i);
        obtain.setStr1(cls.getName());
        logBuffer2.commit(obtain);
    }

    public final void logHeadsUp(int i, boolean z) {
        logPosition(i, "Heads Up", z);
    }

    public final void logConversation(int i, boolean z) {
        logPosition(i, "Conversation", z);
    }

    public final void logAlerting(int i, boolean z) {
        logPosition(i, "Alerting", z);
    }

    public final void logSilent(int i, boolean z) {
        logPosition(i, "Silent", z);
    }

    public final void logStr(@NotNull String str) {
        LogBuffer logBuffer2 = this.logBuffer;
        LogMessageImpl obtain = logBuffer2.obtain("NotifSections", LogLevel.DEBUG, NotificationSectionsLogger$logStr$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer2.commit(obtain);
    }

    public final void logPosition(int i, String str, boolean z) {
        String str2 = z ? " (HUN)" : "";
        LogBuffer logBuffer2 = this.logBuffer;
        LogMessageImpl obtain = logBuffer2.obtain("NotifSections", LogLevel.DEBUG, NotificationSectionsLogger$logPosition$2.INSTANCE);
        obtain.setInt1(i);
        obtain.setStr1(str);
        obtain.setStr2(str2);
        logBuffer2.commit(obtain);
    }

    public final void logPosition(int i, String str) {
        LogBuffer logBuffer2 = this.logBuffer;
        LogMessageImpl obtain = logBuffer2.obtain("NotifSections", LogLevel.DEBUG, NotificationSectionsLogger$logPosition$4.INSTANCE);
        obtain.setInt1(i);
        obtain.setStr1(str);
        logBuffer2.commit(obtain);
    }
}
