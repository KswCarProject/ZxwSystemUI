package com.android.systemui.statusbar.phone;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: LSShadeTransitionLogger.kt */
public final class LSShadeTransitionLogger$logGoingToLockedShade$2 extends Lambda implements Function1<LogMessage, String> {
    public final /* synthetic */ boolean $customAnimationHandler;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public LSShadeTransitionLogger$logGoingToLockedShade$2(boolean z) {
        super(1);
        this.$customAnimationHandler = z;
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return Intrinsics.stringPlus("Going to locked shade ", this.$customAnimationHandler ? "with" : "without a custom handler");
    }
}
