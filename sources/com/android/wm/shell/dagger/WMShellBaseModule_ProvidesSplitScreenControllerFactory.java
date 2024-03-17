package com.android.wm.shell.dagger;

import android.content.Context;
import com.android.wm.shell.splitscreen.SplitScreenController;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;

public final class WMShellBaseModule_ProvidesSplitScreenControllerFactory implements Factory<Optional<SplitScreenController>> {
    public final Provider<Context> contextProvider;
    public final Provider<Optional<SplitScreenController>> splitscreenControllerProvider;

    public WMShellBaseModule_ProvidesSplitScreenControllerFactory(Provider<Optional<SplitScreenController>> provider, Provider<Context> provider2) {
        this.splitscreenControllerProvider = provider;
        this.contextProvider = provider2;
    }

    public Optional<SplitScreenController> get() {
        return providesSplitScreenController(this.splitscreenControllerProvider.get(), this.contextProvider.get());
    }

    public static WMShellBaseModule_ProvidesSplitScreenControllerFactory create(Provider<Optional<SplitScreenController>> provider, Provider<Context> provider2) {
        return new WMShellBaseModule_ProvidesSplitScreenControllerFactory(provider, provider2);
    }

    /* JADX WARNING: type inference failed for: r0v0, types: [java.util.Optional<com.android.wm.shell.splitscreen.SplitScreenController>, java.util.Optional] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.Optional<com.android.wm.shell.splitscreen.SplitScreenController> providesSplitScreenController(java.util.Optional<com.android.wm.shell.splitscreen.SplitScreenController> r0, android.content.Context r1) {
        /*
            java.util.Optional r0 = com.android.wm.shell.dagger.WMShellBaseModule.providesSplitScreenController(r0, r1)
            java.lang.Object r0 = dagger.internal.Preconditions.checkNotNullFromProvides(r0)
            java.util.Optional r0 = (java.util.Optional) r0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.dagger.WMShellBaseModule_ProvidesSplitScreenControllerFactory.providesSplitScreenController(java.util.Optional, android.content.Context):java.util.Optional");
    }
}
