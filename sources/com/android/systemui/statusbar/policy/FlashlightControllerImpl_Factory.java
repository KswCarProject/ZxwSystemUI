package com.android.systemui.statusbar.policy;

import android.content.Context;
import com.android.systemui.dump.DumpManager;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class FlashlightControllerImpl_Factory implements Factory<FlashlightControllerImpl> {
    public final Provider<Context> contextProvider;
    public final Provider<DumpManager> dumpManagerProvider;

    public FlashlightControllerImpl_Factory(Provider<Context> provider, Provider<DumpManager> provider2) {
        this.contextProvider = provider;
        this.dumpManagerProvider = provider2;
    }

    public FlashlightControllerImpl get() {
        return newInstance(this.contextProvider.get(), this.dumpManagerProvider.get());
    }

    public static FlashlightControllerImpl_Factory create(Provider<Context> provider, Provider<DumpManager> provider2) {
        return new FlashlightControllerImpl_Factory(provider, provider2);
    }

    public static FlashlightControllerImpl newInstance(Context context, DumpManager dumpManager) {
        return new FlashlightControllerImpl(context, dumpManager);
    }
}
