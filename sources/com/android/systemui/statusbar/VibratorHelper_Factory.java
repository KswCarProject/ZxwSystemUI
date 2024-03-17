package com.android.systemui.statusbar;

import android.os.Vibrator;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class VibratorHelper_Factory implements Factory<VibratorHelper> {
    public final Provider<Executor> executorProvider;
    public final Provider<Vibrator> vibratorProvider;

    public VibratorHelper_Factory(Provider<Vibrator> provider, Provider<Executor> provider2) {
        this.vibratorProvider = provider;
        this.executorProvider = provider2;
    }

    public VibratorHelper get() {
        return newInstance(this.vibratorProvider.get(), this.executorProvider.get());
    }

    public static VibratorHelper_Factory create(Provider<Vibrator> provider, Provider<Executor> provider2) {
        return new VibratorHelper_Factory(provider, provider2);
    }

    public static VibratorHelper newInstance(Vibrator vibrator, Executor executor) {
        return new VibratorHelper(vibrator, executor);
    }
}
