package com.android.wm.shell.dagger;

import com.android.wm.shell.RootDisplayAreaOrganizer;
import com.android.wm.shell.common.ShellExecutor;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class WMShellBaseModule_ProvideRootDisplayAreaOrganizerFactory implements Factory<RootDisplayAreaOrganizer> {
    public final Provider<ShellExecutor> mainExecutorProvider;

    public WMShellBaseModule_ProvideRootDisplayAreaOrganizerFactory(Provider<ShellExecutor> provider) {
        this.mainExecutorProvider = provider;
    }

    public RootDisplayAreaOrganizer get() {
        return provideRootDisplayAreaOrganizer(this.mainExecutorProvider.get());
    }

    public static WMShellBaseModule_ProvideRootDisplayAreaOrganizerFactory create(Provider<ShellExecutor> provider) {
        return new WMShellBaseModule_ProvideRootDisplayAreaOrganizerFactory(provider);
    }

    public static RootDisplayAreaOrganizer provideRootDisplayAreaOrganizer(ShellExecutor shellExecutor) {
        return (RootDisplayAreaOrganizer) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideRootDisplayAreaOrganizer(shellExecutor));
    }
}
