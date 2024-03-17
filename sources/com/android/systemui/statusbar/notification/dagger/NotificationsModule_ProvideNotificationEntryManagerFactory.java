package com.android.systemui.statusbar.notification.dagger;

import com.android.internal.statusbar.IStatusBarService;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.NotificationEntryManagerLogger;
import com.android.systemui.statusbar.notification.collection.NotifLiveDataStoreImpl;
import com.android.systemui.statusbar.notification.collection.inflation.NotificationRowBinder;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.util.leak.LeakDetector;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class NotificationsModule_ProvideNotificationEntryManagerFactory implements Factory<NotificationEntryManager> {
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<NotificationGroupManagerLegacy> groupManagerProvider;
    public final Provider<LeakDetector> leakDetectorProvider;
    public final Provider<NotificationEntryManagerLogger> loggerProvider;
    public final Provider<NotifLiveDataStoreImpl> notifLiveDataStoreProvider;
    public final Provider<NotifPipelineFlags> notifPipelineFlagsProvider;
    public final Provider<NotificationRemoteInputManager> notificationRemoteInputManagerLazyProvider;
    public final Provider<NotificationRowBinder> notificationRowBinderLazyProvider;
    public final Provider<IStatusBarService> statusBarServiceProvider;

    public NotificationsModule_ProvideNotificationEntryManagerFactory(Provider<NotificationEntryManagerLogger> provider, Provider<NotificationGroupManagerLegacy> provider2, Provider<NotifPipelineFlags> provider3, Provider<NotificationRowBinder> provider4, Provider<NotificationRemoteInputManager> provider5, Provider<LeakDetector> provider6, Provider<IStatusBarService> provider7, Provider<NotifLiveDataStoreImpl> provider8, Provider<DumpManager> provider9) {
        this.loggerProvider = provider;
        this.groupManagerProvider = provider2;
        this.notifPipelineFlagsProvider = provider3;
        this.notificationRowBinderLazyProvider = provider4;
        this.notificationRemoteInputManagerLazyProvider = provider5;
        this.leakDetectorProvider = provider6;
        this.statusBarServiceProvider = provider7;
        this.notifLiveDataStoreProvider = provider8;
        this.dumpManagerProvider = provider9;
    }

    public NotificationEntryManager get() {
        return provideNotificationEntryManager(this.loggerProvider.get(), this.groupManagerProvider.get(), this.notifPipelineFlagsProvider.get(), DoubleCheck.lazy(this.notificationRowBinderLazyProvider), DoubleCheck.lazy(this.notificationRemoteInputManagerLazyProvider), this.leakDetectorProvider.get(), this.statusBarServiceProvider.get(), this.notifLiveDataStoreProvider.get(), this.dumpManagerProvider.get());
    }

    public static NotificationsModule_ProvideNotificationEntryManagerFactory create(Provider<NotificationEntryManagerLogger> provider, Provider<NotificationGroupManagerLegacy> provider2, Provider<NotifPipelineFlags> provider3, Provider<NotificationRowBinder> provider4, Provider<NotificationRemoteInputManager> provider5, Provider<LeakDetector> provider6, Provider<IStatusBarService> provider7, Provider<NotifLiveDataStoreImpl> provider8, Provider<DumpManager> provider9) {
        return new NotificationsModule_ProvideNotificationEntryManagerFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9);
    }

    public static NotificationEntryManager provideNotificationEntryManager(NotificationEntryManagerLogger notificationEntryManagerLogger, NotificationGroupManagerLegacy notificationGroupManagerLegacy, NotifPipelineFlags notifPipelineFlags, Lazy<NotificationRowBinder> lazy, Lazy<NotificationRemoteInputManager> lazy2, LeakDetector leakDetector, IStatusBarService iStatusBarService, NotifLiveDataStoreImpl notifLiveDataStoreImpl, DumpManager dumpManager) {
        return (NotificationEntryManager) Preconditions.checkNotNullFromProvides(NotificationsModule.provideNotificationEntryManager(notificationEntryManagerLogger, notificationGroupManagerLegacy, notifPipelineFlags, lazy, lazy2, leakDetector, iStatusBarService, notifLiveDataStoreImpl, dumpManager));
    }
}
