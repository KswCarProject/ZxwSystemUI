package com.android.systemui.statusbar.phone;

import android.content.Context;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.phone.LightBarTransitionsController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class DarkIconDispatcherImpl_Factory implements Factory<DarkIconDispatcherImpl> {
    public final Provider<Context> contextProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<LightBarTransitionsController.Factory> lightBarTransitionsControllerFactoryProvider;

    public DarkIconDispatcherImpl_Factory(Provider<Context> provider, Provider<LightBarTransitionsController.Factory> provider2, Provider<DumpManager> provider3) {
        this.contextProvider = provider;
        this.lightBarTransitionsControllerFactoryProvider = provider2;
        this.dumpManagerProvider = provider3;
    }

    public DarkIconDispatcherImpl get() {
        return newInstance(this.contextProvider.get(), this.lightBarTransitionsControllerFactoryProvider.get(), this.dumpManagerProvider.get());
    }

    public static DarkIconDispatcherImpl_Factory create(Provider<Context> provider, Provider<LightBarTransitionsController.Factory> provider2, Provider<DumpManager> provider3) {
        return new DarkIconDispatcherImpl_Factory(provider, provider2, provider3);
    }

    public static DarkIconDispatcherImpl newInstance(Context context, LightBarTransitionsController.Factory factory, DumpManager dumpManager) {
        return new DarkIconDispatcherImpl(context, factory, dumpManager);
    }
}
