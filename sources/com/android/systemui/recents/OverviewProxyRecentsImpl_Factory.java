package com.android.systemui.recents;

import com.android.systemui.statusbar.phone.CentralSurfaces;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;

public final class OverviewProxyRecentsImpl_Factory implements Factory<OverviewProxyRecentsImpl> {
    public final Provider<Optional<CentralSurfaces>> centralSurfacesOptionalLazyProvider;
    public final Provider<OverviewProxyService> overviewProxyServiceProvider;

    public OverviewProxyRecentsImpl_Factory(Provider<Optional<CentralSurfaces>> provider, Provider<OverviewProxyService> provider2) {
        this.centralSurfacesOptionalLazyProvider = provider;
        this.overviewProxyServiceProvider = provider2;
    }

    public OverviewProxyRecentsImpl get() {
        return newInstance(DoubleCheck.lazy(this.centralSurfacesOptionalLazyProvider), this.overviewProxyServiceProvider.get());
    }

    public static OverviewProxyRecentsImpl_Factory create(Provider<Optional<CentralSurfaces>> provider, Provider<OverviewProxyService> provider2) {
        return new OverviewProxyRecentsImpl_Factory(provider, provider2);
    }

    public static OverviewProxyRecentsImpl newInstance(Lazy<Optional<CentralSurfaces>> lazy, OverviewProxyService overviewProxyService) {
        return new OverviewProxyRecentsImpl(lazy, overviewProxyService);
    }
}
