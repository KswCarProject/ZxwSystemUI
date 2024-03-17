package com.android.wm.shell.dagger;

import com.android.wm.shell.transition.ShellTransitions;
import com.android.wm.shell.transition.Transitions;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class WMShellBaseModule_ProvideRemoteTransitionsFactory implements Factory<ShellTransitions> {
    public final Provider<Transitions> transitionsProvider;

    public WMShellBaseModule_ProvideRemoteTransitionsFactory(Provider<Transitions> provider) {
        this.transitionsProvider = provider;
    }

    public ShellTransitions get() {
        return provideRemoteTransitions(this.transitionsProvider.get());
    }

    public static WMShellBaseModule_ProvideRemoteTransitionsFactory create(Provider<Transitions> provider) {
        return new WMShellBaseModule_ProvideRemoteTransitionsFactory(provider);
    }

    public static ShellTransitions provideRemoteTransitions(Transitions transitions) {
        return (ShellTransitions) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideRemoteTransitions(transitions));
    }
}
