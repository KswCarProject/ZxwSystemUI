package com.android.systemui.dreams.dagger;

import com.android.systemui.touch.TouchInsetManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class DreamOverlayModule_ProvidesTouchInsetSessionFactory implements Factory<TouchInsetManager.TouchInsetSession> {
    public final Provider<TouchInsetManager> managerProvider;

    public DreamOverlayModule_ProvidesTouchInsetSessionFactory(Provider<TouchInsetManager> provider) {
        this.managerProvider = provider;
    }

    public TouchInsetManager.TouchInsetSession get() {
        return providesTouchInsetSession(this.managerProvider.get());
    }

    public static DreamOverlayModule_ProvidesTouchInsetSessionFactory create(Provider<TouchInsetManager> provider) {
        return new DreamOverlayModule_ProvidesTouchInsetSessionFactory(provider);
    }

    public static TouchInsetManager.TouchInsetSession providesTouchInsetSession(TouchInsetManager touchInsetManager) {
        return (TouchInsetManager.TouchInsetSession) Preconditions.checkNotNullFromProvides(DreamOverlayModule.providesTouchInsetSession(touchInsetManager));
    }
}
