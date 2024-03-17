package com.android.systemui.dagger;

import android.view.Choreographer;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class FrameworkServicesModule_ProvidesChoreographerFactory implements Factory<Choreographer> {
    public final FrameworkServicesModule module;

    public FrameworkServicesModule_ProvidesChoreographerFactory(FrameworkServicesModule frameworkServicesModule) {
        this.module = frameworkServicesModule;
    }

    public Choreographer get() {
        return providesChoreographer(this.module);
    }

    public static FrameworkServicesModule_ProvidesChoreographerFactory create(FrameworkServicesModule frameworkServicesModule) {
        return new FrameworkServicesModule_ProvidesChoreographerFactory(frameworkServicesModule);
    }

    public static Choreographer providesChoreographer(FrameworkServicesModule frameworkServicesModule) {
        return (Choreographer) Preconditions.checkNotNullFromProvides(frameworkServicesModule.providesChoreographer());
    }
}
