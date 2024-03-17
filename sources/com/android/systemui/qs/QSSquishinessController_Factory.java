package com.android.systemui.qs;

import dagger.internal.Factory;
import javax.inject.Provider;

public final class QSSquishinessController_Factory implements Factory<QSSquishinessController> {
    public final Provider<QSAnimator> qsAnimatorProvider;
    public final Provider<QSPanelController> qsPanelControllerProvider;
    public final Provider<QuickQSPanelController> quickQSPanelControllerProvider;

    public QSSquishinessController_Factory(Provider<QSAnimator> provider, Provider<QSPanelController> provider2, Provider<QuickQSPanelController> provider3) {
        this.qsAnimatorProvider = provider;
        this.qsPanelControllerProvider = provider2;
        this.quickQSPanelControllerProvider = provider3;
    }

    public QSSquishinessController get() {
        return newInstance(this.qsAnimatorProvider.get(), this.qsPanelControllerProvider.get(), this.quickQSPanelControllerProvider.get());
    }

    public static QSSquishinessController_Factory create(Provider<QSAnimator> provider, Provider<QSPanelController> provider2, Provider<QuickQSPanelController> provider3) {
        return new QSSquishinessController_Factory(provider, provider2, provider3);
    }

    public static QSSquishinessController newInstance(QSAnimator qSAnimator, QSPanelController qSPanelController, QuickQSPanelController quickQSPanelController) {
        return new QSSquishinessController(qSAnimator, qSPanelController, quickQSPanelController);
    }
}
