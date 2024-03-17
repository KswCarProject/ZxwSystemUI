package com.android.systemui.statusbar.phone;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: LSShadeTransitionLogger.kt */
public final class LSShadeTransitionLogger$logPulseExpansionFinished$4 extends Lambda implements Function1<LogMessage, String> {
    public static final LSShadeTransitionLogger$logPulseExpansionFinished$4 INSTANCE = new LSShadeTransitionLogger$logPulseExpansionFinished$4();

    public LSShadeTransitionLogger$logPulseExpansionFinished$4() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "Pulse Expansion is requested to finish";
    }
}
