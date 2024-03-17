package com.android.wm.shell.dagger;

import com.android.wm.shell.onehanded.OneHandedController;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;

public final class WMShellBaseModule_ProvidesOneHandedControllerFactory implements Factory<Optional<OneHandedController>> {
    public final Provider<Optional<OneHandedController>> oneHandedControllerProvider;

    public WMShellBaseModule_ProvidesOneHandedControllerFactory(Provider<Optional<OneHandedController>> provider) {
        this.oneHandedControllerProvider = provider;
    }

    public Optional<OneHandedController> get() {
        return providesOneHandedController(this.oneHandedControllerProvider.get());
    }

    public static WMShellBaseModule_ProvidesOneHandedControllerFactory create(Provider<Optional<OneHandedController>> provider) {
        return new WMShellBaseModule_ProvidesOneHandedControllerFactory(provider);
    }

    /* JADX WARNING: type inference failed for: r0v0, types: [java.util.Optional, java.util.Optional<com.android.wm.shell.onehanded.OneHandedController>] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.Optional<com.android.wm.shell.onehanded.OneHandedController> providesOneHandedController(java.util.Optional<com.android.wm.shell.onehanded.OneHandedController> r0) {
        /*
            java.util.Optional r0 = com.android.wm.shell.dagger.WMShellBaseModule.providesOneHandedController(r0)
            java.lang.Object r0 = dagger.internal.Preconditions.checkNotNullFromProvides(r0)
            java.util.Optional r0 = (java.util.Optional) r0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.dagger.WMShellBaseModule_ProvidesOneHandedControllerFactory.providesOneHandedController(java.util.Optional):java.util.Optional");
    }
}
