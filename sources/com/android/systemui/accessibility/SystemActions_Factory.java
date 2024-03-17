package com.android.systemui.accessibility;

import android.content.Context;
import com.android.systemui.recents.Recents;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;

public final class SystemActions_Factory implements Factory<SystemActions> {
    public final Provider<Optional<CentralSurfaces>> centralSurfacesOptionalLazyProvider;
    public final Provider<Context> contextProvider;
    public final Provider<NotificationShadeWindowController> notificationShadeControllerProvider;
    public final Provider<Optional<Recents>> recentsOptionalProvider;

    public SystemActions_Factory(Provider<Context> provider, Provider<NotificationShadeWindowController> provider2, Provider<Optional<CentralSurfaces>> provider3, Provider<Optional<Recents>> provider4) {
        this.contextProvider = provider;
        this.notificationShadeControllerProvider = provider2;
        this.centralSurfacesOptionalLazyProvider = provider3;
        this.recentsOptionalProvider = provider4;
    }

    public SystemActions get() {
        return newInstance(this.contextProvider.get(), this.notificationShadeControllerProvider.get(), DoubleCheck.lazy(this.centralSurfacesOptionalLazyProvider), this.recentsOptionalProvider.get());
    }

    public static SystemActions_Factory create(Provider<Context> provider, Provider<NotificationShadeWindowController> provider2, Provider<Optional<CentralSurfaces>> provider3, Provider<Optional<Recents>> provider4) {
        return new SystemActions_Factory(provider, provider2, provider3, provider4);
    }

    public static SystemActions newInstance(Context context, NotificationShadeWindowController notificationShadeWindowController, Lazy<Optional<CentralSurfaces>> lazy, Optional<Recents> optional) {
        return new SystemActions(context, notificationShadeWindowController, lazy, optional);
    }
}
