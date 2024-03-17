package com.android.systemui.broadcast.logging;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: BroadcastDispatcherLogger.kt */
public final class BroadcastDispatcherLogger$logBroadcastDispatched$2 extends Lambda implements Function1<LogMessage, String> {
    public static final BroadcastDispatcherLogger$logBroadcastDispatched$2 INSTANCE = new BroadcastDispatcherLogger$logBroadcastDispatched$2();

    public BroadcastDispatcherLogger$logBroadcastDispatched$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "Broadcast " + logMessage.getInt1() + " (" + logMessage.getStr1() + ") dispatched to " + logMessage.getStr2();
    }
}
