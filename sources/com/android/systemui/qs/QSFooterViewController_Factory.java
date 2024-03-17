package com.android.systemui.qs;

import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.settings.UserTracker;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class QSFooterViewController_Factory implements Factory<QSFooterViewController> {
    public final Provider<ActivityStarter> activityStarterProvider;
    public final Provider<FalsingManager> falsingManagerProvider;
    public final Provider<QSPanelController> qsPanelControllerProvider;
    public final Provider<UserTracker> userTrackerProvider;
    public final Provider<QSFooterView> viewProvider;

    public QSFooterViewController_Factory(Provider<QSFooterView> provider, Provider<UserTracker> provider2, Provider<FalsingManager> provider3, Provider<ActivityStarter> provider4, Provider<QSPanelController> provider5) {
        this.viewProvider = provider;
        this.userTrackerProvider = provider2;
        this.falsingManagerProvider = provider3;
        this.activityStarterProvider = provider4;
        this.qsPanelControllerProvider = provider5;
    }

    public QSFooterViewController get() {
        return newInstance(this.viewProvider.get(), this.userTrackerProvider.get(), this.falsingManagerProvider.get(), this.activityStarterProvider.get(), this.qsPanelControllerProvider.get());
    }

    public static QSFooterViewController_Factory create(Provider<QSFooterView> provider, Provider<UserTracker> provider2, Provider<FalsingManager> provider3, Provider<ActivityStarter> provider4, Provider<QSPanelController> provider5) {
        return new QSFooterViewController_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static QSFooterViewController newInstance(QSFooterView qSFooterView, UserTracker userTracker, FalsingManager falsingManager, ActivityStarter activityStarter, QSPanelController qSPanelController) {
        return new QSFooterViewController(qSFooterView, userTracker, falsingManager, activityStarter, qSPanelController);
    }
}
