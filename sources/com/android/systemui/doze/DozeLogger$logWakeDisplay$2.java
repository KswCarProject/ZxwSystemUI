package com.android.systemui.doze;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: DozeLogger.kt */
public final class DozeLogger$logWakeDisplay$2 extends Lambda implements Function1<LogMessage, String> {
    public static final DozeLogger$logWakeDisplay$2 INSTANCE = new DozeLogger$logWakeDisplay$2();

    public DozeLogger$logWakeDisplay$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "Display wakefulness changed, isAwake=" + logMessage.getBool1() + ", reason=" + DozeLog.reasonToString(logMessage.getInt1());
    }
}
