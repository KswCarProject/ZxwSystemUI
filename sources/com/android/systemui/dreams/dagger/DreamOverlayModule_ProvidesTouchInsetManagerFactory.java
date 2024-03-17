package com.android.systemui.dreams.dagger;

import com.android.systemui.dreams.DreamOverlayContainerView;
import com.android.systemui.touch.TouchInsetManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class DreamOverlayModule_ProvidesTouchInsetManagerFactory implements Factory<TouchInsetManager> {
    public final Provider<Executor> executorProvider;
    public final Provider<DreamOverlayContainerView> viewProvider;

    public DreamOverlayModule_ProvidesTouchInsetManagerFactory(Provider<Executor> provider, Provider<DreamOverlayContainerView> provider2) {
        this.executorProvider = provider;
        this.viewProvider = provider2;
    }

    public TouchInsetManager get() {
        return providesTouchInsetManager(this.executorProvider.get(), this.viewProvider.get());
    }

    public static DreamOverlayModule_ProvidesTouchInsetManagerFactory create(Provider<Executor> provider, Provider<DreamOverlayContainerView> provider2) {
        return new DreamOverlayModule_ProvidesTouchInsetManagerFactory(provider, provider2);
    }

    public static TouchInsetManager providesTouchInsetManager(Executor executor, DreamOverlayContainerView dreamOverlayContainerView) {
        return (TouchInsetManager) Preconditions.checkNotNullFromProvides(DreamOverlayModule.providesTouchInsetManager(executor, dreamOverlayContainerView));
    }
}
