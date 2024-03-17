package com.android.systemui.statusbar.notification.collection.coalescer;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: GroupCoalescerLogger.kt */
public final class GroupCoalescerLogger$logEmitBatch$2 extends Lambda implements Function1<LogMessage, String> {
    public static final GroupCoalescerLogger$logEmitBatch$2 INSTANCE = new GroupCoalescerLogger$logEmitBatch$2();

    public GroupCoalescerLogger$logEmitBatch$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "Emitting batch for group " + logMessage.getStr1() + " size=" + logMessage.getInt1() + " age=" + logMessage.getLong1() + "ms";
    }
}
