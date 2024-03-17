package com.android.systemui.qs;

import com.android.systemui.statusbar.policy.ConfigurationController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class QSContainerImplController_Factory implements Factory<QSContainerImplController> {
    public final Provider<ConfigurationController> configurationControllerProvider;
    public final Provider<QSPanelController> qsPanelControllerProvider;
    public final Provider<QuickStatusBarHeaderController> quickStatusBarHeaderControllerProvider;
    public final Provider<QSContainerImpl> viewProvider;

    public QSContainerImplController_Factory(Provider<QSContainerImpl> provider, Provider<QSPanelController> provider2, Provider<QuickStatusBarHeaderController> provider3, Provider<ConfigurationController> provider4) {
        this.viewProvider = provider;
        this.qsPanelControllerProvider = provider2;
        this.quickStatusBarHeaderControllerProvider = provider3;
        this.configurationControllerProvider = provider4;
    }

    public QSContainerImplController get() {
        return newInstance(this.viewProvider.get(), this.qsPanelControllerProvider.get(), this.quickStatusBarHeaderControllerProvider.get(), this.configurationControllerProvider.get());
    }

    public static QSContainerImplController_Factory create(Provider<QSContainerImpl> provider, Provider<QSPanelController> provider2, Provider<QuickStatusBarHeaderController> provider3, Provider<ConfigurationController> provider4) {
        return new QSContainerImplController_Factory(provider, provider2, provider3, provider4);
    }

    public static QSContainerImplController newInstance(QSContainerImpl qSContainerImpl, QSPanelController qSPanelController, Object obj, ConfigurationController configurationController) {
        return new QSContainerImplController(qSContainerImpl, qSPanelController, (QuickStatusBarHeaderController) obj, configurationController);
    }
}
