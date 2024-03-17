package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotificationStackScrollLogger.kt */
public final class NotificationStackScrollLogger$hunAnimationEventAdded$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotificationStackScrollLogger$hunAnimationEventAdded$2 INSTANCE = new NotificationStackScrollLogger$hunAnimationEventAdded$2();

    public NotificationStackScrollLogger$hunAnimationEventAdded$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "heads up animation added: " + logMessage.getStr1() + " with type " + logMessage.getStr2();
    }
}
