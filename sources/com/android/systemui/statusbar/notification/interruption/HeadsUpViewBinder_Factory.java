package com.android.systemui.statusbar.notification.interruption;

import com.android.internal.util.NotificationMessagingUtil;
import com.android.systemui.statusbar.notification.row.RowContentBindStage;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class HeadsUpViewBinder_Factory implements Factory<HeadsUpViewBinder> {
    public final Provider<RowContentBindStage> bindStageProvider;
    public final Provider<HeadsUpViewBinderLogger> loggerProvider;
    public final Provider<NotificationMessagingUtil> notificationMessagingUtilProvider;

    public HeadsUpViewBinder_Factory(Provider<NotificationMessagingUtil> provider, Provider<RowContentBindStage> provider2, Provider<HeadsUpViewBinderLogger> provider3) {
        this.notificationMessagingUtilProvider = provider;
        this.bindStageProvider = provider2;
        this.loggerProvider = provider3;
    }

    public HeadsUpViewBinder get() {
        return newInstance(this.notificationMessagingUtilProvider.get(), this.bindStageProvider.get(), this.loggerProvider.get());
    }

    public static HeadsUpViewBinder_Factory create(Provider<NotificationMessagingUtil> provider, Provider<RowContentBindStage> provider2, Provider<HeadsUpViewBinderLogger> provider3) {
        return new HeadsUpViewBinder_Factory(provider, provider2, provider3);
    }

    public static HeadsUpViewBinder newInstance(NotificationMessagingUtil notificationMessagingUtil, RowContentBindStage rowContentBindStage, HeadsUpViewBinderLogger headsUpViewBinderLogger) {
        return new HeadsUpViewBinder(notificationMessagingUtil, rowContentBindStage, headsUpViewBinderLogger);
    }
}
