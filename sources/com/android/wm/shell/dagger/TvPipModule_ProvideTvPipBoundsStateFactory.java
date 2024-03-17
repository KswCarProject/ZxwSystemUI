package com.android.wm.shell.dagger;

import android.content.Context;
import com.android.wm.shell.pip.tv.TvPipBoundsState;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class TvPipModule_ProvideTvPipBoundsStateFactory implements Factory<TvPipBoundsState> {
    public final Provider<Context> contextProvider;

    public TvPipModule_ProvideTvPipBoundsStateFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public TvPipBoundsState get() {
        return provideTvPipBoundsState(this.contextProvider.get());
    }

    public static TvPipModule_ProvideTvPipBoundsStateFactory create(Provider<Context> provider) {
        return new TvPipModule_ProvideTvPipBoundsStateFactory(provider);
    }

    public static TvPipBoundsState provideTvPipBoundsState(Context context) {
        return (TvPipBoundsState) Preconditions.checkNotNullFromProvides(TvPipModule.provideTvPipBoundsState(context));
    }
}
