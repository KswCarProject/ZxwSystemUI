package com.android.systemui.statusbar.notification.collection;

import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.NotificationInteractionTracker;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.collection.listbuilder.ShadeListBuilderLogger;
import com.android.systemui.util.time.SystemClock;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ShadeListBuilder_Factory implements Factory<ShadeListBuilder> {
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<NotifPipelineFlags> flagsProvider;
    public final Provider<NotificationInteractionTracker> interactionTrackerProvider;
    public final Provider<ShadeListBuilderLogger> loggerProvider;
    public final Provider<NotifPipelineChoreographer> pipelineChoreographerProvider;
    public final Provider<SystemClock> systemClockProvider;

    public ShadeListBuilder_Factory(Provider<DumpManager> provider, Provider<NotifPipelineChoreographer> provider2, Provider<NotifPipelineFlags> provider3, Provider<NotificationInteractionTracker> provider4, Provider<ShadeListBuilderLogger> provider5, Provider<SystemClock> provider6) {
        this.dumpManagerProvider = provider;
        this.pipelineChoreographerProvider = provider2;
        this.flagsProvider = provider3;
        this.interactionTrackerProvider = provider4;
        this.loggerProvider = provider5;
        this.systemClockProvider = provider6;
    }

    public ShadeListBuilder get() {
        return newInstance(this.dumpManagerProvider.get(), this.pipelineChoreographerProvider.get(), this.flagsProvider.get(), this.interactionTrackerProvider.get(), this.loggerProvider.get(), this.systemClockProvider.get());
    }

    public static ShadeListBuilder_Factory create(Provider<DumpManager> provider, Provider<NotifPipelineChoreographer> provider2, Provider<NotifPipelineFlags> provider3, Provider<NotificationInteractionTracker> provider4, Provider<ShadeListBuilderLogger> provider5, Provider<SystemClock> provider6) {
        return new ShadeListBuilder_Factory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static ShadeListBuilder newInstance(DumpManager dumpManager, NotifPipelineChoreographer notifPipelineChoreographer, NotifPipelineFlags notifPipelineFlags, NotificationInteractionTracker notificationInteractionTracker, ShadeListBuilderLogger shadeListBuilderLogger, SystemClock systemClock) {
        return new ShadeListBuilder(dumpManager, notifPipelineChoreographer, notifPipelineFlags, notificationInteractionTracker, shadeListBuilderLogger, systemClock);
    }
}
