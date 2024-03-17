package com.android.systemui.statusbar.notification.collection.notifcollection;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotifCollectionLogger.kt */
public final class NotifCollectionLogger$logNotifInternalUpdate$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotifCollectionLogger$logNotifInternalUpdate$2 INSTANCE = new NotifCollectionLogger$logNotifInternalUpdate$2();

    public NotifCollectionLogger$logNotifInternalUpdate$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "UPDATED INTERNALLY " + logMessage.getStr1() + " BY " + logMessage.getStr2() + " BECAUSE " + logMessage.getStr3();
    }
}
