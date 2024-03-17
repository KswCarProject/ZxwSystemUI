package com.android.systemui.statusbar.dagger;

import android.content.Context;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import com.android.systemui.tracing.ProtoTracer;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class CentralSurfacesDependenciesModule_ProvideCommandQueueFactory implements Factory<CommandQueue> {
    public final Provider<Context> contextProvider;
    public final Provider<ProtoTracer> protoTracerProvider;
    public final Provider<CommandRegistry> registryProvider;

    public CentralSurfacesDependenciesModule_ProvideCommandQueueFactory(Provider<Context> provider, Provider<ProtoTracer> provider2, Provider<CommandRegistry> provider3) {
        this.contextProvider = provider;
        this.protoTracerProvider = provider2;
        this.registryProvider = provider3;
    }

    public CommandQueue get() {
        return provideCommandQueue(this.contextProvider.get(), this.protoTracerProvider.get(), this.registryProvider.get());
    }

    public static CentralSurfacesDependenciesModule_ProvideCommandQueueFactory create(Provider<Context> provider, Provider<ProtoTracer> provider2, Provider<CommandRegistry> provider3) {
        return new CentralSurfacesDependenciesModule_ProvideCommandQueueFactory(provider, provider2, provider3);
    }

    public static CommandQueue provideCommandQueue(Context context, ProtoTracer protoTracer, CommandRegistry commandRegistry) {
        return (CommandQueue) Preconditions.checkNotNullFromProvides(CentralSurfacesDependenciesModule.provideCommandQueue(context, protoTracer, commandRegistry));
    }
}
