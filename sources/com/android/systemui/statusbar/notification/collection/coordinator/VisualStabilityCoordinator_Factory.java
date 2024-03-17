package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.collection.provider.VisualStabilityProvider;
import com.android.systemui.statusbar.phone.NotifPanelEvents;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class VisualStabilityCoordinator_Factory implements Factory<VisualStabilityCoordinator> {
    public final Provider<DelayableExecutor> delayableExecutorProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<HeadsUpManager> headsUpManagerProvider;
    public final Provider<NotifPanelEvents> notifPanelEventsProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;
    public final Provider<VisualStabilityProvider> visualStabilityProvider;
    public final Provider<WakefulnessLifecycle> wakefulnessLifecycleProvider;

    public VisualStabilityCoordinator_Factory(Provider<DelayableExecutor> provider, Provider<DumpManager> provider2, Provider<HeadsUpManager> provider3, Provider<NotifPanelEvents> provider4, Provider<StatusBarStateController> provider5, Provider<VisualStabilityProvider> provider6, Provider<WakefulnessLifecycle> provider7) {
        this.delayableExecutorProvider = provider;
        this.dumpManagerProvider = provider2;
        this.headsUpManagerProvider = provider3;
        this.notifPanelEventsProvider = provider4;
        this.statusBarStateControllerProvider = provider5;
        this.visualStabilityProvider = provider6;
        this.wakefulnessLifecycleProvider = provider7;
    }

    public VisualStabilityCoordinator get() {
        return newInstance(this.delayableExecutorProvider.get(), this.dumpManagerProvider.get(), this.headsUpManagerProvider.get(), this.notifPanelEventsProvider.get(), this.statusBarStateControllerProvider.get(), this.visualStabilityProvider.get(), this.wakefulnessLifecycleProvider.get());
    }

    public static VisualStabilityCoordinator_Factory create(Provider<DelayableExecutor> provider, Provider<DumpManager> provider2, Provider<HeadsUpManager> provider3, Provider<NotifPanelEvents> provider4, Provider<StatusBarStateController> provider5, Provider<VisualStabilityProvider> provider6, Provider<WakefulnessLifecycle> provider7) {
        return new VisualStabilityCoordinator_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7);
    }

    public static VisualStabilityCoordinator newInstance(DelayableExecutor delayableExecutor, DumpManager dumpManager, HeadsUpManager headsUpManager, NotifPanelEvents notifPanelEvents, StatusBarStateController statusBarStateController, VisualStabilityProvider visualStabilityProvider2, WakefulnessLifecycle wakefulnessLifecycle) {
        return new VisualStabilityCoordinator(delayableExecutor, dumpManager, headsUpManager, notifPanelEvents, statusBarStateController, visualStabilityProvider2, wakefulnessLifecycle);
    }
}
