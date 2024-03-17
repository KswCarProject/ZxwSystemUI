package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: ShadeEventCoordinatorLogger.kt */
public final class ShadeEventCoordinatorLogger$logShadeEmptied$2 extends Lambda implements Function1<LogMessage, String> {
    public static final ShadeEventCoordinatorLogger$logShadeEmptied$2 INSTANCE = new ShadeEventCoordinatorLogger$logShadeEmptied$2();

    public ShadeEventCoordinatorLogger$logShadeEmptied$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "Shade emptied";
    }
}
