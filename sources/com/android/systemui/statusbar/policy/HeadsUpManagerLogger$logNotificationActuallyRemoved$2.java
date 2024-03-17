package com.android.systemui.statusbar.policy;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: HeadsUpManagerLogger.kt */
public final class HeadsUpManagerLogger$logNotificationActuallyRemoved$2 extends Lambda implements Function1<LogMessage, String> {
    public static final HeadsUpManagerLogger$logNotificationActuallyRemoved$2 INSTANCE = new HeadsUpManagerLogger$logNotificationActuallyRemoved$2();

    public HeadsUpManagerLogger$logNotificationActuallyRemoved$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "notification removed " + logMessage.getStr1() + ' ';
    }
}
