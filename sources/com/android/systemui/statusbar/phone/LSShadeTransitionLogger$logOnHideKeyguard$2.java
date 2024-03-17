package com.android.systemui.statusbar.phone;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: LSShadeTransitionLogger.kt */
public final class LSShadeTransitionLogger$logOnHideKeyguard$2 extends Lambda implements Function1<LogMessage, String> {
    public static final LSShadeTransitionLogger$logOnHideKeyguard$2 INSTANCE = new LSShadeTransitionLogger$logOnHideKeyguard$2();

    public LSShadeTransitionLogger$logOnHideKeyguard$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "Notified that the keyguard is being hidden";
    }
}
