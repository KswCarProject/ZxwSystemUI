package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.os.Handler;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.util.settings.GlobalSettings;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ZenModeControllerImpl_Factory implements Factory<ZenModeControllerImpl> {
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<GlobalSettings> globalSettingsProvider;
    public final Provider<Handler> handlerProvider;

    public ZenModeControllerImpl_Factory(Provider<Context> provider, Provider<Handler> provider2, Provider<BroadcastDispatcher> provider3, Provider<DumpManager> provider4, Provider<GlobalSettings> provider5) {
        this.contextProvider = provider;
        this.handlerProvider = provider2;
        this.broadcastDispatcherProvider = provider3;
        this.dumpManagerProvider = provider4;
        this.globalSettingsProvider = provider5;
    }

    public ZenModeControllerImpl get() {
        return newInstance(this.contextProvider.get(), this.handlerProvider.get(), this.broadcastDispatcherProvider.get(), this.dumpManagerProvider.get(), this.globalSettingsProvider.get());
    }

    public static ZenModeControllerImpl_Factory create(Provider<Context> provider, Provider<Handler> provider2, Provider<BroadcastDispatcher> provider3, Provider<DumpManager> provider4, Provider<GlobalSettings> provider5) {
        return new ZenModeControllerImpl_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static ZenModeControllerImpl newInstance(Context context, Handler handler, BroadcastDispatcher broadcastDispatcher, DumpManager dumpManager, GlobalSettings globalSettings) {
        return new ZenModeControllerImpl(context, handler, broadcastDispatcher, dumpManager, globalSettings);
    }
}
