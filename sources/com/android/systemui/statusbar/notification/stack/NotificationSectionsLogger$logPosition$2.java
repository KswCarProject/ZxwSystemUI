package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotificationSectionsLogger.kt */
public final class NotificationSectionsLogger$logPosition$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationSectionsLogger$logPosition$2 INSTANCE = new NotificationSectionsLogger$logPosition$2();

    public NotificationSectionsLogger$logPosition$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return logMessage.getInt1() + ": " + logMessage.getStr1() + logMessage.getStr2();
    }
}
