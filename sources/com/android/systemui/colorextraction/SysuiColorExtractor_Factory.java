package com.android.systemui.colorextraction;

import android.content.Context;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class SysuiColorExtractor_Factory implements Factory<SysuiColorExtractor> {
    public final Provider<ConfigurationController> configurationControllerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DumpManager> dumpManagerProvider;

    public SysuiColorExtractor_Factory(Provider<Context> provider, Provider<ConfigurationController> provider2, Provider<DumpManager> provider3) {
        this.contextProvider = provider;
        this.configurationControllerProvider = provider2;
        this.dumpManagerProvider = provider3;
    }

    public SysuiColorExtractor get() {
        return newInstance(this.contextProvider.get(), this.configurationControllerProvider.get(), this.dumpManagerProvider.get());
    }

    public static SysuiColorExtractor_Factory create(Provider<Context> provider, Provider<ConfigurationController> provider2, Provider<DumpManager> provider3) {
        return new SysuiColorExtractor_Factory(provider, provider2, provider3);
    }

    public static SysuiColorExtractor newInstance(Context context, ConfigurationController configurationController, DumpManager dumpManager) {
        return new SysuiColorExtractor(context, configurationController, dumpManager);
    }
}
