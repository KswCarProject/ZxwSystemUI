package com.android.systemui.statusbar.phone;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: LSShadeTransitionLogger.kt */
public final class LSShadeTransitionLogger$logUnSuccessfulDragDown$2 extends Lambda implements Function1<LogMessage, String> {
    public static final LSShadeTransitionLogger$logUnSuccessfulDragDown$2 INSTANCE = new LSShadeTransitionLogger$logUnSuccessfulDragDown$2();

    public LSShadeTransitionLogger$logUnSuccessfulDragDown$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return Intrinsics.stringPlus("Tried to drag down but can't drag down on ", logMessage.getStr1());
    }
}
