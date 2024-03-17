package com.android.systemui.statusbar.notification.collection.notifcollection;

import com.android.systemui.log.LogMessage;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotifCollectionLogger.kt */
public final class NotifCollectionLogger$logRemoteExceptionOnNotificationClear$2 extends Lambda implements Function1<LogMessage, String> {
    public static final NotifCollectionLogger$logRemoteExceptionOnNotificationClear$2 INSTANCE = new NotifCollectionLogger$logRemoteExceptionOnNotificationClear$2();

    public NotifCollectionLogger$logRemoteExceptionOnNotificationClear$2() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull LogMessage logMessage) {
        return "RemoteException while attempting to clear " + logMessage.getStr1() + ":\n" + logMessage.getStr2();
    }
}
