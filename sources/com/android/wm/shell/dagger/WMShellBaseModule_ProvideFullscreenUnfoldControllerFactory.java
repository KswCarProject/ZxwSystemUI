package com.android.wm.shell.dagger;

import com.android.wm.shell.fullscreen.FullscreenUnfoldController;
import com.android.wm.shell.unfold.ShellUnfoldProgressProvider;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;

public final class WMShellBaseModule_ProvideFullscreenUnfoldControllerFactory implements Factory<Optional<FullscreenUnfoldController>> {
    public final Provider<Optional<FullscreenUnfoldController>> fullscreenUnfoldControllerProvider;
    public final Provider<Optional<ShellUnfoldProgressProvider>> progressProvider;

    public WMShellBaseModule_ProvideFullscreenUnfoldControllerFactory(Provider<Optional<FullscreenUnfoldController>> provider, Provider<Optional<ShellUnfoldProgressProvider>> provider2) {
        this.fullscreenUnfoldControllerProvider = provider;
        this.progressProvider = provider2;
    }

    public Optional<FullscreenUnfoldController> get() {
        return provideFullscreenUnfoldController(this.fullscreenUnfoldControllerProvider.get(), this.progressProvider.get());
    }

    public static WMShellBaseModule_ProvideFullscreenUnfoldControllerFactory create(Provider<Optional<FullscreenUnfoldController>> provider, Provider<Optional<ShellUnfoldProgressProvider>> provider2) {
        return new WMShellBaseModule_ProvideFullscreenUnfoldControllerFactory(provider, provider2);
    }

    /* JADX WARNING: type inference failed for: r0v0, types: [java.util.Optional<com.android.wm.shell.fullscreen.FullscreenUnfoldController>, java.util.Optional] */
    /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional, java.util.Optional<com.android.wm.shell.unfold.ShellUnfoldProgressProvider>] */
    /* JADX WARNING: Unknown variable types count: 2 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.Optional<com.android.wm.shell.fullscreen.FullscreenUnfoldController> provideFullscreenUnfoldController(java.util.Optional<com.android.wm.shell.fullscreen.FullscreenUnfoldController> r0, java.util.Optional<com.android.wm.shell.unfold.ShellUnfoldProgressProvider> r1) {
        /*
            java.util.Optional r0 = com.android.wm.shell.dagger.WMShellBaseModule.provideFullscreenUnfoldController(r0, r1)
            java.lang.Object r0 = dagger.internal.Preconditions.checkNotNullFromProvides(r0)
            java.util.Optional r0 = (java.util.Optional) r0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.dagger.WMShellBaseModule_ProvideFullscreenUnfoldControllerFactory.provideFullscreenUnfoldController(java.util.Optional, java.util.Optional):java.util.Optional");
    }
}
