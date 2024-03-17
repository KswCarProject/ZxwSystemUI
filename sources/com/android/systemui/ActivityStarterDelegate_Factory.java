package com.android.systemui;

import com.android.systemui.statusbar.phone.CentralSurfaces;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;

public final class ActivityStarterDelegate_Factory implements Factory<ActivityStarterDelegate> {
    public final Provider<Optional<CentralSurfaces>> centralSurfacesOptionalLazyProvider;

    public ActivityStarterDelegate_Factory(Provider<Optional<CentralSurfaces>> provider) {
        this.centralSurfacesOptionalLazyProvider = provider;
    }

    public ActivityStarterDelegate get() {
        return newInstance(DoubleCheck.lazy(this.centralSurfacesOptionalLazyProvider));
    }

    public static ActivityStarterDelegate_Factory create(Provider<Optional<CentralSurfaces>> provider) {
        return new ActivityStarterDelegate_Factory(provider);
    }

    public static ActivityStarterDelegate newInstance(Lazy<Optional<CentralSurfaces>> lazy) {
        return new ActivityStarterDelegate(lazy);
    }
}
