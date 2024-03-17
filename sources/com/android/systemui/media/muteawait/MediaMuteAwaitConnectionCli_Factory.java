package com.android.systemui.media.muteawait;

import android.content.Context;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class MediaMuteAwaitConnectionCli_Factory implements Factory<MediaMuteAwaitConnectionCli> {
    public final Provider<CommandRegistry> commandRegistryProvider;
    public final Provider<Context> contextProvider;

    public MediaMuteAwaitConnectionCli_Factory(Provider<CommandRegistry> provider, Provider<Context> provider2) {
        this.commandRegistryProvider = provider;
        this.contextProvider = provider2;
    }

    public MediaMuteAwaitConnectionCli get() {
        return newInstance(this.commandRegistryProvider.get(), this.contextProvider.get());
    }

    public static MediaMuteAwaitConnectionCli_Factory create(Provider<CommandRegistry> provider, Provider<Context> provider2) {
        return new MediaMuteAwaitConnectionCli_Factory(provider, provider2);
    }

    public static MediaMuteAwaitConnectionCli newInstance(CommandRegistry commandRegistry, Context context) {
        return new MediaMuteAwaitConnectionCli(commandRegistry, context);
    }
}
