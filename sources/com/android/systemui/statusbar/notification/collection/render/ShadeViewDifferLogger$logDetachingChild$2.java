package com.android.systemui.statusbar.notification.collection.render;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: ShadeViewDifferLogger.kt */
public final class ShadeViewDifferLogger$logDetachingChild$2 extends Lambda implements Function1<LogMessage, String> {
    public static final ShadeViewDifferLogger$logDetachingChild$2 INSTANCE = new ShadeViewDifferLogger$logDetachingChild$2();

    public ShadeViewDifferLogger$logDetachingChild$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "Detach " + logMessage.getStr1() + " isTransfer=" + logMessage.getBool1() + " isParentRemoved=" + logMessage.getBool2() + " oldParent=" + logMessage.getStr2() + " newParent=" + logMessage.getStr3();
    }
}
