package com.android.systemui.keyguard;

import com.android.systemui.dump.DumpManager;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ScreenLifecycle_Factory implements Factory<ScreenLifecycle> {
    public final Provider<DumpManager> dumpManagerProvider;

    public ScreenLifecycle_Factory(Provider<DumpManager> provider) {
        this.dumpManagerProvider = provider;
    }

    public ScreenLifecycle get() {
        return newInstance(this.dumpManagerProvider.get());
    }

    public static ScreenLifecycle_Factory create(Provider<DumpManager> provider) {
        return new ScreenLifecycle_Factory(provider);
    }

    public static ScreenLifecycle newInstance(DumpManager dumpManager) {
        return new ScreenLifecycle(dumpManager);
    }
}
