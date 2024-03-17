package com.android.systemui.statusbar;

import com.android.internal.jank.InteractionJankMonitor;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.dump.DumpManager;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class StatusBarStateControllerImpl_Factory implements Factory<StatusBarStateControllerImpl> {
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<InteractionJankMonitor> interactionJankMonitorProvider;
    public final Provider<UiEventLogger> uiEventLoggerProvider;

    public StatusBarStateControllerImpl_Factory(Provider<UiEventLogger> provider, Provider<DumpManager> provider2, Provider<InteractionJankMonitor> provider3) {
        this.uiEventLoggerProvider = provider;
        this.dumpManagerProvider = provider2;
        this.interactionJankMonitorProvider = provider3;
    }

    public StatusBarStateControllerImpl get() {
        return newInstance(this.uiEventLoggerProvider.get(), this.dumpManagerProvider.get(), this.interactionJankMonitorProvider.get());
    }

    public static StatusBarStateControllerImpl_Factory create(Provider<UiEventLogger> provider, Provider<DumpManager> provider2, Provider<InteractionJankMonitor> provider3) {
        return new StatusBarStateControllerImpl_Factory(provider, provider2, provider3);
    }

    public static StatusBarStateControllerImpl newInstance(UiEventLogger uiEventLogger, DumpManager dumpManager, InteractionJankMonitor interactionJankMonitor) {
        return new StatusBarStateControllerImpl(uiEventLogger, dumpManager, interactionJankMonitor);
    }
}
