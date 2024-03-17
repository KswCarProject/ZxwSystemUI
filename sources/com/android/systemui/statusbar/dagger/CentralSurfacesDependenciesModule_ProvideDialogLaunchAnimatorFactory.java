package com.android.systemui.statusbar.dagger;

import android.service.dreams.IDreamManager;
import com.android.systemui.animation.DialogLaunchAnimator;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class CentralSurfacesDependenciesModule_ProvideDialogLaunchAnimatorFactory implements Factory<DialogLaunchAnimator> {
    public final Provider<IDreamManager> dreamManagerProvider;

    public CentralSurfacesDependenciesModule_ProvideDialogLaunchAnimatorFactory(Provider<IDreamManager> provider) {
        this.dreamManagerProvider = provider;
    }

    public DialogLaunchAnimator get() {
        return provideDialogLaunchAnimator(this.dreamManagerProvider.get());
    }

    public static CentralSurfacesDependenciesModule_ProvideDialogLaunchAnimatorFactory create(Provider<IDreamManager> provider) {
        return new CentralSurfacesDependenciesModule_ProvideDialogLaunchAnimatorFactory(provider);
    }

    public static DialogLaunchAnimator provideDialogLaunchAnimator(IDreamManager iDreamManager) {
        return (DialogLaunchAnimator) Preconditions.checkNotNullFromProvides(CentralSurfacesDependenciesModule.provideDialogLaunchAnimator(iDreamManager));
    }
}
