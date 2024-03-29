package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: GutsCoordinatorLogger.kt */
public final class GutsCoordinatorLogger$logGutsOpened$2 extends Lambda implements Function1<LogMessage, String> {
    public static final GutsCoordinatorLogger$logGutsOpened$2 INSTANCE = new GutsCoordinatorLogger$logGutsOpened$2();

    public GutsCoordinatorLogger$logGutsOpened$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "Guts of type " + logMessage.getStr2() + " (leave behind: " + logMessage.getBool1() + ") opened for class " + logMessage.getStr1();
    }
}
