package com.android.systemui.dagger;

import android.content.Context;
import com.android.systemui.recents.Recents;
import com.android.systemui.recents.RecentsImplementation;
import com.android.systemui.statusbar.CommandQueue;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class ReferenceSystemUIModule_ProvideRecentsFactory implements Factory<Recents> {
    public final Provider<CommandQueue> commandQueueProvider;
    public final Provider<Context> contextProvider;
    public final Provider<RecentsImplementation> recentsImplementationProvider;

    public ReferenceSystemUIModule_ProvideRecentsFactory(Provider<Context> provider, Provider<RecentsImplementation> provider2, Provider<CommandQueue> provider3) {
        this.contextProvider = provider;
        this.recentsImplementationProvider = provider2;
        this.commandQueueProvider = provider3;
    }

    public Recents get() {
        return provideRecents(this.contextProvider.get(), this.recentsImplementationProvider.get(), this.commandQueueProvider.get());
    }

    public static ReferenceSystemUIModule_ProvideRecentsFactory create(Provider<Context> provider, Provider<RecentsImplementation> provider2, Provider<CommandQueue> provider3) {
        return new ReferenceSystemUIModule_ProvideRecentsFactory(provider, provider2, provider3);
    }

    public static Recents provideRecents(Context context, RecentsImplementation recentsImplementation, CommandQueue commandQueue) {
        return (Recents) Preconditions.checkNotNullFromProvides(ReferenceSystemUIModule.provideRecents(context, recentsImplementation, commandQueue));
    }
}
