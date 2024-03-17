package com.android.systemui.statusbar.notification.collection.render;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: NodeSpecBuilderLogger.kt */
public final class NodeSpecBuilderLogger$logBuildNodeSpec$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NodeSpecBuilderLogger$logBuildNodeSpec$2 INSTANCE = new NodeSpecBuilderLogger$logBuildNodeSpec$2();

    public NodeSpecBuilderLogger$logBuildNodeSpec$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "buildNodeSpec finished with " + logMessage.getInt1() + " sections";
    }
}
