package com.android.systemui.qs.logging;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: QSLogger.kt */
public final class QSLogger$logPanelExpanded$2 extends Lambda implements Function1<LogMessage, String> {
    public static final QSLogger$logPanelExpanded$2 INSTANCE = new QSLogger$logPanelExpanded$2();

    public QSLogger$logPanelExpanded$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return logMessage.getStr1() + " expanded=" + logMessage.getBool1();
    }
}
