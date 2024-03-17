package com.android.systemui.biometrics;

import com.android.systemui.statusbar.commandline.CommandRegistry;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class UdfpsShell_Factory implements Factory<UdfpsShell> {
    public final Provider<CommandRegistry> commandRegistryProvider;

    public UdfpsShell_Factory(Provider<CommandRegistry> provider) {
        this.commandRegistryProvider = provider;
    }

    public UdfpsShell get() {
        return newInstance(this.commandRegistryProvider.get());
    }

    public static UdfpsShell_Factory create(Provider<CommandRegistry> provider) {
        return new UdfpsShell_Factory(provider);
    }

    public static UdfpsShell newInstance(CommandRegistry commandRegistry) {
        return new UdfpsShell(commandRegistry);
    }
}
