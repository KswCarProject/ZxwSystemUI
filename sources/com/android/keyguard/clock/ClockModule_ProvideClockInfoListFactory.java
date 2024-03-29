package com.android.keyguard.clock;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.List;
import javax.inject.Provider;

public final class ClockModule_ProvideClockInfoListFactory implements Factory<List<ClockInfo>> {
    public final Provider<ClockManager> clockManagerProvider;

    public ClockModule_ProvideClockInfoListFactory(Provider<ClockManager> provider) {
        this.clockManagerProvider = provider;
    }

    public List<ClockInfo> get() {
        return provideClockInfoList(this.clockManagerProvider.get());
    }

    public static ClockModule_ProvideClockInfoListFactory create(Provider<ClockManager> provider) {
        return new ClockModule_ProvideClockInfoListFactory(provider);
    }

    public static List<ClockInfo> provideClockInfoList(ClockManager clockManager) {
        return (List) Preconditions.checkNotNullFromProvides(ClockModule.provideClockInfoList(clockManager));
    }
}
