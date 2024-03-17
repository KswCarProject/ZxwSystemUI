package com.android.systemui.statusbar.notification.collection.notifcollection;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotifCollectionLogger.kt */
public final class NotifCollectionLogger$logFutureDismissalDismissing$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotifCollectionLogger$logFutureDismissalDismissing$2 INSTANCE = new NotifCollectionLogger$logFutureDismissalDismissing$2();

    public NotifCollectionLogger$logFutureDismissalDismissing$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "Dismissing " + logMessage.getStr2() + " for: " + logMessage.getStr1();
    }
}
