package com.android.systemui.doze;

import android.app.AlarmManager;
import android.os.Handler;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class DozePauser_Factory implements Factory<DozePauser> {
    public final Provider<AlarmManager> alarmManagerProvider;
    public final Provider<Handler> handlerProvider;
    public final Provider<AlwaysOnDisplayPolicy> policyProvider;

    public DozePauser_Factory(Provider<Handler> provider, Provider<AlarmManager> provider2, Provider<AlwaysOnDisplayPolicy> provider3) {
        this.handlerProvider = provider;
        this.alarmManagerProvider = provider2;
        this.policyProvider = provider3;
    }

    public DozePauser get() {
        return newInstance(this.handlerProvider.get(), this.alarmManagerProvider.get(), this.policyProvider.get());
    }

    public static DozePauser_Factory create(Provider<Handler> provider, Provider<AlarmManager> provider2, Provider<AlwaysOnDisplayPolicy> provider3) {
        return new DozePauser_Factory(provider, provider2, provider3);
    }

    public static DozePauser newInstance(Handler handler, AlarmManager alarmManager, AlwaysOnDisplayPolicy alwaysOnDisplayPolicy) {
        return new DozePauser(handler, alarmManager, alwaysOnDisplayPolicy);
    }
}
