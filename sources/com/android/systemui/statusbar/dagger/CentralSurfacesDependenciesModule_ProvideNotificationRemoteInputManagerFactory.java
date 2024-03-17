package com.android.systemui.statusbar.dagger;

import android.content.Context;
import android.os.Handler;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.ActionClickLogger;
import com.android.systemui.statusbar.NotificationClickNotifier;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.RemoteInputNotificationRebuilder;
import com.android.systemui.statusbar.SmartReplyController;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.policy.RemoteInputUriController;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;

public final class CentralSurfacesDependenciesModule_ProvideNotificationRemoteInputManagerFactory implements Factory<NotificationRemoteInputManager> {
    public final Provider<ActionClickLogger> actionClickLoggerProvider;
    public final Provider<Optional<CentralSurfaces>> centralSurfacesOptionalLazyProvider;
    public final Provider<NotificationClickNotifier> clickNotifierProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<NotificationLockscreenUserManager> lockscreenUserManagerProvider;
    public final Provider<Handler> mainHandlerProvider;
    public final Provider<NotifPipelineFlags> notifPipelineFlagsProvider;
    public final Provider<NotificationEntryManager> notificationEntryManagerProvider;
    public final Provider<RemoteInputNotificationRebuilder> rebuilderProvider;
    public final Provider<RemoteInputUriController> remoteInputUriControllerProvider;
    public final Provider<SmartReplyController> smartReplyControllerProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;
    public final Provider<NotificationVisibilityProvider> visibilityProvider;

    public CentralSurfacesDependenciesModule_ProvideNotificationRemoteInputManagerFactory(Provider<Context> provider, Provider<NotifPipelineFlags> provider2, Provider<NotificationLockscreenUserManager> provider3, Provider<SmartReplyController> provider4, Provider<NotificationVisibilityProvider> provider5, Provider<NotificationEntryManager> provider6, Provider<RemoteInputNotificationRebuilder> provider7, Provider<Optional<CentralSurfaces>> provider8, Provider<StatusBarStateController> provider9, Provider<Handler> provider10, Provider<RemoteInputUriController> provider11, Provider<NotificationClickNotifier> provider12, Provider<ActionClickLogger> provider13, Provider<DumpManager> provider14) {
        this.contextProvider = provider;
        this.notifPipelineFlagsProvider = provider2;
        this.lockscreenUserManagerProvider = provider3;
        this.smartReplyControllerProvider = provider4;
        this.visibilityProvider = provider5;
        this.notificationEntryManagerProvider = provider6;
        this.rebuilderProvider = provider7;
        this.centralSurfacesOptionalLazyProvider = provider8;
        this.statusBarStateControllerProvider = provider9;
        this.mainHandlerProvider = provider10;
        this.remoteInputUriControllerProvider = provider11;
        this.clickNotifierProvider = provider12;
        this.actionClickLoggerProvider = provider13;
        this.dumpManagerProvider = provider14;
    }

    public NotificationRemoteInputManager get() {
        return provideNotificationRemoteInputManager(this.contextProvider.get(), this.notifPipelineFlagsProvider.get(), this.lockscreenUserManagerProvider.get(), this.smartReplyControllerProvider.get(), this.visibilityProvider.get(), this.notificationEntryManagerProvider.get(), this.rebuilderProvider.get(), DoubleCheck.lazy(this.centralSurfacesOptionalLazyProvider), this.statusBarStateControllerProvider.get(), this.mainHandlerProvider.get(), this.remoteInputUriControllerProvider.get(), this.clickNotifierProvider.get(), this.actionClickLoggerProvider.get(), this.dumpManagerProvider.get());
    }

    public static CentralSurfacesDependenciesModule_ProvideNotificationRemoteInputManagerFactory create(Provider<Context> provider, Provider<NotifPipelineFlags> provider2, Provider<NotificationLockscreenUserManager> provider3, Provider<SmartReplyController> provider4, Provider<NotificationVisibilityProvider> provider5, Provider<NotificationEntryManager> provider6, Provider<RemoteInputNotificationRebuilder> provider7, Provider<Optional<CentralSurfaces>> provider8, Provider<StatusBarStateController> provider9, Provider<Handler> provider10, Provider<RemoteInputUriController> provider11, Provider<NotificationClickNotifier> provider12, Provider<ActionClickLogger> provider13, Provider<DumpManager> provider14) {
        return new CentralSurfacesDependenciesModule_ProvideNotificationRemoteInputManagerFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14);
    }

    public static NotificationRemoteInputManager provideNotificationRemoteInputManager(Context context, NotifPipelineFlags notifPipelineFlags, NotificationLockscreenUserManager notificationLockscreenUserManager, SmartReplyController smartReplyController, NotificationVisibilityProvider notificationVisibilityProvider, NotificationEntryManager notificationEntryManager, RemoteInputNotificationRebuilder remoteInputNotificationRebuilder, Lazy<Optional<CentralSurfaces>> lazy, StatusBarStateController statusBarStateController, Handler handler, RemoteInputUriController remoteInputUriController, NotificationClickNotifier notificationClickNotifier, ActionClickLogger actionClickLogger, DumpManager dumpManager) {
        return (NotificationRemoteInputManager) Preconditions.checkNotNullFromProvides(CentralSurfacesDependenciesModule.provideNotificationRemoteInputManager(context, notifPipelineFlags, notificationLockscreenUserManager, smartReplyController, notificationVisibilityProvider, notificationEntryManager, remoteInputNotificationRebuilder, lazy, statusBarStateController, handler, remoteInputUriController, notificationClickNotifier, actionClickLogger, dumpManager));
    }
}
