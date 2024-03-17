package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: StackStateLogger.kt */
public final class StackStateLogger$logHUNViewDisappearingWithRemoveEvent$2 extends Lambda implements Function1<LogMessage, String> {
    public static final StackStateLogger$logHUNViewDisappearingWithRemoveEvent$2 INSTANCE = new StackStateLogger$logHUNViewDisappearingWithRemoveEvent$2();

    public StackStateLogger$logHUNViewDisappearingWithRemoveEvent$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "Heads up view disappearing " + logMessage.getStr1() + " for ANIMATION_TYPE_REMOVE";
    }
}
