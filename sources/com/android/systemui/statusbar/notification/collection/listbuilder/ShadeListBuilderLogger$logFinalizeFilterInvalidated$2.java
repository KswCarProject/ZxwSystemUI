package com.android.systemui.statusbar.notification.collection.listbuilder;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: ShadeListBuilderLogger.kt */
public final class ShadeListBuilderLogger$logFinalizeFilterInvalidated$2 extends Lambda implements Function1<LogMessage, String> {
    public static final ShadeListBuilderLogger$logFinalizeFilterInvalidated$2 INSTANCE = new ShadeListBuilderLogger$logFinalizeFilterInvalidated$2();

    public ShadeListBuilderLogger$logFinalizeFilterInvalidated$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "Finalize NotifFilter \"" + logMessage.getStr1() + "\" invalidated; pipeline state is " + logMessage.getInt1();
    }
}
