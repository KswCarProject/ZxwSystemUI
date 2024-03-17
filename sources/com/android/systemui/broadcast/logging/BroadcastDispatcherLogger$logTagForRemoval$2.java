package com.android.systemui.broadcast.logging;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: BroadcastDispatcherLogger.kt */
public final class BroadcastDispatcherLogger$logTagForRemoval$2 extends Lambda implements Function1<LogMessage, String> {
    public static final BroadcastDispatcherLogger$logTagForRemoval$2 INSTANCE = new BroadcastDispatcherLogger$logTagForRemoval$2();

    public BroadcastDispatcherLogger$logTagForRemoval$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "Receiver " + logMessage.getStr1() + " tagged for removal from user " + logMessage.getInt1();
    }
}
