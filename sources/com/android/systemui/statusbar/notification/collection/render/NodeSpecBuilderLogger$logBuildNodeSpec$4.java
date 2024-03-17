package com.android.systemui.statusbar.notification.collection.render;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: NodeSpecBuilderLogger.kt */
public final class NodeSpecBuilderLogger$logBuildNodeSpec$4 extends Lambda implements Function1<LogMessage, String> {
    public static final NodeSpecBuilderLogger$logBuildNodeSpec$4 INSTANCE = new NodeSpecBuilderLogger$logBuildNodeSpec$4();

    public NodeSpecBuilderLogger$logBuildNodeSpec$4() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "  section " + logMessage.getStr1() + " has header " + logMessage.getStr2() + ", " + logMessage.getInt1() + " entries";
    }
}
