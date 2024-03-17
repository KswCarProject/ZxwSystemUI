package com.android.systemui.unfold;

import android.content.Context;
import com.android.systemui.unfold.config.UnfoldTransitionConfig;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class UnfoldTransitionModule_ProvideUnfoldTransitionConfigFactory implements Factory<UnfoldTransitionConfig> {
    public final Provider<Context> contextProvider;
    public final UnfoldTransitionModule module;

    public UnfoldTransitionModule_ProvideUnfoldTransitionConfigFactory(UnfoldTransitionModule unfoldTransitionModule, Provider<Context> provider) {
        this.module = unfoldTransitionModule;
        this.contextProvider = provider;
    }

    public UnfoldTransitionConfig get() {
        return provideUnfoldTransitionConfig(this.module, this.contextProvider.get());
    }

    public static UnfoldTransitionModule_ProvideUnfoldTransitionConfigFactory create(UnfoldTransitionModule unfoldTransitionModule, Provider<Context> provider) {
        return new UnfoldTransitionModule_ProvideUnfoldTransitionConfigFactory(unfoldTransitionModule, provider);
    }

    public static UnfoldTransitionConfig provideUnfoldTransitionConfig(UnfoldTransitionModule unfoldTransitionModule, Context context) {
        return (UnfoldTransitionConfig) Preconditions.checkNotNullFromProvides(unfoldTransitionModule.provideUnfoldTransitionConfig(context));
    }
}
