package com.android.systemui.statusbar.notification.init;

import com.android.systemui.people.widget.PeopleSpaceWidgetManager;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.notification.AnimatedImageNotificationManager;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationClicker;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotifLiveDataStore;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationRankingManager;
import com.android.systemui.statusbar.notification.collection.TargetSdkResolver;
import com.android.systemui.statusbar.notification.collection.inflation.BindEventManagerImpl;
import com.android.systemui.statusbar.notification.collection.inflation.NotificationRowBinderImpl;
import com.android.systemui.statusbar.notification.collection.init.NotifPipelineInitializer;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.notification.collection.provider.DebugModeFilterProvider;
import com.android.systemui.statusbar.notification.interruption.HeadsUpController;
import com.android.systemui.statusbar.notification.interruption.HeadsUpViewBinder;
import com.android.systemui.statusbar.notification.row.NotifBindPipelineInitializer;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.phone.NotificationGroupAlertTransferHelper;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.statusbar.policy.RemoteInputUriController;
import com.android.wm.shell.bubbles.Bubbles;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;

public final class NotificationsControllerImpl_Factory implements Factory<NotificationsControllerImpl> {
    public final Provider<AnimatedImageNotificationManager> animatedImageNotificationManagerProvider;
    public final Provider<BindEventManagerImpl> bindEventManagerImplProvider;
    public final Provider<Optional<Bubbles>> bubblesOptionalProvider;
    public final Provider<CentralSurfaces> centralSurfacesProvider;
    public final Provider<NotificationClicker.Builder> clickerBuilderProvider;
    public final Provider<CommonNotifCollection> commonNotifCollectionProvider;
    public final Provider<DebugModeFilterProvider> debugModeFilterProvider;
    public final Provider<DeviceProvisionedController> deviceProvisionedControllerProvider;
    public final Provider<NotificationEntryManager> entryManagerProvider;
    public final Provider<NotificationGroupAlertTransferHelper> groupAlertTransferHelperProvider;
    public final Provider<NotificationGroupManagerLegacy> groupManagerLegacyProvider;
    public final Provider<HeadsUpController> headsUpControllerProvider;
    public final Provider<HeadsUpManager> headsUpManagerProvider;
    public final Provider<HeadsUpViewBinder> headsUpViewBinderProvider;
    public final Provider<NotificationRankingManager> legacyRankerProvider;
    public final Provider<NotifPipelineInitializer> newNotifPipelineInitializerProvider;
    public final Provider<NotifBindPipelineInitializer> notifBindPipelineInitializerProvider;
    public final Provider<NotifLiveDataStore> notifLiveDataStoreProvider;
    public final Provider<NotifPipelineFlags> notifPipelineFlagsProvider;
    public final Provider<NotifPipeline> notifPipelineProvider;
    public final Provider<NotificationListener> notificationListenerProvider;
    public final Provider<NotificationRowBinderImpl> notificationRowBinderProvider;
    public final Provider<PeopleSpaceWidgetManager> peopleSpaceWidgetManagerProvider;
    public final Provider<RemoteInputUriController> remoteInputUriControllerProvider;
    public final Provider<TargetSdkResolver> targetSdkResolverProvider;

    public NotificationsControllerImpl_Factory(Provider<CentralSurfaces> provider, Provider<NotifPipelineFlags> provider2, Provider<NotificationListener> provider3, Provider<NotificationEntryManager> provider4, Provider<DebugModeFilterProvider> provider5, Provider<NotificationRankingManager> provider6, Provider<CommonNotifCollection> provider7, Provider<NotifPipeline> provider8, Provider<NotifLiveDataStore> provider9, Provider<TargetSdkResolver> provider10, Provider<NotifPipelineInitializer> provider11, Provider<NotifBindPipelineInitializer> provider12, Provider<DeviceProvisionedController> provider13, Provider<NotificationRowBinderImpl> provider14, Provider<BindEventManagerImpl> provider15, Provider<RemoteInputUriController> provider16, Provider<NotificationGroupManagerLegacy> provider17, Provider<NotificationGroupAlertTransferHelper> provider18, Provider<HeadsUpManager> provider19, Provider<HeadsUpController> provider20, Provider<HeadsUpViewBinder> provider21, Provider<NotificationClicker.Builder> provider22, Provider<AnimatedImageNotificationManager> provider23, Provider<PeopleSpaceWidgetManager> provider24, Provider<Optional<Bubbles>> provider25) {
        this.centralSurfacesProvider = provider;
        this.notifPipelineFlagsProvider = provider2;
        this.notificationListenerProvider = provider3;
        this.entryManagerProvider = provider4;
        this.debugModeFilterProvider = provider5;
        this.legacyRankerProvider = provider6;
        this.commonNotifCollectionProvider = provider7;
        this.notifPipelineProvider = provider8;
        this.notifLiveDataStoreProvider = provider9;
        this.targetSdkResolverProvider = provider10;
        this.newNotifPipelineInitializerProvider = provider11;
        this.notifBindPipelineInitializerProvider = provider12;
        this.deviceProvisionedControllerProvider = provider13;
        this.notificationRowBinderProvider = provider14;
        this.bindEventManagerImplProvider = provider15;
        this.remoteInputUriControllerProvider = provider16;
        this.groupManagerLegacyProvider = provider17;
        this.groupAlertTransferHelperProvider = provider18;
        this.headsUpManagerProvider = provider19;
        this.headsUpControllerProvider = provider20;
        this.headsUpViewBinderProvider = provider21;
        this.clickerBuilderProvider = provider22;
        this.animatedImageNotificationManagerProvider = provider23;
        this.peopleSpaceWidgetManagerProvider = provider24;
        this.bubblesOptionalProvider = provider25;
    }

    public NotificationsControllerImpl get() {
        return newInstance(DoubleCheck.lazy(this.centralSurfacesProvider), this.notifPipelineFlagsProvider.get(), this.notificationListenerProvider.get(), this.entryManagerProvider.get(), this.debugModeFilterProvider.get(), this.legacyRankerProvider.get(), DoubleCheck.lazy(this.commonNotifCollectionProvider), DoubleCheck.lazy(this.notifPipelineProvider), this.notifLiveDataStoreProvider.get(), this.targetSdkResolverProvider.get(), DoubleCheck.lazy(this.newNotifPipelineInitializerProvider), this.notifBindPipelineInitializerProvider.get(), this.deviceProvisionedControllerProvider.get(), this.notificationRowBinderProvider.get(), this.bindEventManagerImplProvider.get(), this.remoteInputUriControllerProvider.get(), DoubleCheck.lazy(this.groupManagerLegacyProvider), this.groupAlertTransferHelperProvider.get(), this.headsUpManagerProvider.get(), this.headsUpControllerProvider.get(), this.headsUpViewBinderProvider.get(), this.clickerBuilderProvider.get(), this.animatedImageNotificationManagerProvider.get(), this.peopleSpaceWidgetManagerProvider.get(), this.bubblesOptionalProvider.get());
    }

    public static NotificationsControllerImpl_Factory create(Provider<CentralSurfaces> provider, Provider<NotifPipelineFlags> provider2, Provider<NotificationListener> provider3, Provider<NotificationEntryManager> provider4, Provider<DebugModeFilterProvider> provider5, Provider<NotificationRankingManager> provider6, Provider<CommonNotifCollection> provider7, Provider<NotifPipeline> provider8, Provider<NotifLiveDataStore> provider9, Provider<TargetSdkResolver> provider10, Provider<NotifPipelineInitializer> provider11, Provider<NotifBindPipelineInitializer> provider12, Provider<DeviceProvisionedController> provider13, Provider<NotificationRowBinderImpl> provider14, Provider<BindEventManagerImpl> provider15, Provider<RemoteInputUriController> provider16, Provider<NotificationGroupManagerLegacy> provider17, Provider<NotificationGroupAlertTransferHelper> provider18, Provider<HeadsUpManager> provider19, Provider<HeadsUpController> provider20, Provider<HeadsUpViewBinder> provider21, Provider<NotificationClicker.Builder> provider22, Provider<AnimatedImageNotificationManager> provider23, Provider<PeopleSpaceWidgetManager> provider24, Provider<Optional<Bubbles>> provider25) {
        return new NotificationsControllerImpl_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19, provider20, provider21, provider22, provider23, provider24, provider25);
    }

    public static NotificationsControllerImpl newInstance(Lazy<CentralSurfaces> lazy, NotifPipelineFlags notifPipelineFlags, NotificationListener notificationListener, NotificationEntryManager notificationEntryManager, DebugModeFilterProvider debugModeFilterProvider2, NotificationRankingManager notificationRankingManager, Lazy<CommonNotifCollection> lazy2, Lazy<NotifPipeline> lazy3, NotifLiveDataStore notifLiveDataStore, TargetSdkResolver targetSdkResolver, Lazy<NotifPipelineInitializer> lazy4, NotifBindPipelineInitializer notifBindPipelineInitializer, DeviceProvisionedController deviceProvisionedController, NotificationRowBinderImpl notificationRowBinderImpl, BindEventManagerImpl bindEventManagerImpl, RemoteInputUriController remoteInputUriController, Lazy<NotificationGroupManagerLegacy> lazy5, NotificationGroupAlertTransferHelper notificationGroupAlertTransferHelper, HeadsUpManager headsUpManager, HeadsUpController headsUpController, HeadsUpViewBinder headsUpViewBinder, NotificationClicker.Builder builder, AnimatedImageNotificationManager animatedImageNotificationManager, PeopleSpaceWidgetManager peopleSpaceWidgetManager, Optional<Bubbles> optional) {
        return new NotificationsControllerImpl(lazy, notifPipelineFlags, notificationListener, notificationEntryManager, debugModeFilterProvider2, notificationRankingManager, lazy2, lazy3, notifLiveDataStore, targetSdkResolver, lazy4, notifBindPipelineInitializer, deviceProvisionedController, notificationRowBinderImpl, bindEventManagerImpl, remoteInputUriController, lazy5, notificationGroupAlertTransferHelper, headsUpManager, headsUpController, headsUpViewBinder, builder, animatedImageNotificationManager, peopleSpaceWidgetManager, optional);
    }
}
