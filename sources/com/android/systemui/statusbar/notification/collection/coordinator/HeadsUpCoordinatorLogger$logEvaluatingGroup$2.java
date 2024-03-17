package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: HeadsUpCoordinatorLogger.kt */
public final class HeadsUpCoordinatorLogger$logEvaluatingGroup$2 extends Lambda implements Function1<LogMessage, String> {
    public static final HeadsUpCoordinatorLogger$logEvaluatingGroup$2 INSTANCE = new HeadsUpCoordinatorLogger$logEvaluatingGroup$2();

    public HeadsUpCoordinatorLogger$logEvaluatingGroup$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "evaluating group for alert transfer: " + logMessage.getStr1() + " numPostedEntries=" + logMessage.getInt1() + " logicalGroupSize=" + logMessage.getInt2();
    }
}
