package com.android.systemui.statusbar.notification.interruption;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: HeadsUpViewBinderLogger.kt */
public final class HeadsUpViewBinderLogger$entryContentViewMarkedFreeable$2 extends Lambda implements Function1<LogMessage, String> {
    public static final HeadsUpViewBinderLogger$entryContentViewMarkedFreeable$2 INSTANCE = new HeadsUpViewBinderLogger$entryContentViewMarkedFreeable$2();

    public HeadsUpViewBinderLogger$entryContentViewMarkedFreeable$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "start unbinding heads up entry " + logMessage.getStr1() + ' ';
    }
}
