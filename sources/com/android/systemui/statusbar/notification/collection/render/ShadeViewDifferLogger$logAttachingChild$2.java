package com.android.systemui.statusbar.notification.collection.render;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: ShadeViewDifferLogger.kt */
public final class ShadeViewDifferLogger$logAttachingChild$2 extends Lambda implements Function1<LogMessage, String> {
    public static final ShadeViewDifferLogger$logAttachingChild$2 INSTANCE = new ShadeViewDifferLogger$logAttachingChild$2();

    public ShadeViewDifferLogger$logAttachingChild$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "Attaching view " + logMessage.getStr1() + " to " + logMessage.getStr2();
    }
}
