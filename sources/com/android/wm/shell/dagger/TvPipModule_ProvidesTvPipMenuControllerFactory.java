package com.android.wm.shell.dagger;

import android.content.Context;
import android.os.Handler;
import com.android.wm.shell.common.SystemWindows;
import com.android.wm.shell.pip.PipMediaController;
import com.android.wm.shell.pip.tv.TvPipBoundsState;
import com.android.wm.shell.pip.tv.TvPipMenuController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class TvPipModule_ProvidesTvPipMenuControllerFactory implements Factory<TvPipMenuController> {
    public final Provider<Context> contextProvider;
    public final Provider<Handler> mainHandlerProvider;
    public final Provider<PipMediaController> pipMediaControllerProvider;
    public final Provider<SystemWindows> systemWindowsProvider;
    public final Provider<TvPipBoundsState> tvPipBoundsStateProvider;

    public TvPipModule_ProvidesTvPipMenuControllerFactory(Provider<Context> provider, Provider<TvPipBoundsState> provider2, Provider<SystemWindows> provider3, Provider<PipMediaController> provider4, Provider<Handler> provider5) {
        this.contextProvider = provider;
        this.tvPipBoundsStateProvider = provider2;
        this.systemWindowsProvider = provider3;
        this.pipMediaControllerProvider = provider4;
        this.mainHandlerProvider = provider5;
    }

    public TvPipMenuController get() {
        return providesTvPipMenuController(this.contextProvider.get(), this.tvPipBoundsStateProvider.get(), this.systemWindowsProvider.get(), this.pipMediaControllerProvider.get(), this.mainHandlerProvider.get());
    }

    public static TvPipModule_ProvidesTvPipMenuControllerFactory create(Provider<Context> provider, Provider<TvPipBoundsState> provider2, Provider<SystemWindows> provider3, Provider<PipMediaController> provider4, Provider<Handler> provider5) {
        return new TvPipModule_ProvidesTvPipMenuControllerFactory(provider, provider2, provider3, provider4, provider5);
    }

    public static TvPipMenuController providesTvPipMenuController(Context context, TvPipBoundsState tvPipBoundsState, SystemWindows systemWindows, PipMediaController pipMediaController, Handler handler) {
        return (TvPipMenuController) Preconditions.checkNotNullFromProvides(TvPipModule.providesTvPipMenuController(context, tvPipBoundsState, systemWindows, pipMediaController, handler));
    }
}
