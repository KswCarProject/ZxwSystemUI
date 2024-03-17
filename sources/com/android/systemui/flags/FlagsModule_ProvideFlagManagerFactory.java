package com.android.systemui.flags;

import android.content.Context;
import android.os.Handler;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class FlagsModule_ProvideFlagManagerFactory implements Factory<FlagManager> {
    public final Provider<Context> contextProvider;
    public final Provider<Handler> handlerProvider;

    public FlagsModule_ProvideFlagManagerFactory(Provider<Context> provider, Provider<Handler> provider2) {
        this.contextProvider = provider;
        this.handlerProvider = provider2;
    }

    public FlagManager get() {
        return provideFlagManager(this.contextProvider.get(), this.handlerProvider.get());
    }

    public static FlagsModule_ProvideFlagManagerFactory create(Provider<Context> provider, Provider<Handler> provider2) {
        return new FlagsModule_ProvideFlagManagerFactory(provider, provider2);
    }

    public static FlagManager provideFlagManager(Context context, Handler handler) {
        return (FlagManager) Preconditions.checkNotNullFromProvides(FlagsModule.provideFlagManager(context, handler));
    }
}
