package com.android.systemui.media;

import android.content.Context;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.notification.collection.provider.VisualStabilityProvider;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.time.SystemClock;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class MediaCarouselController_Factory implements Factory<MediaCarouselController> {
    public final Provider<ActivityStarter> activityStarterProvider;
    public final Provider<ConfigurationController> configurationControllerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<MediaCarouselControllerLogger> debugLoggerProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<DelayableExecutor> executorProvider;
    public final Provider<FalsingCollector> falsingCollectorProvider;
    public final Provider<FalsingManager> falsingManagerProvider;
    public final Provider<MediaUiEventLogger> loggerProvider;
    public final Provider<MediaControlPanel> mediaControlPanelFactoryProvider;
    public final Provider<MediaHostStatesManager> mediaHostStatesManagerProvider;
    public final Provider<MediaDataManager> mediaManagerProvider;
    public final Provider<SystemClock> systemClockProvider;
    public final Provider<VisualStabilityProvider> visualStabilityProvider;

    public MediaCarouselController_Factory(Provider<Context> provider, Provider<MediaControlPanel> provider2, Provider<VisualStabilityProvider> provider3, Provider<MediaHostStatesManager> provider4, Provider<ActivityStarter> provider5, Provider<SystemClock> provider6, Provider<DelayableExecutor> provider7, Provider<MediaDataManager> provider8, Provider<ConfigurationController> provider9, Provider<FalsingCollector> provider10, Provider<FalsingManager> provider11, Provider<DumpManager> provider12, Provider<MediaUiEventLogger> provider13, Provider<MediaCarouselControllerLogger> provider14) {
        this.contextProvider = provider;
        this.mediaControlPanelFactoryProvider = provider2;
        this.visualStabilityProvider = provider3;
        this.mediaHostStatesManagerProvider = provider4;
        this.activityStarterProvider = provider5;
        this.systemClockProvider = provider6;
        this.executorProvider = provider7;
        this.mediaManagerProvider = provider8;
        this.configurationControllerProvider = provider9;
        this.falsingCollectorProvider = provider10;
        this.falsingManagerProvider = provider11;
        this.dumpManagerProvider = provider12;
        this.loggerProvider = provider13;
        this.debugLoggerProvider = provider14;
    }

    public MediaCarouselController get() {
        return newInstance(this.contextProvider.get(), this.mediaControlPanelFactoryProvider, this.visualStabilityProvider.get(), this.mediaHostStatesManagerProvider.get(), this.activityStarterProvider.get(), this.systemClockProvider.get(), this.executorProvider.get(), this.mediaManagerProvider.get(), this.configurationControllerProvider.get(), this.falsingCollectorProvider.get(), this.falsingManagerProvider.get(), this.dumpManagerProvider.get(), this.loggerProvider.get(), this.debugLoggerProvider.get());
    }

    public static MediaCarouselController_Factory create(Provider<Context> provider, Provider<MediaControlPanel> provider2, Provider<VisualStabilityProvider> provider3, Provider<MediaHostStatesManager> provider4, Provider<ActivityStarter> provider5, Provider<SystemClock> provider6, Provider<DelayableExecutor> provider7, Provider<MediaDataManager> provider8, Provider<ConfigurationController> provider9, Provider<FalsingCollector> provider10, Provider<FalsingManager> provider11, Provider<DumpManager> provider12, Provider<MediaUiEventLogger> provider13, Provider<MediaCarouselControllerLogger> provider14) {
        return new MediaCarouselController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14);
    }

    public static MediaCarouselController newInstance(Context context, Provider<MediaControlPanel> provider, VisualStabilityProvider visualStabilityProvider2, MediaHostStatesManager mediaHostStatesManager, ActivityStarter activityStarter, SystemClock systemClock, DelayableExecutor delayableExecutor, MediaDataManager mediaDataManager, ConfigurationController configurationController, FalsingCollector falsingCollector, FalsingManager falsingManager, DumpManager dumpManager, MediaUiEventLogger mediaUiEventLogger, MediaCarouselControllerLogger mediaCarouselControllerLogger) {
        return new MediaCarouselController(context, provider, visualStabilityProvider2, mediaHostStatesManager, activityStarter, systemClock, delayableExecutor, mediaDataManager, configurationController, falsingCollector, falsingManager, dumpManager, mediaUiEventLogger, mediaCarouselControllerLogger);
    }
}
