package com.android.systemui.toast;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: ToastLogger.kt */
public final class ToastLogger$logOrientationChange$2 extends Lambda implements Function1<LogMessage, String> {
    public static final ToastLogger$logOrientationChange$2 INSTANCE = new ToastLogger$logOrientationChange$2();

    public ToastLogger$logOrientationChange$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "Orientation change for toast. msg='" + logMessage.getStr1() + "' isPortrait=" + logMessage.getBool1();
    }
}
