package com.android.systemui.flags;

import android.content.Context;
import android.content.res.Resources;
import com.android.internal.statusbar.IStatusBarService;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import com.android.systemui.util.settings.SecureSettings;
import dagger.internal.Factory;
import java.util.Map;
import javax.inject.Provider;

public final class FeatureFlagsDebug_Factory implements Factory<FeatureFlagsDebug> {
    public final Provider<Map<Integer, Flag<?>>> allFlagsProvider;
    public final Provider<IStatusBarService> barServiceProvider;
    public final Provider<CommandRegistry> commandRegistryProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<FlagManager> flagManagerProvider;
    public final Provider<Resources> resourcesProvider;
    public final Provider<SecureSettings> secureSettingsProvider;
    public final Provider<SystemPropertiesHelper> systemPropertiesProvider;

    public FeatureFlagsDebug_Factory(Provider<FlagManager> provider, Provider<Context> provider2, Provider<SecureSettings> provider3, Provider<SystemPropertiesHelper> provider4, Provider<Resources> provider5, Provider<DumpManager> provider6, Provider<Map<Integer, Flag<?>>> provider7, Provider<CommandRegistry> provider8, Provider<IStatusBarService> provider9) {
        this.flagManagerProvider = provider;
        this.contextProvider = provider2;
        this.secureSettingsProvider = provider3;
        this.systemPropertiesProvider = provider4;
        this.resourcesProvider = provider5;
        this.dumpManagerProvider = provider6;
        this.allFlagsProvider = provider7;
        this.commandRegistryProvider = provider8;
        this.barServiceProvider = provider9;
    }

    public FeatureFlagsDebug get() {
        return newInstance(this.flagManagerProvider.get(), this.contextProvider.get(), this.secureSettingsProvider.get(), this.systemPropertiesProvider.get(), this.resourcesProvider.get(), this.dumpManagerProvider.get(), this.allFlagsProvider.get(), this.commandRegistryProvider.get(), this.barServiceProvider.get());
    }

    public static FeatureFlagsDebug_Factory create(Provider<FlagManager> provider, Provider<Context> provider2, Provider<SecureSettings> provider3, Provider<SystemPropertiesHelper> provider4, Provider<Resources> provider5, Provider<DumpManager> provider6, Provider<Map<Integer, Flag<?>>> provider7, Provider<CommandRegistry> provider8, Provider<IStatusBarService> provider9) {
        return new FeatureFlagsDebug_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9);
    }

    public static FeatureFlagsDebug newInstance(FlagManager flagManager, Context context, SecureSettings secureSettings, SystemPropertiesHelper systemPropertiesHelper, Resources resources, DumpManager dumpManager, Map<Integer, Flag<?>> map, CommandRegistry commandRegistry, IStatusBarService iStatusBarService) {
        return new FeatureFlagsDebug(flagManager, context, secureSettings, systemPropertiesHelper, resources, dumpManager, map, commandRegistry, iStatusBarService);
    }
}
