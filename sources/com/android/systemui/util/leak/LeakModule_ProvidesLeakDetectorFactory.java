package com.android.systemui.util.leak;

import com.android.systemui.dump.DumpManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class LeakModule_ProvidesLeakDetectorFactory implements Factory<LeakDetector> {
    public final Provider<TrackedCollections> collectionsProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final LeakModule module;

    public LeakModule_ProvidesLeakDetectorFactory(LeakModule leakModule, Provider<DumpManager> provider, Provider<TrackedCollections> provider2) {
        this.module = leakModule;
        this.dumpManagerProvider = provider;
        this.collectionsProvider = provider2;
    }

    public LeakDetector get() {
        return providesLeakDetector(this.module, this.dumpManagerProvider.get(), this.collectionsProvider.get());
    }

    public static LeakModule_ProvidesLeakDetectorFactory create(LeakModule leakModule, Provider<DumpManager> provider, Provider<TrackedCollections> provider2) {
        return new LeakModule_ProvidesLeakDetectorFactory(leakModule, provider, provider2);
    }

    public static LeakDetector providesLeakDetector(LeakModule leakModule, DumpManager dumpManager, TrackedCollections trackedCollections) {
        return (LeakDetector) Preconditions.checkNotNullFromProvides(leakModule.providesLeakDetector(dumpManager, trackedCollections));
    }
}
