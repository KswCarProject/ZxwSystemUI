package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: PreparationCoordinatorLogger.kt */
public final class PreparationCoordinatorLogger$logDoneWaitingForGroupInflation$2 extends Lambda implements Function1<LogMessage, String> {
    public static final PreparationCoordinatorLogger$logDoneWaitingForGroupInflation$2 INSTANCE = new PreparationCoordinatorLogger$logDoneWaitingForGroupInflation$2();

    public PreparationCoordinatorLogger$logDoneWaitingForGroupInflation$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "Finished inflating all members of group " + logMessage.getStr1() + ", releasing group";
    }
}
