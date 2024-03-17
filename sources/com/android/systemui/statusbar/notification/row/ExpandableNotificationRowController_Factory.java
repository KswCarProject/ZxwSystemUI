package com.android.systemui.statusbar.notification.row;

import com.android.internal.logging.MetricsLogger;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.SmartReplyController;
import com.android.systemui.statusbar.notification.collection.render.GroupExpansionManager;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifier;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.statusbar.policy.SmartReplyConstants;
import com.android.systemui.statusbar.policy.dagger.RemoteInputViewSubcomponent;
import com.android.systemui.util.time.SystemClock;
import com.android.systemui.wmshell.BubblesManager;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;

public final class ExpandableNotificationRowController_Factory implements Factory<ExpandableNotificationRowController> {
    public final Provider<ActivatableNotificationViewController> activatableNotificationViewControllerProvider;
    public final Provider<Boolean> allowLongPressProvider;
    public final Provider<String> appNameProvider;
    public final Provider<Optional<BubblesManager>> bubblesManagerOptionalProvider;
    public final Provider<SystemClock> clockProvider;
    public final Provider<ExpandableNotificationRowDragController> dragControllerProvider;
    public final Provider<FalsingCollector> falsingCollectorProvider;
    public final Provider<FalsingManager> falsingManagerProvider;
    public final Provider<FeatureFlags> featureFlagsProvider;
    public final Provider<GroupExpansionManager> groupExpansionManagerProvider;
    public final Provider<GroupMembershipManager> groupMembershipManagerProvider;
    public final Provider<HeadsUpManager> headsUpManagerProvider;
    public final Provider<KeyguardBypassController> keyguardBypassControllerProvider;
    public final Provider<NotificationListContainer> listContainerProvider;
    public final Provider<NotificationMediaManager> mediaManagerProvider;
    public final Provider<MetricsLogger> metricsLoggerProvider;
    public final Provider<NotificationGutsManager> notificationGutsManagerProvider;
    public final Provider<String> notificationKeyProvider;
    public final Provider<NotificationLogger> notificationLoggerProvider;
    public final Provider<ExpandableNotificationRow.OnExpandClickListener> onExpandClickListenerProvider;
    public final Provider<OnUserInteractionCallback> onUserInteractionCallbackProvider;
    public final Provider<PeopleNotificationIdentifier> peopleNotificationIdentifierProvider;
    public final Provider<PluginManager> pluginManagerProvider;
    public final Provider<RemoteInputViewSubcomponent.Factory> rivSubcomponentFactoryProvider;
    public final Provider<RowContentBindStage> rowContentBindStageProvider;
    public final Provider<SmartReplyConstants> smartReplyConstantsProvider;
    public final Provider<SmartReplyController> smartReplyControllerProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;
    public final Provider<ExpandableNotificationRow> viewProvider;

    public ExpandableNotificationRowController_Factory(Provider<ExpandableNotificationRow> provider, Provider<ActivatableNotificationViewController> provider2, Provider<RemoteInputViewSubcomponent.Factory> provider3, Provider<MetricsLogger> provider4, Provider<NotificationListContainer> provider5, Provider<NotificationMediaManager> provider6, Provider<SmartReplyConstants> provider7, Provider<SmartReplyController> provider8, Provider<PluginManager> provider9, Provider<SystemClock> provider10, Provider<String> provider11, Provider<String> provider12, Provider<KeyguardBypassController> provider13, Provider<GroupMembershipManager> provider14, Provider<GroupExpansionManager> provider15, Provider<RowContentBindStage> provider16, Provider<NotificationLogger> provider17, Provider<HeadsUpManager> provider18, Provider<ExpandableNotificationRow.OnExpandClickListener> provider19, Provider<StatusBarStateController> provider20, Provider<NotificationGutsManager> provider21, Provider<Boolean> provider22, Provider<OnUserInteractionCallback> provider23, Provider<FalsingManager> provider24, Provider<FalsingCollector> provider25, Provider<FeatureFlags> provider26, Provider<PeopleNotificationIdentifier> provider27, Provider<Optional<BubblesManager>> provider28, Provider<ExpandableNotificationRowDragController> provider29) {
        this.viewProvider = provider;
        this.activatableNotificationViewControllerProvider = provider2;
        this.rivSubcomponentFactoryProvider = provider3;
        this.metricsLoggerProvider = provider4;
        this.listContainerProvider = provider5;
        this.mediaManagerProvider = provider6;
        this.smartReplyConstantsProvider = provider7;
        this.smartReplyControllerProvider = provider8;
        this.pluginManagerProvider = provider9;
        this.clockProvider = provider10;
        this.appNameProvider = provider11;
        this.notificationKeyProvider = provider12;
        this.keyguardBypassControllerProvider = provider13;
        this.groupMembershipManagerProvider = provider14;
        this.groupExpansionManagerProvider = provider15;
        this.rowContentBindStageProvider = provider16;
        this.notificationLoggerProvider = provider17;
        this.headsUpManagerProvider = provider18;
        this.onExpandClickListenerProvider = provider19;
        this.statusBarStateControllerProvider = provider20;
        this.notificationGutsManagerProvider = provider21;
        this.allowLongPressProvider = provider22;
        this.onUserInteractionCallbackProvider = provider23;
        this.falsingManagerProvider = provider24;
        this.falsingCollectorProvider = provider25;
        this.featureFlagsProvider = provider26;
        this.peopleNotificationIdentifierProvider = provider27;
        this.bubblesManagerOptionalProvider = provider28;
        this.dragControllerProvider = provider29;
    }

    public ExpandableNotificationRowController get() {
        return newInstance(this.viewProvider.get(), this.activatableNotificationViewControllerProvider.get(), this.rivSubcomponentFactoryProvider.get(), this.metricsLoggerProvider.get(), this.listContainerProvider.get(), this.mediaManagerProvider.get(), this.smartReplyConstantsProvider.get(), this.smartReplyControllerProvider.get(), this.pluginManagerProvider.get(), this.clockProvider.get(), this.appNameProvider.get(), this.notificationKeyProvider.get(), this.keyguardBypassControllerProvider.get(), this.groupMembershipManagerProvider.get(), this.groupExpansionManagerProvider.get(), this.rowContentBindStageProvider.get(), this.notificationLoggerProvider.get(), this.headsUpManagerProvider.get(), this.onExpandClickListenerProvider.get(), this.statusBarStateControllerProvider.get(), this.notificationGutsManagerProvider.get(), this.allowLongPressProvider.get().booleanValue(), this.onUserInteractionCallbackProvider.get(), this.falsingManagerProvider.get(), this.falsingCollectorProvider.get(), this.featureFlagsProvider.get(), this.peopleNotificationIdentifierProvider.get(), this.bubblesManagerOptionalProvider.get(), this.dragControllerProvider.get());
    }

    public static ExpandableNotificationRowController_Factory create(Provider<ExpandableNotificationRow> provider, Provider<ActivatableNotificationViewController> provider2, Provider<RemoteInputViewSubcomponent.Factory> provider3, Provider<MetricsLogger> provider4, Provider<NotificationListContainer> provider5, Provider<NotificationMediaManager> provider6, Provider<SmartReplyConstants> provider7, Provider<SmartReplyController> provider8, Provider<PluginManager> provider9, Provider<SystemClock> provider10, Provider<String> provider11, Provider<String> provider12, Provider<KeyguardBypassController> provider13, Provider<GroupMembershipManager> provider14, Provider<GroupExpansionManager> provider15, Provider<RowContentBindStage> provider16, Provider<NotificationLogger> provider17, Provider<HeadsUpManager> provider18, Provider<ExpandableNotificationRow.OnExpandClickListener> provider19, Provider<StatusBarStateController> provider20, Provider<NotificationGutsManager> provider21, Provider<Boolean> provider22, Provider<OnUserInteractionCallback> provider23, Provider<FalsingManager> provider24, Provider<FalsingCollector> provider25, Provider<FeatureFlags> provider26, Provider<PeopleNotificationIdentifier> provider27, Provider<Optional<BubblesManager>> provider28, Provider<ExpandableNotificationRowDragController> provider29) {
        return new ExpandableNotificationRowController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19, provider20, provider21, provider22, provider23, provider24, provider25, provider26, provider27, provider28, provider29);
    }

    public static ExpandableNotificationRowController newInstance(ExpandableNotificationRow expandableNotificationRow, ActivatableNotificationViewController activatableNotificationViewController, RemoteInputViewSubcomponent.Factory factory, MetricsLogger metricsLogger, NotificationListContainer notificationListContainer, NotificationMediaManager notificationMediaManager, SmartReplyConstants smartReplyConstants, SmartReplyController smartReplyController, PluginManager pluginManager, SystemClock systemClock, String str, String str2, KeyguardBypassController keyguardBypassController, GroupMembershipManager groupMembershipManager, GroupExpansionManager groupExpansionManager, RowContentBindStage rowContentBindStage, NotificationLogger notificationLogger, HeadsUpManager headsUpManager, ExpandableNotificationRow.OnExpandClickListener onExpandClickListener, StatusBarStateController statusBarStateController, NotificationGutsManager notificationGutsManager, boolean z, OnUserInteractionCallback onUserInteractionCallback, FalsingManager falsingManager, FalsingCollector falsingCollector, FeatureFlags featureFlags, PeopleNotificationIdentifier peopleNotificationIdentifier, Optional<BubblesManager> optional, ExpandableNotificationRowDragController expandableNotificationRowDragController) {
        return new ExpandableNotificationRowController(expandableNotificationRow, activatableNotificationViewController, factory, metricsLogger, notificationListContainer, notificationMediaManager, smartReplyConstants, smartReplyController, pluginManager, systemClock, str, str2, keyguardBypassController, groupMembershipManager, groupExpansionManager, rowContentBindStage, notificationLogger, headsUpManager, onExpandClickListener, statusBarStateController, notificationGutsManager, z, onUserInteractionCallback, falsingManager, falsingCollector, featureFlags, peopleNotificationIdentifier, optional, expandableNotificationRowDragController);
    }
}
