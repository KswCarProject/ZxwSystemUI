package com.android.systemui.statusbar.notification.init;

import android.service.notification.StatusBarNotification;
import com.android.systemui.people.widget.PeopleSpaceWidgetManager;
import com.android.systemui.plugins.statusbar.NotificationSwipeActionHelper;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.NotificationPresenter;
import com.android.systemui.statusbar.notification.AnimatedImageNotificationManager;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationActivityStarter;
import com.android.systemui.statusbar.notification.NotificationClicker;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.NotificationListController;
import com.android.systemui.statusbar.notification.collection.NotifLiveDataStore;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.NotificationRankingManager;
import com.android.systemui.statusbar.notification.collection.TargetSdkResolver;
import com.android.systemui.statusbar.notification.collection.inflation.BindEventManagerImpl;
import com.android.systemui.statusbar.notification.collection.inflation.NotificationRowBinderImpl;
import com.android.systemui.statusbar.notification.collection.init.NotifPipelineInitializer;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.notification.collection.provider.DebugModeFilterProvider;
import com.android.systemui.statusbar.notification.collection.render.NotifStackController;
import com.android.systemui.statusbar.notification.interruption.HeadsUpController;
import com.android.systemui.statusbar.notification.interruption.HeadsUpViewBinder;
import com.android.systemui.statusbar.notification.row.NotifBindPipelineInitializer;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.phone.NotificationGroupAlertTransferHelper;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.statusbar.policy.RemoteInputUriController;
import com.android.wm.shell.bubbles.Bubbles;
import dagger.Lazy;
import java.io.PrintWriter;
import java.util.Optional;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotificationsControllerImpl.kt */
public final class NotificationsControllerImpl implements NotificationsController {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public final AnimatedImageNotificationManager animatedImageNotificationManager;
    @NotNull
    public final BindEventManagerImpl bindEventManagerImpl;
    @NotNull
    public final Optional<Bubbles> bubblesOptional;
    @NotNull
    public final Lazy<CentralSurfaces> centralSurfaces;
    @NotNull
    public final NotificationClicker.Builder clickerBuilder;
    @NotNull
    public final Lazy<CommonNotifCollection> commonNotifCollection;
    @NotNull
    public final DebugModeFilterProvider debugModeFilterProvider;
    @NotNull
    public final DeviceProvisionedController deviceProvisionedController;
    @NotNull
    public final NotificationEntryManager entryManager;
    @NotNull
    public final NotificationGroupAlertTransferHelper groupAlertTransferHelper;
    @NotNull
    public final Lazy<NotificationGroupManagerLegacy> groupManagerLegacy;
    @NotNull
    public final HeadsUpController headsUpController;
    @NotNull
    public final HeadsUpManager headsUpManager;
    @NotNull
    public final HeadsUpViewBinder headsUpViewBinder;
    @NotNull
    public final NotificationRankingManager legacyRanker;
    @NotNull
    public final Lazy<NotifPipelineInitializer> newNotifPipelineInitializer;
    @NotNull
    public final NotifBindPipelineInitializer notifBindPipelineInitializer;
    @NotNull
    public final NotifLiveDataStore notifLiveDataStore;
    @NotNull
    public final Lazy<NotifPipeline> notifPipeline;
    @NotNull
    public final NotifPipelineFlags notifPipelineFlags;
    @NotNull
    public final NotificationListener notificationListener;
    @NotNull
    public final NotificationRowBinderImpl notificationRowBinder;
    @NotNull
    public final PeopleSpaceWidgetManager peopleSpaceWidgetManager;
    @NotNull
    public final RemoteInputUriController remoteInputUriController;
    @NotNull
    public final TargetSdkResolver targetSdkResolver;

    public NotificationsControllerImpl(@NotNull Lazy<CentralSurfaces> lazy, @NotNull NotifPipelineFlags notifPipelineFlags2, @NotNull NotificationListener notificationListener2, @NotNull NotificationEntryManager notificationEntryManager, @NotNull DebugModeFilterProvider debugModeFilterProvider2, @NotNull NotificationRankingManager notificationRankingManager, @NotNull Lazy<CommonNotifCollection> lazy2, @NotNull Lazy<NotifPipeline> lazy3, @NotNull NotifLiveDataStore notifLiveDataStore2, @NotNull TargetSdkResolver targetSdkResolver2, @NotNull Lazy<NotifPipelineInitializer> lazy4, @NotNull NotifBindPipelineInitializer notifBindPipelineInitializer2, @NotNull DeviceProvisionedController deviceProvisionedController2, @NotNull NotificationRowBinderImpl notificationRowBinderImpl, @NotNull BindEventManagerImpl bindEventManagerImpl2, @NotNull RemoteInputUriController remoteInputUriController2, @NotNull Lazy<NotificationGroupManagerLegacy> lazy5, @NotNull NotificationGroupAlertTransferHelper notificationGroupAlertTransferHelper, @NotNull HeadsUpManager headsUpManager2, @NotNull HeadsUpController headsUpController2, @NotNull HeadsUpViewBinder headsUpViewBinder2, @NotNull NotificationClicker.Builder builder, @NotNull AnimatedImageNotificationManager animatedImageNotificationManager2, @NotNull PeopleSpaceWidgetManager peopleSpaceWidgetManager2, @NotNull Optional<Bubbles> optional) {
        this.centralSurfaces = lazy;
        this.notifPipelineFlags = notifPipelineFlags2;
        this.notificationListener = notificationListener2;
        this.entryManager = notificationEntryManager;
        this.debugModeFilterProvider = debugModeFilterProvider2;
        this.legacyRanker = notificationRankingManager;
        this.commonNotifCollection = lazy2;
        this.notifPipeline = lazy3;
        this.notifLiveDataStore = notifLiveDataStore2;
        this.targetSdkResolver = targetSdkResolver2;
        this.newNotifPipelineInitializer = lazy4;
        this.notifBindPipelineInitializer = notifBindPipelineInitializer2;
        this.deviceProvisionedController = deviceProvisionedController2;
        this.notificationRowBinder = notificationRowBinderImpl;
        this.bindEventManagerImpl = bindEventManagerImpl2;
        this.remoteInputUriController = remoteInputUriController2;
        this.groupManagerLegacy = lazy5;
        this.groupAlertTransferHelper = notificationGroupAlertTransferHelper;
        this.headsUpManager = headsUpManager2;
        this.headsUpController = headsUpController2;
        this.headsUpViewBinder = headsUpViewBinder2;
        this.clickerBuilder = builder;
        this.animatedImageNotificationManager = animatedImageNotificationManager2;
        this.peopleSpaceWidgetManager = peopleSpaceWidgetManager2;
        this.bubblesOptional = optional;
    }

    public void initialize(@NotNull NotificationPresenter notificationPresenter, @NotNull NotificationListContainer notificationListContainer, @NotNull NotifStackController notifStackController, @NotNull NotificationActivityStarter notificationActivityStarter, @NotNull NotificationRowBinderImpl.BindRowCallback bindRowCallback) {
        this.notificationListener.registerAsSystemService();
        new NotificationListController(this.entryManager, notificationListContainer, this.deviceProvisionedController).bind();
        this.notificationRowBinder.setNotificationClicker(this.clickerBuilder.build(Optional.of(this.centralSurfaces.get()), this.bubblesOptional, notificationActivityStarter));
        this.notificationRowBinder.setUpWithPresenter(notificationPresenter, notificationListContainer, bindRowCallback);
        this.headsUpViewBinder.setPresenter(notificationPresenter);
        this.notifBindPipelineInitializer.initialize();
        this.animatedImageNotificationManager.bind();
        this.newNotifPipelineInitializer.get().initialize(this.notificationListener, this.notificationRowBinder, notificationListContainer, notifStackController);
        if (this.notifPipelineFlags.isNewPipelineEnabled()) {
            this.targetSdkResolver.initialize(this.notifPipeline.get());
        } else {
            this.targetSdkResolver.initialize(this.entryManager);
            this.remoteInputUriController.attach(this.entryManager);
            this.groupAlertTransferHelper.bind(this.entryManager, this.groupManagerLegacy.get());
            this.bindEventManagerImpl.attachToLegacyPipeline(this.entryManager);
            this.headsUpManager.addListener(this.groupManagerLegacy.get());
            this.headsUpManager.addListener(this.groupAlertTransferHelper);
            this.headsUpController.attach(this.entryManager, this.headsUpManager);
            this.groupManagerLegacy.get().setHeadsUpManager(this.headsUpManager);
            this.groupAlertTransferHelper.setHeadsUpManager(this.headsUpManager);
            this.debugModeFilterProvider.registerInvalidationListener(new NotificationsControllerImpl$initialize$1(this));
            this.entryManager.initialize(this.notificationListener, this.legacyRanker);
        }
        this.peopleSpaceWidgetManager.attach(this.notificationListener);
    }

    public void dump(@NotNull PrintWriter printWriter, @NotNull String[] strArr, boolean z) {
        if (z) {
            this.entryManager.dump(printWriter, "  ");
        }
    }

    public void requestNotificationUpdate(@NotNull String str) {
        this.entryManager.updateNotifications(str);
    }

    public void resetUserExpandedStates() {
        for (NotificationEntry resetUserExpansion : this.commonNotifCollection.get().getAllNotifs()) {
            resetUserExpansion.resetUserExpansion();
        }
    }

    public void setNotificationSnoozed(@NotNull StatusBarNotification statusBarNotification, @NotNull NotificationSwipeActionHelper.SnoozeOption snoozeOption) {
        if (snoozeOption.getSnoozeCriterion() != null) {
            this.notificationListener.snoozeNotification(statusBarNotification.getKey(), snoozeOption.getSnoozeCriterion().getId());
        } else {
            this.notificationListener.snoozeNotification(statusBarNotification.getKey(), ((long) (snoozeOption.getMinutesToSnoozeFor() * 60)) * 1000);
        }
    }

    public int getActiveNotificationsCount() {
        return this.notifLiveDataStore.getActiveNotifCount().getValue().intValue();
    }

    /* compiled from: NotificationsControllerImpl.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}
