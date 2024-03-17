package com.android.wm.shell.dagger;

import android.content.Context;
import com.android.wm.shell.RootTaskDisplayAreaOrganizer;
import com.android.wm.shell.common.ShellExecutor;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class WMShellBaseModule_ProvideRootTaskDisplayAreaOrganizerFactory implements Factory<RootTaskDisplayAreaOrganizer> {
    public final Provider<Context> contextProvider;
    public final Provider<ShellExecutor> mainExecutorProvider;

    public WMShellBaseModule_ProvideRootTaskDisplayAreaOrganizerFactory(Provider<ShellExecutor> provider, Provider<Context> provider2) {
        this.mainExecutorProvider = provider;
        this.contextProvider = provider2;
    }

    public RootTaskDisplayAreaOrganizer get() {
        return provideRootTaskDisplayAreaOrganizer(this.mainExecutorProvider.get(), this.contextProvider.get());
    }

    public static WMShellBaseModule_ProvideRootTaskDisplayAreaOrganizerFactory create(Provider<ShellExecutor> provider, Provider<Context> provider2) {
        return new WMShellBaseModule_ProvideRootTaskDisplayAreaOrganizerFactory(provider, provider2);
    }

    public static RootTaskDisplayAreaOrganizer provideRootTaskDisplayAreaOrganizer(ShellExecutor shellExecutor, Context context) {
        return (RootTaskDisplayAreaOrganizer) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideRootTaskDisplayAreaOrganizer(shellExecutor, context));
    }
}
