package com.android.wm.shell.dagger;

import com.android.wm.shell.back.BackAnimation;
import com.android.wm.shell.back.BackAnimationController;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;

public final class WMShellBaseModule_ProvideBackAnimationFactory implements Factory<Optional<BackAnimation>> {
    public final Provider<Optional<BackAnimationController>> backAnimationControllerProvider;

    public WMShellBaseModule_ProvideBackAnimationFactory(Provider<Optional<BackAnimationController>> provider) {
        this.backAnimationControllerProvider = provider;
    }

    public Optional<BackAnimation> get() {
        return provideBackAnimation(this.backAnimationControllerProvider.get());
    }

    public static WMShellBaseModule_ProvideBackAnimationFactory create(Provider<Optional<BackAnimationController>> provider) {
        return new WMShellBaseModule_ProvideBackAnimationFactory(provider);
    }

    /* JADX WARNING: type inference failed for: r0v0, types: [java.util.Optional<com.android.wm.shell.back.BackAnimationController>, java.util.Optional] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.Optional<com.android.wm.shell.back.BackAnimation> provideBackAnimation(java.util.Optional<com.android.wm.shell.back.BackAnimationController> r0) {
        /*
            java.util.Optional r0 = com.android.wm.shell.dagger.WMShellBaseModule.provideBackAnimation(r0)
            java.lang.Object r0 = dagger.internal.Preconditions.checkNotNullFromProvides(r0)
            java.util.Optional r0 = (java.util.Optional) r0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.dagger.WMShellBaseModule_ProvideBackAnimationFactory.provideBackAnimation(java.util.Optional):java.util.Optional");
    }
}
