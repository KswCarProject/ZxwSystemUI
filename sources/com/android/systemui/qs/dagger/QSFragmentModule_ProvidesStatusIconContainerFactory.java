package com.android.systemui.qs.dagger;

import com.android.systemui.qs.QuickStatusBarHeader;
import com.android.systemui.statusbar.phone.StatusIconContainer;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class QSFragmentModule_ProvidesStatusIconContainerFactory implements Factory<StatusIconContainer> {
    public final Provider<QuickStatusBarHeader> qsHeaderProvider;

    public QSFragmentModule_ProvidesStatusIconContainerFactory(Provider<QuickStatusBarHeader> provider) {
        this.qsHeaderProvider = provider;
    }

    public StatusIconContainer get() {
        return providesStatusIconContainer(this.qsHeaderProvider.get());
    }

    public static QSFragmentModule_ProvidesStatusIconContainerFactory create(Provider<QuickStatusBarHeader> provider) {
        return new QSFragmentModule_ProvidesStatusIconContainerFactory(provider);
    }

    public static StatusIconContainer providesStatusIconContainer(QuickStatusBarHeader quickStatusBarHeader) {
        return (StatusIconContainer) Preconditions.checkNotNullFromProvides(QSFragmentModule.providesStatusIconContainer(quickStatusBarHeader));
    }
}
