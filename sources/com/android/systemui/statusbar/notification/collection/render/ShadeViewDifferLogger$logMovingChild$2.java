package com.android.systemui.statusbar.notification.collection.render;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: ShadeViewDifferLogger.kt */
public final class ShadeViewDifferLogger$logMovingChild$2 extends Lambda implements Function1<LogMessage, String> {
    public static final ShadeViewDifferLogger$logMovingChild$2 INSTANCE = new ShadeViewDifferLogger$logMovingChild$2();

    public ShadeViewDifferLogger$logMovingChild$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "Moving child view " + logMessage.getStr1() + " in " + logMessage.getStr2() + " to index " + logMessage.getInt1();
    }
}
