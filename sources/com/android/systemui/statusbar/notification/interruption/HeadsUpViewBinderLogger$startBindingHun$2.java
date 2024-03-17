package com.android.systemui.statusbar.notification.interruption;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: HeadsUpViewBinderLogger.kt */
public final class HeadsUpViewBinderLogger$startBindingHun$2 extends Lambda implements Function1<LogMessage, String> {
    public static final HeadsUpViewBinderLogger$startBindingHun$2 INSTANCE = new HeadsUpViewBinderLogger$startBindingHun$2();

    public HeadsUpViewBinderLogger$startBindingHun$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "start binding heads up entry " + logMessage.getStr1() + ' ';
    }
}
