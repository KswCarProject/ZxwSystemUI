package com.android.systemui.plugins;

import com.android.systemui.util.concurrency.ThreadFactory;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class PluginsModule_ProvidesPluginExecutorFactory implements Factory<Executor> {
    private final Provider<ThreadFactory> threadFactoryProvider;

    public PluginsModule_ProvidesPluginExecutorFactory(Provider<ThreadFactory> provider) {
        this.threadFactoryProvider = provider;
    }

    public Executor get() {
        return providesPluginExecutor(this.threadFactoryProvider.get());
    }

    public static PluginsModule_ProvidesPluginExecutorFactory create(Provider<ThreadFactory> provider) {
        return new PluginsModule_ProvidesPluginExecutorFactory(provider);
    }

    public static Executor providesPluginExecutor(ThreadFactory threadFactory) {
        return (Executor) Preconditions.checkNotNullFromProvides(PluginsModule.providesPluginExecutor(threadFactory));
    }
}
