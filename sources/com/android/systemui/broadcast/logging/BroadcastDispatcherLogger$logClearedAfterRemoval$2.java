package com.android.systemui.broadcast.logging;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: BroadcastDispatcherLogger.kt */
public final class BroadcastDispatcherLogger$logClearedAfterRemoval$2 extends Lambda implements Function1<LogMessage, String> {
    public static final BroadcastDispatcherLogger$logClearedAfterRemoval$2 INSTANCE = new BroadcastDispatcherLogger$logClearedAfterRemoval$2();

    public BroadcastDispatcherLogger$logClearedAfterRemoval$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "Receiver " + logMessage.getStr1() + " has been completely removed for user " + logMessage.getInt1();
    }
}
