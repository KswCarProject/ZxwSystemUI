package com.android.systemui.statusbar.policy;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: HeadsUpManagerLogger.kt */
public final class HeadsUpManagerLogger$logUpdateNotification$2 extends Lambda implements Function1<LogMessage, String> {
    public static final HeadsUpManagerLogger$logUpdateNotification$2 INSTANCE = new HeadsUpManagerLogger$logUpdateNotification$2();

    public HeadsUpManagerLogger$logUpdateNotification$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "update notification " + logMessage.getStr1() + " alert: " + logMessage.getBool1() + " hasEntry: " + logMessage.getBool2();
    }
}
