package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: SharedCoordinatorLogger.kt */
public final class SharedCoordinatorLogger$logKeyguardCoordinatorInvalidated$2 extends Lambda implements Function1<LogMessage, String> {
    public static final SharedCoordinatorLogger$logKeyguardCoordinatorInvalidated$2 INSTANCE = new SharedCoordinatorLogger$logKeyguardCoordinatorInvalidated$2();

    public SharedCoordinatorLogger$logKeyguardCoordinatorInvalidated$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return Intrinsics.stringPlus("KeyguardCoordinator invalidated: ", logMessage.getStr1());
    }
}
