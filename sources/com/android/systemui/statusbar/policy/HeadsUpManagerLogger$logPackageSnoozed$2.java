package com.android.systemui.statusbar.policy;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: HeadsUpManagerLogger.kt */
public final class HeadsUpManagerLogger$logPackageSnoozed$2 extends Lambda implements Function1<LogMessage, String> {
    public static final HeadsUpManagerLogger$logPackageSnoozed$2 INSTANCE = new HeadsUpManagerLogger$logPackageSnoozed$2();

    public HeadsUpManagerLogger$logPackageSnoozed$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return Intrinsics.stringPlus("package snoozed ", logMessage.getStr1());
    }
}
