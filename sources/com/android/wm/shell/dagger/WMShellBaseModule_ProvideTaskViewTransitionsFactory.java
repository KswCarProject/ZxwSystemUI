package com.android.wm.shell.dagger;

import com.android.wm.shell.TaskViewTransitions;
import com.android.wm.shell.transition.Transitions;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class WMShellBaseModule_ProvideTaskViewTransitionsFactory implements Factory<TaskViewTransitions> {
    public final Provider<Transitions> transitionsProvider;

    public WMShellBaseModule_ProvideTaskViewTransitionsFactory(Provider<Transitions> provider) {
        this.transitionsProvider = provider;
    }

    public TaskViewTransitions get() {
        return provideTaskViewTransitions(this.transitionsProvider.get());
    }

    public static WMShellBaseModule_ProvideTaskViewTransitionsFactory create(Provider<Transitions> provider) {
        return new WMShellBaseModule_ProvideTaskViewTransitionsFactory(provider);
    }

    public static TaskViewTransitions provideTaskViewTransitions(Transitions transitions) {
        return (TaskViewTransitions) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideTaskViewTransitions(transitions));
    }
}
