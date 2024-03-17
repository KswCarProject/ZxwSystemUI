package com.android.systemui.statusbar.phone;

import android.content.Context;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class StatusBarContentInsetsProvider_Factory implements Factory<StatusBarContentInsetsProvider> {
    public final Provider<ConfigurationController> configurationControllerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DumpManager> dumpManagerProvider;

    public StatusBarContentInsetsProvider_Factory(Provider<Context> provider, Provider<ConfigurationController> provider2, Provider<DumpManager> provider3) {
        this.contextProvider = provider;
        this.configurationControllerProvider = provider2;
        this.dumpManagerProvider = provider3;
    }

    public StatusBarContentInsetsProvider get() {
        return newInstance(this.contextProvider.get(), this.configurationControllerProvider.get(), this.dumpManagerProvider.get());
    }

    public static StatusBarContentInsetsProvider_Factory create(Provider<Context> provider, Provider<ConfigurationController> provider2, Provider<DumpManager> provider3) {
        return new StatusBarContentInsetsProvider_Factory(provider, provider2, provider3);
    }

    public static StatusBarContentInsetsProvider newInstance(Context context, ConfigurationController configurationController, DumpManager dumpManager) {
        return new StatusBarContentInsetsProvider(context, configurationController, dumpManager);
    }
}
