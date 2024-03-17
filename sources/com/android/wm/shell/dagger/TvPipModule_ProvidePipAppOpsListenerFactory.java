package com.android.wm.shell.dagger;

import android.content.Context;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.pip.PipAppOpsListener;
import com.android.wm.shell.pip.PipTaskOrganizer;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class TvPipModule_ProvidePipAppOpsListenerFactory implements Factory<PipAppOpsListener> {
    public final Provider<Context> contextProvider;
    public final Provider<ShellExecutor> mainExecutorProvider;
    public final Provider<PipTaskOrganizer> pipTaskOrganizerProvider;

    public TvPipModule_ProvidePipAppOpsListenerFactory(Provider<Context> provider, Provider<PipTaskOrganizer> provider2, Provider<ShellExecutor> provider3) {
        this.contextProvider = provider;
        this.pipTaskOrganizerProvider = provider2;
        this.mainExecutorProvider = provider3;
    }

    public PipAppOpsListener get() {
        return providePipAppOpsListener(this.contextProvider.get(), this.pipTaskOrganizerProvider.get(), this.mainExecutorProvider.get());
    }

    public static TvPipModule_ProvidePipAppOpsListenerFactory create(Provider<Context> provider, Provider<PipTaskOrganizer> provider2, Provider<ShellExecutor> provider3) {
        return new TvPipModule_ProvidePipAppOpsListenerFactory(provider, provider2, provider3);
    }

    public static PipAppOpsListener providePipAppOpsListener(Context context, PipTaskOrganizer pipTaskOrganizer, ShellExecutor shellExecutor) {
        return (PipAppOpsListener) Preconditions.checkNotNullFromProvides(TvPipModule.providePipAppOpsListener(context, pipTaskOrganizer, shellExecutor));
    }
}
