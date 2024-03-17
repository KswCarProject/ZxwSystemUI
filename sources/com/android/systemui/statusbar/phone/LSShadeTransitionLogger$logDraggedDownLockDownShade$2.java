package com.android.systemui.statusbar.phone;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: LSShadeTransitionLogger.kt */
public final class LSShadeTransitionLogger$logDraggedDownLockDownShade$2 extends Lambda implements Function1<LogMessage, String> {
    public static final LSShadeTransitionLogger$logDraggedDownLockDownShade$2 INSTANCE = new LSShadeTransitionLogger$logDraggedDownLockDownShade$2();

    public LSShadeTransitionLogger$logDraggedDownLockDownShade$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return Intrinsics.stringPlus("Dragged down in locked down shade on ", logMessage.getStr1());
    }
}
