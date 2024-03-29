package com.android.systemui.toast;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: ToastLogger.kt */
public final class ToastLogger$logOnShowToast$2 extends Lambda implements Function1<LogMessage, String> {
    public static final ToastLogger$logOnShowToast$2 INSTANCE = new ToastLogger$logOnShowToast$2();

    public ToastLogger$logOnShowToast$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return '[' + logMessage.getStr3() + "] Show toast for (" + logMessage.getStr1() + ", " + logMessage.getInt1() + "). msg='" + logMessage.getStr2() + '\'';
    }
}
