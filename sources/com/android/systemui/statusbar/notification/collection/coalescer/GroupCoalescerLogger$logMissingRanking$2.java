package com.android.systemui.statusbar.notification.collection.coalescer;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: GroupCoalescerLogger.kt */
public final class GroupCoalescerLogger$logMissingRanking$2 extends Lambda implements Function1<LogMessage, String> {
    public static final GroupCoalescerLogger$logMissingRanking$2 INSTANCE = new GroupCoalescerLogger$logMissingRanking$2();

    public GroupCoalescerLogger$logMissingRanking$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return Intrinsics.stringPlus("RankingMap is missing an entry for coalesced notification ", logMessage.getStr1());
    }
}
