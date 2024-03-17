package com.android.systemui.doze;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: DozeLogger.kt */
public final class DozeLogger$logDozeScreenBrightness$2 extends Lambda implements Function1<LogMessage, String> {
    public static final DozeLogger$logDozeScreenBrightness$2 INSTANCE = new DozeLogger$logDozeScreenBrightness$2();

    public DozeLogger$logDozeScreenBrightness$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return Intrinsics.stringPlus("Doze screen brightness set, brightness=", Integer.valueOf(logMessage.getInt1()));
    }
}
