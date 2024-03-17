package com.android.systemui.statusbar.phone;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: LSShadeTransitionLogger.kt */
public final class LSShadeTransitionLogger$logDragDownAmountResetWhenFullyCollapsed$2 extends Lambda implements Function1<LogMessage, String> {
    public static final LSShadeTransitionLogger$logDragDownAmountResetWhenFullyCollapsed$2 INSTANCE = new LSShadeTransitionLogger$logDragDownAmountResetWhenFullyCollapsed$2();

    public LSShadeTransitionLogger$logDragDownAmountResetWhenFullyCollapsed$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "Drag down amount stuck and reset after shade was fully collapsed";
    }
}
