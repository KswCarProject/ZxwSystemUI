package com.android.systemui.statusbar.phone;

import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.row.RowContentBindStage;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class NotificationGroupAlertTransferHelper_Factory implements Factory<NotificationGroupAlertTransferHelper> {
    public final Provider<RowContentBindStage> bindStageProvider;
    public final Provider<NotificationGroupManagerLegacy> notificationGroupManagerLegacyProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;

    public NotificationGroupAlertTransferHelper_Factory(Provider<RowContentBindStage> provider, Provider<StatusBarStateController> provider2, Provider<NotificationGroupManagerLegacy> provider3) {
        this.bindStageProvider = provider;
        this.statusBarStateControllerProvider = provider2;
        this.notificationGroupManagerLegacyProvider = provider3;
    }

    public NotificationGroupAlertTransferHelper get() {
        return newInstance(this.bindStageProvider.get(), this.statusBarStateControllerProvider.get(), this.notificationGroupManagerLegacyProvider.get());
    }

    public static NotificationGroupAlertTransferHelper_Factory create(Provider<RowContentBindStage> provider, Provider<StatusBarStateController> provider2, Provider<NotificationGroupManagerLegacy> provider3) {
        return new NotificationGroupAlertTransferHelper_Factory(provider, provider2, provider3);
    }

    public static NotificationGroupAlertTransferHelper newInstance(RowContentBindStage rowContentBindStage, StatusBarStateController statusBarStateController, NotificationGroupManagerLegacy notificationGroupManagerLegacy) {
        return new NotificationGroupAlertTransferHelper(rowContentBindStage, statusBarStateController, notificationGroupManagerLegacy);
    }
}
