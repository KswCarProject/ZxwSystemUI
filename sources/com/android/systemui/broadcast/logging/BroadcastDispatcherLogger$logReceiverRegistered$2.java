package com.android.systemui.broadcast.logging;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: BroadcastDispatcherLogger.kt */
public final class BroadcastDispatcherLogger$logReceiverRegistered$2 extends Lambda implements Function1<LogMessage, String> {
    public static final BroadcastDispatcherLogger$logReceiverRegistered$2 INSTANCE = new BroadcastDispatcherLogger$logReceiverRegistered$2();

    public BroadcastDispatcherLogger$logReceiverRegistered$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "Receiver " + logMessage.getStr1() + " (" + logMessage.getStr2() + ") registered for user " + logMessage.getInt1();
    }
}
