package com.android.systemui.doze;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: DozeLogger.kt */
public final class DozeLogger$logDisplayStateDelayedByUdfps$2 extends Lambda implements Function1<LogMessage, String> {
    public static final DozeLogger$logDisplayStateDelayedByUdfps$2 INSTANCE = new DozeLogger$logDisplayStateDelayedByUdfps$2();

    public DozeLogger$logDisplayStateDelayedByUdfps$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "Delaying display state change to: " + logMessage.getStr1() + " due to UDFPS activity";
    }
}
