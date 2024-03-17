package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.media.KeyguardMediaController;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationSectionsFeatureManager;
import com.android.systemui.statusbar.notification.collection.render.MediaContainerController;
import com.android.systemui.statusbar.notification.collection.render.SectionHeaderController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class NotificationSectionsManager_Factory implements Factory<NotificationSectionsManager> {
    public final Provider<SectionHeaderController> alertingHeaderControllerProvider;
    public final Provider<ConfigurationController> configurationControllerProvider;
    public final Provider<SectionHeaderController> incomingHeaderControllerProvider;
    public final Provider<KeyguardMediaController> keyguardMediaControllerProvider;
    public final Provider<NotificationSectionsLogger> loggerProvider;
    public final Provider<MediaContainerController> mediaContainerControllerProvider;
    public final Provider<NotifPipelineFlags> notifPipelineFlagsProvider;
    public final Provider<SectionHeaderController> peopleHeaderControllerProvider;
    public final Provider<NotificationSectionsFeatureManager> sectionsFeatureManagerProvider;
    public final Provider<SectionHeaderController> silentHeaderControllerProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;

    public NotificationSectionsManager_Factory(Provider<StatusBarStateController> provider, Provider<ConfigurationController> provider2, Provider<KeyguardMediaController> provider3, Provider<NotificationSectionsFeatureManager> provider4, Provider<NotificationSectionsLogger> provider5, Provider<NotifPipelineFlags> provider6, Provider<MediaContainerController> provider7, Provider<SectionHeaderController> provider8, Provider<SectionHeaderController> provider9, Provider<SectionHeaderController> provider10, Provider<SectionHeaderController> provider11) {
        this.statusBarStateControllerProvider = provider;
        this.configurationControllerProvider = provider2;
        this.keyguardMediaControllerProvider = provider3;
        this.sectionsFeatureManagerProvider = provider4;
        this.loggerProvider = provider5;
        this.notifPipelineFlagsProvider = provider6;
        this.mediaContainerControllerProvider = provider7;
        this.incomingHeaderControllerProvider = provider8;
        this.peopleHeaderControllerProvider = provider9;
        this.alertingHeaderControllerProvider = provider10;
        this.silentHeaderControllerProvider = provider11;
    }

    public NotificationSectionsManager get() {
        return newInstance(this.statusBarStateControllerProvider.get(), this.configurationControllerProvider.get(), this.keyguardMediaControllerProvider.get(), this.sectionsFeatureManagerProvider.get(), this.loggerProvider.get(), this.notifPipelineFlagsProvider.get(), this.mediaContainerControllerProvider.get(), this.incomingHeaderControllerProvider.get(), this.peopleHeaderControllerProvider.get(), this.alertingHeaderControllerProvider.get(), this.silentHeaderControllerProvider.get());
    }

    public static NotificationSectionsManager_Factory create(Provider<StatusBarStateController> provider, Provider<ConfigurationController> provider2, Provider<KeyguardMediaController> provider3, Provider<NotificationSectionsFeatureManager> provider4, Provider<NotificationSectionsLogger> provider5, Provider<NotifPipelineFlags> provider6, Provider<MediaContainerController> provider7, Provider<SectionHeaderController> provider8, Provider<SectionHeaderController> provider9, Provider<SectionHeaderController> provider10, Provider<SectionHeaderController> provider11) {
        return new NotificationSectionsManager_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11);
    }

    public static NotificationSectionsManager newInstance(StatusBarStateController statusBarStateController, ConfigurationController configurationController, KeyguardMediaController keyguardMediaController, NotificationSectionsFeatureManager notificationSectionsFeatureManager, NotificationSectionsLogger notificationSectionsLogger, NotifPipelineFlags notifPipelineFlags, MediaContainerController mediaContainerController, SectionHeaderController sectionHeaderController, SectionHeaderController sectionHeaderController2, SectionHeaderController sectionHeaderController3, SectionHeaderController sectionHeaderController4) {
        return new NotificationSectionsManager(statusBarStateController, configurationController, keyguardMediaController, notificationSectionsFeatureManager, notificationSectionsLogger, notifPipelineFlags, mediaContainerController, sectionHeaderController, sectionHeaderController2, sectionHeaderController3, sectionHeaderController4);
    }
}
