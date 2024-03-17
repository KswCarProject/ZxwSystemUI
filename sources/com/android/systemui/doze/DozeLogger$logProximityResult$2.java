package com.android.systemui.doze;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: DozeLogger.kt */
public final class DozeLogger$logProximityResult$2 extends Lambda implements Function1<LogMessage, String> {
    public static final DozeLogger$logProximityResult$2 INSTANCE = new DozeLogger$logProximityResult$2();

    public DozeLogger$logProximityResult$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "Proximity result reason=" + DozeLog.reasonToString(logMessage.getInt1()) + " near=" + logMessage.getBool1() + " millis=" + logMessage.getLong1();
    }
}
