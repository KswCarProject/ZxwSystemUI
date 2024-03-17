package com.android.systemui.statusbar.notification.collection.render;

import android.content.Context;
import com.android.systemui.statusbar.notification.NotificationSectionsFeatureManager;
import com.android.systemui.statusbar.notification.SectionHeaderVisibilityProvider;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import javax.inject.Provider;

public final class ShadeViewManager_Factory {
    public final Provider<Context> contextProvider;
    public final Provider<NotificationSectionsFeatureManager> featureManagerProvider;
    public final Provider<MediaContainerController> mediaContainerControllerProvider;
    public final Provider<NodeSpecBuilderLogger> nodeSpecBuilderLoggerProvider;
    public final Provider<SectionHeaderVisibilityProvider> sectionHeaderVisibilityProvider;
    public final Provider<ShadeViewDifferLogger> shadeViewDifferLoggerProvider;
    public final Provider<NotifViewBarn> viewBarnProvider;

    public ShadeViewManager_Factory(Provider<Context> provider, Provider<MediaContainerController> provider2, Provider<NotificationSectionsFeatureManager> provider3, Provider<SectionHeaderVisibilityProvider> provider4, Provider<NodeSpecBuilderLogger> provider5, Provider<ShadeViewDifferLogger> provider6, Provider<NotifViewBarn> provider7) {
        this.contextProvider = provider;
        this.mediaContainerControllerProvider = provider2;
        this.featureManagerProvider = provider3;
        this.sectionHeaderVisibilityProvider = provider4;
        this.nodeSpecBuilderLoggerProvider = provider5;
        this.shadeViewDifferLoggerProvider = provider6;
        this.viewBarnProvider = provider7;
    }

    public ShadeViewManager get(NotificationListContainer notificationListContainer, NotifStackController notifStackController) {
        return newInstance(this.contextProvider.get(), notificationListContainer, notifStackController, this.mediaContainerControllerProvider.get(), this.featureManagerProvider.get(), this.sectionHeaderVisibilityProvider.get(), this.nodeSpecBuilderLoggerProvider.get(), this.shadeViewDifferLoggerProvider.get(), this.viewBarnProvider.get());
    }

    public static ShadeViewManager_Factory create(Provider<Context> provider, Provider<MediaContainerController> provider2, Provider<NotificationSectionsFeatureManager> provider3, Provider<SectionHeaderVisibilityProvider> provider4, Provider<NodeSpecBuilderLogger> provider5, Provider<ShadeViewDifferLogger> provider6, Provider<NotifViewBarn> provider7) {
        return new ShadeViewManager_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7);
    }

    public static ShadeViewManager newInstance(Context context, NotificationListContainer notificationListContainer, NotifStackController notifStackController, MediaContainerController mediaContainerController, NotificationSectionsFeatureManager notificationSectionsFeatureManager, SectionHeaderVisibilityProvider sectionHeaderVisibilityProvider2, NodeSpecBuilderLogger nodeSpecBuilderLogger, ShadeViewDifferLogger shadeViewDifferLogger, NotifViewBarn notifViewBarn) {
        return new ShadeViewManager(context, notificationListContainer, notifStackController, mediaContainerController, notificationSectionsFeatureManager, sectionHeaderVisibilityProvider2, nodeSpecBuilderLogger, shadeViewDifferLogger, notifViewBarn);
    }
}
