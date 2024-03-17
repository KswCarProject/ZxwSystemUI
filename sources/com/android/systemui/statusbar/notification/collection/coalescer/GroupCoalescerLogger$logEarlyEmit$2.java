package com.android.systemui.statusbar.notification.collection.coalescer;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: GroupCoalescerLogger.kt */
public final class GroupCoalescerLogger$logEarlyEmit$2 extends Lambda implements Function1<LogMessage, String> {
    public static final GroupCoalescerLogger$logEarlyEmit$2 INSTANCE = new GroupCoalescerLogger$logEarlyEmit$2();

    public GroupCoalescerLogger$logEarlyEmit$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "Modification of notif " + logMessage.getStr1() + " triggered early emit of batched group " + logMessage.getStr2();
    }
}
