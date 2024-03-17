package com.android.systemui.statusbar.policy;

import android.content.Context;
import com.android.systemui.statusbar.CommandQueue;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class RemoteInputQuickSettingsDisabler_Factory implements Factory<RemoteInputQuickSettingsDisabler> {
    public final Provider<CommandQueue> commandQueueProvider;
    public final Provider<ConfigurationController> configControllerProvider;
    public final Provider<Context> contextProvider;

    public RemoteInputQuickSettingsDisabler_Factory(Provider<Context> provider, Provider<CommandQueue> provider2, Provider<ConfigurationController> provider3) {
        this.contextProvider = provider;
        this.commandQueueProvider = provider2;
        this.configControllerProvider = provider3;
    }

    public RemoteInputQuickSettingsDisabler get() {
        return newInstance(this.contextProvider.get(), this.commandQueueProvider.get(), this.configControllerProvider.get());
    }

    public static RemoteInputQuickSettingsDisabler_Factory create(Provider<Context> provider, Provider<CommandQueue> provider2, Provider<ConfigurationController> provider3) {
        return new RemoteInputQuickSettingsDisabler_Factory(provider, provider2, provider3);
    }

    public static RemoteInputQuickSettingsDisabler newInstance(Context context, CommandQueue commandQueue, ConfigurationController configurationController) {
        return new RemoteInputQuickSettingsDisabler(context, commandQueue, configurationController);
    }
}
