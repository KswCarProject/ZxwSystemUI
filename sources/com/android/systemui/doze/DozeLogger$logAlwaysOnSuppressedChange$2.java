package com.android.systemui.doze;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: DozeLogger.kt */
public final class DozeLogger$logAlwaysOnSuppressedChange$2 extends Lambda implements Function1<LogMessage, String> {
    public static final DozeLogger$logAlwaysOnSuppressedChange$2 INSTANCE = new DozeLogger$logAlwaysOnSuppressedChange$2();

    public DozeLogger$logAlwaysOnSuppressedChange$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "Always on (AOD) suppressed changed, suppressed=" + logMessage.getBool1() + " nextState=" + logMessage.getStr1();
    }
}
