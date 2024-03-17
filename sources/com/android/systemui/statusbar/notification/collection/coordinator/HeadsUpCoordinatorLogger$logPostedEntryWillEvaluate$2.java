package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: HeadsUpCoordinatorLogger.kt */
public final class HeadsUpCoordinatorLogger$logPostedEntryWillEvaluate$2 extends Lambda implements Function1<LogMessage, String> {
    public static final HeadsUpCoordinatorLogger$logPostedEntryWillEvaluate$2 INSTANCE = new HeadsUpCoordinatorLogger$logPostedEntryWillEvaluate$2();

    public HeadsUpCoordinatorLogger$logPostedEntryWillEvaluate$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "will evaluate posted entry " + logMessage.getStr1() + ": reason=" + logMessage.getStr2() + " shouldHeadsUpEver=" + logMessage.getBool1() + " shouldHeadsUpAgain=" + logMessage.getBool2();
    }
}
