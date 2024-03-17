package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: HeadsUpCoordinatorLogger.kt */
public final class HeadsUpCoordinatorLogger$logPostedEntryWillNotEvaluate$2 extends Lambda implements Function1<LogMessage, String> {
    public static final HeadsUpCoordinatorLogger$logPostedEntryWillNotEvaluate$2 INSTANCE = new HeadsUpCoordinatorLogger$logPostedEntryWillNotEvaluate$2();

    public HeadsUpCoordinatorLogger$logPostedEntryWillNotEvaluate$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "will not evaluate posted entry " + logMessage.getStr1() + ": reason=" + logMessage.getStr2();
    }
}
