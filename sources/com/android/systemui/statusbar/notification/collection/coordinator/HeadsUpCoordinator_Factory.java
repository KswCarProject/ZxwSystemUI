package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.notification.collection.render.NodeController;
import com.android.systemui.statusbar.notification.interruption.HeadsUpViewBinder;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProvider;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.time.SystemClock;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class HeadsUpCoordinator_Factory implements Factory<HeadsUpCoordinator> {
    public final Provider<DelayableExecutor> mExecutorProvider;
    public final Provider<HeadsUpManager> mHeadsUpManagerProvider;
    public final Provider<HeadsUpViewBinder> mHeadsUpViewBinderProvider;
    public final Provider<NodeController> mIncomingHeaderControllerProvider;
    public final Provider<HeadsUpCoordinatorLogger> mLoggerProvider;
    public final Provider<NotificationInterruptStateProvider> mNotificationInterruptStateProvider;
    public final Provider<NotificationRemoteInputManager> mRemoteInputManagerProvider;
    public final Provider<SystemClock> mSystemClockProvider;

    public HeadsUpCoordinator_Factory(Provider<HeadsUpCoordinatorLogger> provider, Provider<SystemClock> provider2, Provider<HeadsUpManager> provider3, Provider<HeadsUpViewBinder> provider4, Provider<NotificationInterruptStateProvider> provider5, Provider<NotificationRemoteInputManager> provider6, Provider<NodeController> provider7, Provider<DelayableExecutor> provider8) {
        this.mLoggerProvider = provider;
        this.mSystemClockProvider = provider2;
        this.mHeadsUpManagerProvider = provider3;
        this.mHeadsUpViewBinderProvider = provider4;
        this.mNotificationInterruptStateProvider = provider5;
        this.mRemoteInputManagerProvider = provider6;
        this.mIncomingHeaderControllerProvider = provider7;
        this.mExecutorProvider = provider8;
    }

    public HeadsUpCoordinator get() {
        return newInstance(this.mLoggerProvider.get(), this.mSystemClockProvider.get(), this.mHeadsUpManagerProvider.get(), this.mHeadsUpViewBinderProvider.get(), this.mNotificationInterruptStateProvider.get(), this.mRemoteInputManagerProvider.get(), this.mIncomingHeaderControllerProvider.get(), this.mExecutorProvider.get());
    }

    public static HeadsUpCoordinator_Factory create(Provider<HeadsUpCoordinatorLogger> provider, Provider<SystemClock> provider2, Provider<HeadsUpManager> provider3, Provider<HeadsUpViewBinder> provider4, Provider<NotificationInterruptStateProvider> provider5, Provider<NotificationRemoteInputManager> provider6, Provider<NodeController> provider7, Provider<DelayableExecutor> provider8) {
        return new HeadsUpCoordinator_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8);
    }

    public static HeadsUpCoordinator newInstance(HeadsUpCoordinatorLogger headsUpCoordinatorLogger, SystemClock systemClock, HeadsUpManager headsUpManager, HeadsUpViewBinder headsUpViewBinder, NotificationInterruptStateProvider notificationInterruptStateProvider, NotificationRemoteInputManager notificationRemoteInputManager, NodeController nodeController, DelayableExecutor delayableExecutor) {
        return new HeadsUpCoordinator(headsUpCoordinatorLogger, systemClock, headsUpManager, headsUpViewBinder, notificationInterruptStateProvider, notificationRemoteInputManager, nodeController, delayableExecutor);
    }
}
