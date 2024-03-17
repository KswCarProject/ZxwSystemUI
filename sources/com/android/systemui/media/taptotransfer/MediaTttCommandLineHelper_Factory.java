package com.android.systemui.media.taptotransfer;

import android.content.Context;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class MediaTttCommandLineHelper_Factory implements Factory<MediaTttCommandLineHelper> {
    public final Provider<CommandRegistry> commandRegistryProvider;
    public final Provider<Context> contextProvider;
    public final Provider<Executor> mainExecutorProvider;

    public MediaTttCommandLineHelper_Factory(Provider<CommandRegistry> provider, Provider<Context> provider2, Provider<Executor> provider3) {
        this.commandRegistryProvider = provider;
        this.contextProvider = provider2;
        this.mainExecutorProvider = provider3;
    }

    public MediaTttCommandLineHelper get() {
        return newInstance(this.commandRegistryProvider.get(), this.contextProvider.get(), this.mainExecutorProvider.get());
    }

    public static MediaTttCommandLineHelper_Factory create(Provider<CommandRegistry> provider, Provider<Context> provider2, Provider<Executor> provider3) {
        return new MediaTttCommandLineHelper_Factory(provider, provider2, provider3);
    }

    public static MediaTttCommandLineHelper newInstance(CommandRegistry commandRegistry, Context context, Executor executor) {
        return new MediaTttCommandLineHelper(commandRegistry, context, executor);
    }
}
