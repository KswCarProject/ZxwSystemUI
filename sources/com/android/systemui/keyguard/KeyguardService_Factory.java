package com.android.systemui.keyguard;

import com.android.wm.shell.transition.ShellTransitions;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class KeyguardService_Factory implements Factory<KeyguardService> {
    public final Provider<KeyguardLifecyclesDispatcher> keyguardLifecyclesDispatcherProvider;
    public final Provider<KeyguardViewMediator> keyguardViewMediatorProvider;
    public final Provider<ShellTransitions> shellTransitionsProvider;

    public KeyguardService_Factory(Provider<KeyguardViewMediator> provider, Provider<KeyguardLifecyclesDispatcher> provider2, Provider<ShellTransitions> provider3) {
        this.keyguardViewMediatorProvider = provider;
        this.keyguardLifecyclesDispatcherProvider = provider2;
        this.shellTransitionsProvider = provider3;
    }

    public KeyguardService get() {
        return newInstance(this.keyguardViewMediatorProvider.get(), this.keyguardLifecyclesDispatcherProvider.get(), this.shellTransitionsProvider.get());
    }

    public static KeyguardService_Factory create(Provider<KeyguardViewMediator> provider, Provider<KeyguardLifecyclesDispatcher> provider2, Provider<ShellTransitions> provider3) {
        return new KeyguardService_Factory(provider, provider2, provider3);
    }

    public static KeyguardService newInstance(KeyguardViewMediator keyguardViewMediator, KeyguardLifecyclesDispatcher keyguardLifecyclesDispatcher, ShellTransitions shellTransitions) {
        return new KeyguardService(keyguardViewMediator, keyguardLifecyclesDispatcher, shellTransitions);
    }
}
