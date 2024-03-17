package com.android.systemui.statusbar.tv;

import android.content.Context;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.statusbar.CommandQueue;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class TvStatusBar_Factory implements Factory<TvStatusBar> {
    public final Provider<AssistManager> assistManagerLazyProvider;
    public final Provider<CommandQueue> commandQueueProvider;
    public final Provider<Context> contextProvider;

    public TvStatusBar_Factory(Provider<Context> provider, Provider<CommandQueue> provider2, Provider<AssistManager> provider3) {
        this.contextProvider = provider;
        this.commandQueueProvider = provider2;
        this.assistManagerLazyProvider = provider3;
    }

    public TvStatusBar get() {
        return newInstance(this.contextProvider.get(), this.commandQueueProvider.get(), DoubleCheck.lazy(this.assistManagerLazyProvider));
    }

    public static TvStatusBar_Factory create(Provider<Context> provider, Provider<CommandQueue> provider2, Provider<AssistManager> provider3) {
        return new TvStatusBar_Factory(provider, provider2, provider3);
    }

    public static TvStatusBar newInstance(Context context, CommandQueue commandQueue, Lazy<AssistManager> lazy) {
        return new TvStatusBar(context, commandQueue, lazy);
    }
}
