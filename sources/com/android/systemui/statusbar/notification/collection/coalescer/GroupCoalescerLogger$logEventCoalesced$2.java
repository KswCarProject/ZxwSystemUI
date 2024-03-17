package com.android.systemui.statusbar.notification.collection.coalescer;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: GroupCoalescerLogger.kt */
public final class GroupCoalescerLogger$logEventCoalesced$2 extends Lambda implements Function1<LogMessage, String> {
    public static final GroupCoalescerLogger$logEventCoalesced$2 INSTANCE = new GroupCoalescerLogger$logEventCoalesced$2();

    public GroupCoalescerLogger$logEventCoalesced$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return Intrinsics.stringPlus("COALESCED: ", logMessage.getStr1());
    }
}
