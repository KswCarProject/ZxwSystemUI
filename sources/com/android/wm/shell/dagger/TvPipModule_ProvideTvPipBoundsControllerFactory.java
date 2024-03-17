package com.android.wm.shell.dagger;

import android.content.Context;
import android.os.Handler;
import com.android.wm.shell.pip.tv.TvPipBoundsAlgorithm;
import com.android.wm.shell.pip.tv.TvPipBoundsController;
import com.android.wm.shell.pip.tv.TvPipBoundsState;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class TvPipModule_ProvideTvPipBoundsControllerFactory implements Factory<TvPipBoundsController> {
    public final Provider<Context> contextProvider;
    public final Provider<Handler> mainHandlerProvider;
    public final Provider<TvPipBoundsAlgorithm> tvPipBoundsAlgorithmProvider;
    public final Provider<TvPipBoundsState> tvPipBoundsStateProvider;

    public TvPipModule_ProvideTvPipBoundsControllerFactory(Provider<Context> provider, Provider<Handler> provider2, Provider<TvPipBoundsState> provider3, Provider<TvPipBoundsAlgorithm> provider4) {
        this.contextProvider = provider;
        this.mainHandlerProvider = provider2;
        this.tvPipBoundsStateProvider = provider3;
        this.tvPipBoundsAlgorithmProvider = provider4;
    }

    public TvPipBoundsController get() {
        return provideTvPipBoundsController(this.contextProvider.get(), this.mainHandlerProvider.get(), this.tvPipBoundsStateProvider.get(), this.tvPipBoundsAlgorithmProvider.get());
    }

    public static TvPipModule_ProvideTvPipBoundsControllerFactory create(Provider<Context> provider, Provider<Handler> provider2, Provider<TvPipBoundsState> provider3, Provider<TvPipBoundsAlgorithm> provider4) {
        return new TvPipModule_ProvideTvPipBoundsControllerFactory(provider, provider2, provider3, provider4);
    }

    public static TvPipBoundsController provideTvPipBoundsController(Context context, Handler handler, TvPipBoundsState tvPipBoundsState, TvPipBoundsAlgorithm tvPipBoundsAlgorithm) {
        return (TvPipBoundsController) Preconditions.checkNotNullFromProvides(TvPipModule.provideTvPipBoundsController(context, handler, tvPipBoundsState, tvPipBoundsAlgorithm));
    }
}
