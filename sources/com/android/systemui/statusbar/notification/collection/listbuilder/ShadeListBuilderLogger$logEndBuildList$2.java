package com.android.systemui.statusbar.notification.collection.listbuilder;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: ShadeListBuilderLogger.kt */
public final class ShadeListBuilderLogger$logEndBuildList$2 extends Lambda implements Function1<LogMessage, String> {
    public static final ShadeListBuilderLogger$logEndBuildList$2 INSTANCE = new ShadeListBuilderLogger$logEndBuildList$2();

    public ShadeListBuilderLogger$logEndBuildList$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "(Build " + logMessage.getLong1() + ") Build complete (" + logMessage.getInt1() + " top-level entries, " + logMessage.getInt2() + " children)";
    }
}
