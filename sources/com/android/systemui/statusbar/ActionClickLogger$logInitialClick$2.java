package com.android.systemui.statusbar;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: ActionClickLogger.kt */
public final class ActionClickLogger$logInitialClick$2 extends Lambda implements Function1<LogMessage, String> {
    public static final ActionClickLogger$logInitialClick$2 INSTANCE = new ActionClickLogger$logInitialClick$2();

    public ActionClickLogger$logInitialClick$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "ACTION CLICK " + logMessage.getStr1() + " (channel=" + logMessage.getStr2() + ") for pending intent " + logMessage.getStr3();
    }
}
