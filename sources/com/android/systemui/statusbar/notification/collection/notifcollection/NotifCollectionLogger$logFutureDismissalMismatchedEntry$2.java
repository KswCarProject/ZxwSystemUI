package com.android.systemui.statusbar.notification.collection.notifcollection;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotifCollectionLogger.kt */
public final class NotifCollectionLogger$logFutureDismissalMismatchedEntry$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotifCollectionLogger$logFutureDismissalMismatchedEntry$2 INSTANCE = new NotifCollectionLogger$logFutureDismissalMismatchedEntry$2();

    public NotifCollectionLogger$logFutureDismissalMismatchedEntry$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "Mismatch: current " + logMessage.getStr2() + " is " + logMessage.getStr3() + " for: " + logMessage.getStr1();
    }
}
