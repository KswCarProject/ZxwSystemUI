package com.android.systemui.statusbar.notification.collection.provider;

import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class DebugModeFilterProvider_Factory implements Factory<DebugModeFilterProvider> {
    public final Provider<CommandRegistry> commandRegistryProvider;
    public final Provider<DumpManager> dumpManagerProvider;

    public DebugModeFilterProvider_Factory(Provider<CommandRegistry> provider, Provider<DumpManager> provider2) {
        this.commandRegistryProvider = provider;
        this.dumpManagerProvider = provider2;
    }

    public DebugModeFilterProvider get() {
        return newInstance(this.commandRegistryProvider.get(), this.dumpManagerProvider.get());
    }

    public static DebugModeFilterProvider_Factory create(Provider<CommandRegistry> provider, Provider<DumpManager> provider2) {
        return new DebugModeFilterProvider_Factory(provider, provider2);
    }

    public static DebugModeFilterProvider newInstance(CommandRegistry commandRegistry, DumpManager dumpManager) {
        return new DebugModeFilterProvider(commandRegistry, dumpManager);
    }
}
