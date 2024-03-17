package com.android.systemui.statusbar.phone.dagger;

import android.view.View;
import com.android.systemui.statusbar.phone.StatusIconContainer;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class StatusBarViewModule_ProvidesStatusIconContainerFactory implements Factory<StatusIconContainer> {
    public final Provider<View> headerProvider;

    public StatusBarViewModule_ProvidesStatusIconContainerFactory(Provider<View> provider) {
        this.headerProvider = provider;
    }

    public StatusIconContainer get() {
        return providesStatusIconContainer(this.headerProvider.get());
    }

    public static StatusBarViewModule_ProvidesStatusIconContainerFactory create(Provider<View> provider) {
        return new StatusBarViewModule_ProvidesStatusIconContainerFactory(provider);
    }

    public static StatusIconContainer providesStatusIconContainer(View view) {
        return (StatusIconContainer) Preconditions.checkNotNullFromProvides(StatusBarViewModule.providesStatusIconContainer(view));
    }
}
