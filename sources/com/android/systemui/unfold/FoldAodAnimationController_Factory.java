package com.android.systemui.unfold;

import android.content.Context;
import android.hardware.devicestate.DeviceStateManager;
import android.os.Handler;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.util.settings.GlobalSettings;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class FoldAodAnimationController_Factory implements Factory<FoldAodAnimationController> {
    public final Provider<Context> contextProvider;
    public final Provider<DeviceStateManager> deviceStateManagerProvider;
    public final Provider<Executor> executorProvider;
    public final Provider<GlobalSettings> globalSettingsProvider;
    public final Provider<Handler> handlerProvider;
    public final Provider<WakefulnessLifecycle> wakefulnessLifecycleProvider;

    public FoldAodAnimationController_Factory(Provider<Handler> provider, Provider<Executor> provider2, Provider<Context> provider3, Provider<DeviceStateManager> provider4, Provider<WakefulnessLifecycle> provider5, Provider<GlobalSettings> provider6) {
        this.handlerProvider = provider;
        this.executorProvider = provider2;
        this.contextProvider = provider3;
        this.deviceStateManagerProvider = provider4;
        this.wakefulnessLifecycleProvider = provider5;
        this.globalSettingsProvider = provider6;
    }

    public FoldAodAnimationController get() {
        return newInstance(this.handlerProvider.get(), this.executorProvider.get(), this.contextProvider.get(), this.deviceStateManagerProvider.get(), this.wakefulnessLifecycleProvider.get(), this.globalSettingsProvider.get());
    }

    public static FoldAodAnimationController_Factory create(Provider<Handler> provider, Provider<Executor> provider2, Provider<Context> provider3, Provider<DeviceStateManager> provider4, Provider<WakefulnessLifecycle> provider5, Provider<GlobalSettings> provider6) {
        return new FoldAodAnimationController_Factory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static FoldAodAnimationController newInstance(Handler handler, Executor executor, Context context, DeviceStateManager deviceStateManager, WakefulnessLifecycle wakefulnessLifecycle, GlobalSettings globalSettings) {
        return new FoldAodAnimationController(handler, executor, context, deviceStateManager, wakefulnessLifecycle, globalSettings);
    }
}
