package com.android.systemui.broadcast.logging;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: BroadcastDispatcherLogger.kt */
public final class BroadcastDispatcherLogger$logContextReceiverUnregistered$2 extends Lambda implements Function1<LogMessage, String> {
    public static final BroadcastDispatcherLogger$logContextReceiverUnregistered$2 INSTANCE = new BroadcastDispatcherLogger$logContextReceiverUnregistered$2();

    public BroadcastDispatcherLogger$logContextReceiverUnregistered$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "Receiver unregistered with Context for user " + logMessage.getInt1() + ", action " + logMessage.getStr1();
    }
}
