package com.android.systemui.statusbar.notification.dagger;

import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotifLiveDataStore;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import com.android.systemui.statusbar.notification.logging.NotificationPanelLogger;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class NotificationsModule_ProvideNotificationLoggerFactory implements Factory<NotificationLogger> {
    public final Provider<NotificationEntryManager> entryManagerProvider;
    public final Provider<NotificationLogger.ExpansionStateLogger> expansionStateLoggerProvider;
    public final Provider<NotifLiveDataStore> notifLiveDataStoreProvider;
    public final Provider<NotifPipelineFlags> notifPipelineFlagsProvider;
    public final Provider<NotifPipeline> notifPipelineProvider;
    public final Provider<NotificationListener> notificationListenerProvider;
    public final Provider<NotificationPanelLogger> notificationPanelLoggerProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;
    public final Provider<Executor> uiBgExecutorProvider;
    public final Provider<NotificationVisibilityProvider> visibilityProvider;

    public NotificationsModule_ProvideNotificationLoggerFactory(Provider<NotificationListener> provider, Provider<Executor> provider2, Provider<NotifPipelineFlags> provider3, Provider<NotifLiveDataStore> provider4, Provider<NotificationVisibilityProvider> provider5, Provider<NotificationEntryManager> provider6, Provider<NotifPipeline> provider7, Provider<StatusBarStateController> provider8, Provider<NotificationLogger.ExpansionStateLogger> provider9, Provider<NotificationPanelLogger> provider10) {
        this.notificationListenerProvider = provider;
        this.uiBgExecutorProvider = provider2;
        this.notifPipelineFlagsProvider = provider3;
        this.notifLiveDataStoreProvider = provider4;
        this.visibilityProvider = provider5;
        this.entryManagerProvider = provider6;
        this.notifPipelineProvider = provider7;
        this.statusBarStateControllerProvider = provider8;
        this.expansionStateLoggerProvider = provider9;
        this.notificationPanelLoggerProvider = provider10;
    }

    public NotificationLogger get() {
        return provideNotificationLogger(this.notificationListenerProvider.get(), this.uiBgExecutorProvider.get(), this.notifPipelineFlagsProvider.get(), this.notifLiveDataStoreProvider.get(), this.visibilityProvider.get(), this.entryManagerProvider.get(), this.notifPipelineProvider.get(), this.statusBarStateControllerProvider.get(), this.expansionStateLoggerProvider.get(), this.notificationPanelLoggerProvider.get());
    }

    public static NotificationsModule_ProvideNotificationLoggerFactory create(Provider<NotificationListener> provider, Provider<Executor> provider2, Provider<NotifPipelineFlags> provider3, Provider<NotifLiveDataStore> provider4, Provider<NotificationVisibilityProvider> provider5, Provider<NotificationEntryManager> provider6, Provider<NotifPipeline> provider7, Provider<StatusBarStateController> provider8, Provider<NotificationLogger.ExpansionStateLogger> provider9, Provider<NotificationPanelLogger> provider10) {
        return new NotificationsModule_ProvideNotificationLoggerFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10);
    }

    public static NotificationLogger provideNotificationLogger(NotificationListener notificationListener, Executor executor, NotifPipelineFlags notifPipelineFlags, NotifLiveDataStore notifLiveDataStore, NotificationVisibilityProvider notificationVisibilityProvider, NotificationEntryManager notificationEntryManager, NotifPipeline notifPipeline, StatusBarStateController statusBarStateController, NotificationLogger.ExpansionStateLogger expansionStateLogger, NotificationPanelLogger notificationPanelLogger) {
        return (NotificationLogger) Preconditions.checkNotNullFromProvides(NotificationsModule.provideNotificationLogger(notificationListener, executor, notifPipelineFlags, notifLiveDataStore, notificationVisibilityProvider, notificationEntryManager, notifPipeline, statusBarStateController, expansionStateLogger, notificationPanelLogger));
    }
}
