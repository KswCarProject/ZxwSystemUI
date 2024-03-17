package com.android.systemui.clipboardoverlay;

import com.android.internal.logging.UiEventLogger;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.broadcast.BroadcastSender;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ClipboardOverlayControllerFactory_Factory implements Factory<ClipboardOverlayControllerFactory> {
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<BroadcastSender> broadcastSenderProvider;
    public final Provider<UiEventLogger> uiEventLoggerProvider;

    public ClipboardOverlayControllerFactory_Factory(Provider<BroadcastDispatcher> provider, Provider<BroadcastSender> provider2, Provider<UiEventLogger> provider3) {
        this.broadcastDispatcherProvider = provider;
        this.broadcastSenderProvider = provider2;
        this.uiEventLoggerProvider = provider3;
    }

    public ClipboardOverlayControllerFactory get() {
        return newInstance(this.broadcastDispatcherProvider.get(), this.broadcastSenderProvider.get(), this.uiEventLoggerProvider.get());
    }

    public static ClipboardOverlayControllerFactory_Factory create(Provider<BroadcastDispatcher> provider, Provider<BroadcastSender> provider2, Provider<UiEventLogger> provider3) {
        return new ClipboardOverlayControllerFactory_Factory(provider, provider2, provider3);
    }

    public static ClipboardOverlayControllerFactory newInstance(BroadcastDispatcher broadcastDispatcher, BroadcastSender broadcastSender, UiEventLogger uiEventLogger) {
        return new ClipboardOverlayControllerFactory(broadcastDispatcher, broadcastSender, uiEventLogger);
    }
}
