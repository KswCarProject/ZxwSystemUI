package com.android.systemui.statusbar;

import android.content.Context;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.phone.ScrimController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class LockscreenShadeScrimTransitionController_Factory implements Factory<LockscreenShadeScrimTransitionController> {
    public final Provider<ConfigurationController> configurationControllerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<ScrimController> scrimControllerProvider;

    public LockscreenShadeScrimTransitionController_Factory(Provider<ScrimController> provider, Provider<Context> provider2, Provider<ConfigurationController> provider3, Provider<DumpManager> provider4) {
        this.scrimControllerProvider = provider;
        this.contextProvider = provider2;
        this.configurationControllerProvider = provider3;
        this.dumpManagerProvider = provider4;
    }

    public LockscreenShadeScrimTransitionController get() {
        return newInstance(this.scrimControllerProvider.get(), this.contextProvider.get(), this.configurationControllerProvider.get(), this.dumpManagerProvider.get());
    }

    public static LockscreenShadeScrimTransitionController_Factory create(Provider<ScrimController> provider, Provider<Context> provider2, Provider<ConfigurationController> provider3, Provider<DumpManager> provider4) {
        return new LockscreenShadeScrimTransitionController_Factory(provider, provider2, provider3, provider4);
    }

    public static LockscreenShadeScrimTransitionController newInstance(ScrimController scrimController, Context context, ConfigurationController configurationController, DumpManager dumpManager) {
        return new LockscreenShadeScrimTransitionController(scrimController, context, configurationController, dumpManager);
    }
}
