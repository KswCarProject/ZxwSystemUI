package com.android.systemui.classifier;

import com.android.systemui.util.time.SystemClock;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class HistoryTracker_Factory implements Factory<HistoryTracker> {
    public final Provider<SystemClock> systemClockProvider;

    public HistoryTracker_Factory(Provider<SystemClock> provider) {
        this.systemClockProvider = provider;
    }

    public HistoryTracker get() {
        return newInstance(this.systemClockProvider.get());
    }

    public static HistoryTracker_Factory create(Provider<SystemClock> provider) {
        return new HistoryTracker_Factory(provider);
    }

    public static HistoryTracker newInstance(SystemClock systemClock) {
        return new HistoryTracker(systemClock);
    }
}
