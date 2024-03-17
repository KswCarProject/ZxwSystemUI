package com.android.wm.shell.dagger;

import com.android.wm.shell.pip.PipAnimationController;
import com.android.wm.shell.pip.PipSurfaceTransactionHelper;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class TvPipModule_ProvidePipAnimationControllerFactory implements Factory<PipAnimationController> {
    public final Provider<PipSurfaceTransactionHelper> pipSurfaceTransactionHelperProvider;

    public TvPipModule_ProvidePipAnimationControllerFactory(Provider<PipSurfaceTransactionHelper> provider) {
        this.pipSurfaceTransactionHelperProvider = provider;
    }

    public PipAnimationController get() {
        return providePipAnimationController(this.pipSurfaceTransactionHelperProvider.get());
    }

    public static TvPipModule_ProvidePipAnimationControllerFactory create(Provider<PipSurfaceTransactionHelper> provider) {
        return new TvPipModule_ProvidePipAnimationControllerFactory(provider);
    }

    public static PipAnimationController providePipAnimationController(PipSurfaceTransactionHelper pipSurfaceTransactionHelper) {
        return (PipAnimationController) Preconditions.checkNotNullFromProvides(TvPipModule.providePipAnimationController(pipSurfaceTransactionHelper));
    }
}
