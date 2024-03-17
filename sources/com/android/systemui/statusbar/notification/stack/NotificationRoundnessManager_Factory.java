package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.statusbar.notification.NotificationSectionsFeatureManager;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class NotificationRoundnessManager_Factory implements Factory<NotificationRoundnessManager> {
    public final Provider<NotificationSectionsFeatureManager> sectionsFeatureManagerProvider;

    public NotificationRoundnessManager_Factory(Provider<NotificationSectionsFeatureManager> provider) {
        this.sectionsFeatureManagerProvider = provider;
    }

    public NotificationRoundnessManager get() {
        return newInstance(this.sectionsFeatureManagerProvider.get());
    }

    public static NotificationRoundnessManager_Factory create(Provider<NotificationSectionsFeatureManager> provider) {
        return new NotificationRoundnessManager_Factory(provider);
    }

    public static NotificationRoundnessManager newInstance(NotificationSectionsFeatureManager notificationSectionsFeatureManager) {
        return new NotificationRoundnessManager(notificationSectionsFeatureManager);
    }
}
