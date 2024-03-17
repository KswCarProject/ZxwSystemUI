package com.android.systemui.broadcast.logging;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: BroadcastDispatcherLogger.kt */
public final class BroadcastDispatcherLogger$logBroadcastReceived$2 extends Lambda implements Function1<LogMessage, String> {
    public static final BroadcastDispatcherLogger$logBroadcastReceived$2 INSTANCE = new BroadcastDispatcherLogger$logBroadcastReceived$2();

    public BroadcastDispatcherLogger$logBroadcastReceived$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return '[' + logMessage.getInt1() + "] Broadcast received for user " + logMessage.getInt2() + ": " + logMessage.getStr1();
    }
}
