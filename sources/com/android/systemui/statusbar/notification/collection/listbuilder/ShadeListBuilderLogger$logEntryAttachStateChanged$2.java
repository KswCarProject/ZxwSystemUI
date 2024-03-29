package com.android.systemui.statusbar.notification.collection.listbuilder;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: ShadeListBuilderLogger.kt */
public final class ShadeListBuilderLogger$logEntryAttachStateChanged$2 extends Lambda implements Function1<LogMessage, String> {
    public static final ShadeListBuilderLogger$logEntryAttachStateChanged$2 INSTANCE = new ShadeListBuilderLogger$logEntryAttachStateChanged$2();

    public ShadeListBuilderLogger$logEntryAttachStateChanged$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        String str;
        if (logMessage.getStr2() == null && logMessage.getStr3() != null) {
            str = "ATTACHED";
        } else if (logMessage.getStr2() == null || logMessage.getStr3() != null) {
            str = (logMessage.getStr2() == null && logMessage.getStr3() == null) ? "MODIFIED (DETACHED)" : "MODIFIED (ATTACHED)";
        } else {
            str = "DETACHED";
        }
        return "(Build " + logMessage.getLong1() + ") " + str + " {" + logMessage.getStr1() + '}';
    }
}
