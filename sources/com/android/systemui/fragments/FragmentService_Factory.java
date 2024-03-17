package com.android.systemui.fragments;

import com.android.systemui.dump.DumpManager;
import com.android.systemui.fragments.FragmentService;
import com.android.systemui.statusbar.policy.ConfigurationController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class FragmentService_Factory implements Factory<FragmentService> {
    public final Provider<ConfigurationController> configurationControllerProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<FragmentService.FragmentCreator.Factory> fragmentCreatorFactoryProvider;

    public FragmentService_Factory(Provider<FragmentService.FragmentCreator.Factory> provider, Provider<ConfigurationController> provider2, Provider<DumpManager> provider3) {
        this.fragmentCreatorFactoryProvider = provider;
        this.configurationControllerProvider = provider2;
        this.dumpManagerProvider = provider3;
    }

    public FragmentService get() {
        return newInstance(this.fragmentCreatorFactoryProvider.get(), this.configurationControllerProvider.get(), this.dumpManagerProvider.get());
    }

    public static FragmentService_Factory create(Provider<FragmentService.FragmentCreator.Factory> provider, Provider<ConfigurationController> provider2, Provider<DumpManager> provider3) {
        return new FragmentService_Factory(provider, provider2, provider3);
    }

    public static FragmentService newInstance(FragmentService.FragmentCreator.Factory factory, ConfigurationController configurationController, DumpManager dumpManager) {
        return new FragmentService(factory, configurationController, dumpManager);
    }
}
