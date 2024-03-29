package com.android.systemui.doze;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: DozeLogger.kt */
public final class DozeLogger$logPulseDropped$2 extends Lambda implements Function1<LogMessage, String> {
    public static final DozeLogger$logPulseDropped$2 INSTANCE = new DozeLogger$logPulseDropped$2();

    public DozeLogger$logPulseDropped$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "Pulse dropped, pulsePending=" + logMessage.getBool1() + " state=" + logMessage.getStr1() + " blocked=" + logMessage.getBool2();
    }
}
