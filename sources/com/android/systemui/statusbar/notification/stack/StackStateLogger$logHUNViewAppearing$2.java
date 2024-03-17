package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: StackStateLogger.kt */
public final class StackStateLogger$logHUNViewAppearing$2 extends Lambda implements Function1<LogMessage, String> {
    public static final StackStateLogger$logHUNViewAppearing$2 INSTANCE = new StackStateLogger$logHUNViewAppearing$2();

    public StackStateLogger$logHUNViewAppearing$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "Heads up notification view appearing " + logMessage.getStr1() + ' ';
    }
}
