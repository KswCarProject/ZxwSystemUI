package com.android.systemui.statusbar.notification.collection.inflation;

import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.notification.SectionClassifier;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class NotifUiAdjustmentProvider_Factory implements Factory<NotifUiAdjustmentProvider> {
    public final Provider<NotificationLockscreenUserManager> lockscreenUserManagerProvider;
    public final Provider<SectionClassifier> sectionClassifierProvider;

    public NotifUiAdjustmentProvider_Factory(Provider<NotificationLockscreenUserManager> provider, Provider<SectionClassifier> provider2) {
        this.lockscreenUserManagerProvider = provider;
        this.sectionClassifierProvider = provider2;
    }

    public NotifUiAdjustmentProvider get() {
        return newInstance(this.lockscreenUserManagerProvider.get(), this.sectionClassifierProvider.get());
    }

    public static NotifUiAdjustmentProvider_Factory create(Provider<NotificationLockscreenUserManager> provider, Provider<SectionClassifier> provider2) {
        return new NotifUiAdjustmentProvider_Factory(provider, provider2);
    }

    public static NotifUiAdjustmentProvider newInstance(NotificationLockscreenUserManager notificationLockscreenUserManager, SectionClassifier sectionClassifier) {
        return new NotifUiAdjustmentProvider(notificationLockscreenUserManager, sectionClassifier);
    }
}
