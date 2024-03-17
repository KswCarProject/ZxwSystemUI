package com.android.systemui.statusbar.notification.interruption;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: HeadsUpViewBinderLogger.kt */
public final class HeadsUpViewBinderLogger$entryUnbound$2 extends Lambda implements Function1<LogMessage, String> {
    public static final HeadsUpViewBinderLogger$entryUnbound$2 INSTANCE = new HeadsUpViewBinderLogger$entryUnbound$2();

    public HeadsUpViewBinderLogger$entryUnbound$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "heads up entry unbound successfully " + logMessage.getStr1() + ' ';
    }
}
