package com.android.systemui.statusbar.tv.notifications;

import android.content.Context;
import com.android.systemui.statusbar.CommandQueue;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class TvNotificationPanel_Factory implements Factory<TvNotificationPanel> {
    public final Provider<CommandQueue> commandQueueProvider;
    public final Provider<Context> contextProvider;

    public TvNotificationPanel_Factory(Provider<Context> provider, Provider<CommandQueue> provider2) {
        this.contextProvider = provider;
        this.commandQueueProvider = provider2;
    }

    public TvNotificationPanel get() {
        return newInstance(this.contextProvider.get(), this.commandQueueProvider.get());
    }

    public static TvNotificationPanel_Factory create(Provider<Context> provider, Provider<CommandQueue> provider2) {
        return new TvNotificationPanel_Factory(provider, provider2);
    }

    public static TvNotificationPanel newInstance(Context context, CommandQueue commandQueue) {
        return new TvNotificationPanel(context, commandQueue);
    }
}
