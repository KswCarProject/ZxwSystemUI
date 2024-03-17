package com.android.systemui.dagger;

import com.android.systemui.ActivityStarterDelegate;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.PluginDependencyProvider;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class PluginModule_ProvideActivityStarterFactory implements Factory<ActivityStarter> {
    public final Provider<ActivityStarterDelegate> delegateProvider;
    public final Provider<PluginDependencyProvider> dependencyProvider;

    public PluginModule_ProvideActivityStarterFactory(Provider<ActivityStarterDelegate> provider, Provider<PluginDependencyProvider> provider2) {
        this.delegateProvider = provider;
        this.dependencyProvider = provider2;
    }

    public ActivityStarter get() {
        return provideActivityStarter(this.delegateProvider.get(), this.dependencyProvider.get());
    }

    public static PluginModule_ProvideActivityStarterFactory create(Provider<ActivityStarterDelegate> provider, Provider<PluginDependencyProvider> provider2) {
        return new PluginModule_ProvideActivityStarterFactory(provider, provider2);
    }

    public static ActivityStarter provideActivityStarter(ActivityStarterDelegate activityStarterDelegate, PluginDependencyProvider pluginDependencyProvider) {
        return (ActivityStarter) Preconditions.checkNotNullFromProvides(PluginModule.provideActivityStarter(activityStarterDelegate, pluginDependencyProvider));
    }
}
