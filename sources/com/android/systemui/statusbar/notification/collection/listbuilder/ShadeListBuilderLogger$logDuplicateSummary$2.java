package com.android.systemui.statusbar.notification.collection.listbuilder;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: ShadeListBuilderLogger.kt */
public final class ShadeListBuilderLogger$logDuplicateSummary$2 extends Lambda implements Function1<LogMessage, String> {
    public static final ShadeListBuilderLogger$logDuplicateSummary$2 INSTANCE = new ShadeListBuilderLogger$logDuplicateSummary$2();

    public ShadeListBuilderLogger$logDuplicateSummary$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "(Build " + logMessage.getLong1() + ") Duplicate summary for group \"" + logMessage.getStr1() + "\": \"" + logMessage.getStr2() + "\" vs. \"" + logMessage.getStr3() + '\"';
    }
}
