package com.android.systemui.util.concurrency;

import android.os.Looper;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class GlobalConcurrencyModule_ProvideMainLooperFactory implements Factory<Looper> {

    public static final class InstanceHolder {
        public static final GlobalConcurrencyModule_ProvideMainLooperFactory INSTANCE = new GlobalConcurrencyModule_ProvideMainLooperFactory();
    }

    public Looper get() {
        return provideMainLooper();
    }

    public static GlobalConcurrencyModule_ProvideMainLooperFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static Looper provideMainLooper() {
        return (Looper) Preconditions.checkNotNullFromProvides(GlobalConcurrencyModule.provideMainLooper());
    }
}
