package com.android.systemui.dagger;

import com.android.systemui.lowlightclock.LowLightClockController;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;

public final class SystemUIModule_ProvideLowLightClockControllerFactory implements Factory<Optional<LowLightClockController>> {
    public final Provider<Optional<LowLightClockController>> optionalControllerProvider;

    public SystemUIModule_ProvideLowLightClockControllerFactory(Provider<Optional<LowLightClockController>> provider) {
        this.optionalControllerProvider = provider;
    }

    public Optional<LowLightClockController> get() {
        return provideLowLightClockController(this.optionalControllerProvider.get());
    }

    public static SystemUIModule_ProvideLowLightClockControllerFactory create(Provider<Optional<LowLightClockController>> provider) {
        return new SystemUIModule_ProvideLowLightClockControllerFactory(provider);
    }

    /* JADX WARNING: type inference failed for: r0v0, types: [java.util.Optional, java.util.Optional<com.android.systemui.lowlightclock.LowLightClockController>] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.Optional<com.android.systemui.lowlightclock.LowLightClockController> provideLowLightClockController(java.util.Optional<com.android.systemui.lowlightclock.LowLightClockController> r0) {
        /*
            java.util.Optional r0 = com.android.systemui.dagger.SystemUIModule.provideLowLightClockController(r0)
            java.lang.Object r0 = dagger.internal.Preconditions.checkNotNullFromProvides(r0)
            java.util.Optional r0 = (java.util.Optional) r0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.dagger.SystemUIModule_ProvideLowLightClockControllerFactory.provideLowLightClockController(java.util.Optional):java.util.Optional");
    }
}
