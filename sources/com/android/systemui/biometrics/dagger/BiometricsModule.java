package com.android.systemui.biometrics.dagger;

import com.android.systemui.util.concurrency.ThreadFactory;
import java.util.concurrent.Executor;
import org.jetbrains.annotations.NotNull;

/* compiled from: BiometricsModule.kt */
public final class BiometricsModule {
    @NotNull
    public static final BiometricsModule INSTANCE = new BiometricsModule();

    @NotNull
    public static final Executor providesPluginExecutor(@NotNull ThreadFactory threadFactory) {
        return threadFactory.buildExecutorOnNewThread("biometrics");
    }
}
