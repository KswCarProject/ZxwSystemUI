package com.android.systemui.statusbar.dagger;

import android.content.Context;
import android.os.Handler;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationViewHierarchyManager;
import com.android.systemui.statusbar.notification.AssistantFeedbackController;
import com.android.systemui.statusbar.notification.DynamicChildBindController;
import com.android.systemui.statusbar.notification.DynamicPrivacyController;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.legacy.LowPriorityInflationHelper;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.wm.shell.bubbles.Bubbles;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;

public final class CentralSurfacesDependenciesModule_ProvideNotificationViewHierarchyManagerFactory implements Factory<NotificationViewHierarchyManager> {
    public final Provider<AssistantFeedbackController> assistantFeedbackControllerProvider;
    public final Provider<Optional<Bubbles>> bubblesOptionalProvider;
    public final Provider<KeyguardBypassController> bypassControllerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DynamicChildBindController> dynamicChildBindControllerProvider;
    public final Provider<FeatureFlags> featureFlagsProvider;
    public final Provider<NotificationGroupManagerLegacy> groupManagerProvider;
    public final Provider<KeyguardStateController> keyguardStateControllerProvider;
    public final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    public final Provider<LowPriorityInflationHelper> lowPriorityInflationHelperProvider;
    public final Provider<Handler> mainHandlerProvider;
    public final Provider<NotifPipelineFlags> notifPipelineFlagsProvider;
    public final Provider<NotificationEntryManager> notificationEntryManagerProvider;
    public final Provider<NotificationLockscreenUserManager> notificationLockscreenUserManagerProvider;
    public final Provider<DynamicPrivacyController> privacyControllerProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;
    public final Provider<VisualStabilityManager> visualStabilityManagerProvider;

    public CentralSurfacesDependenciesModule_ProvideNotificationViewHierarchyManagerFactory(Provider<Context> provider, Provider<Handler> provider2, Provider<FeatureFlags> provider3, Provider<NotificationLockscreenUserManager> provider4, Provider<NotificationGroupManagerLegacy> provider5, Provider<VisualStabilityManager> provider6, Provider<StatusBarStateController> provider7, Provider<NotificationEntryManager> provider8, Provider<KeyguardBypassController> provider9, Provider<Optional<Bubbles>> provider10, Provider<DynamicPrivacyController> provider11, Provider<DynamicChildBindController> provider12, Provider<LowPriorityInflationHelper> provider13, Provider<AssistantFeedbackController> provider14, Provider<NotifPipelineFlags> provider15, Provider<KeyguardUpdateMonitor> provider16, Provider<KeyguardStateController> provider17) {
        this.contextProvider = provider;
        this.mainHandlerProvider = provider2;
        this.featureFlagsProvider = provider3;
        this.notificationLockscreenUserManagerProvider = provider4;
        this.groupManagerProvider = provider5;
        this.visualStabilityManagerProvider = provider6;
        this.statusBarStateControllerProvider = provider7;
        this.notificationEntryManagerProvider = provider8;
        this.bypassControllerProvider = provider9;
        this.bubblesOptionalProvider = provider10;
        this.privacyControllerProvider = provider11;
        this.dynamicChildBindControllerProvider = provider12;
        this.lowPriorityInflationHelperProvider = provider13;
        this.assistantFeedbackControllerProvider = provider14;
        this.notifPipelineFlagsProvider = provider15;
        this.keyguardUpdateMonitorProvider = provider16;
        this.keyguardStateControllerProvider = provider17;
    }

    public NotificationViewHierarchyManager get() {
        return provideNotificationViewHierarchyManager(this.contextProvider.get(), this.mainHandlerProvider.get(), this.featureFlagsProvider.get(), this.notificationLockscreenUserManagerProvider.get(), this.groupManagerProvider.get(), this.visualStabilityManagerProvider.get(), this.statusBarStateControllerProvider.get(), this.notificationEntryManagerProvider.get(), this.bypassControllerProvider.get(), this.bubblesOptionalProvider.get(), this.privacyControllerProvider.get(), this.dynamicChildBindControllerProvider.get(), this.lowPriorityInflationHelperProvider.get(), this.assistantFeedbackControllerProvider.get(), this.notifPipelineFlagsProvider.get(), this.keyguardUpdateMonitorProvider.get(), this.keyguardStateControllerProvider.get());
    }

    public static CentralSurfacesDependenciesModule_ProvideNotificationViewHierarchyManagerFactory create(Provider<Context> provider, Provider<Handler> provider2, Provider<FeatureFlags> provider3, Provider<NotificationLockscreenUserManager> provider4, Provider<NotificationGroupManagerLegacy> provider5, Provider<VisualStabilityManager> provider6, Provider<StatusBarStateController> provider7, Provider<NotificationEntryManager> provider8, Provider<KeyguardBypassController> provider9, Provider<Optional<Bubbles>> provider10, Provider<DynamicPrivacyController> provider11, Provider<DynamicChildBindController> provider12, Provider<LowPriorityInflationHelper> provider13, Provider<AssistantFeedbackController> provider14, Provider<NotifPipelineFlags> provider15, Provider<KeyguardUpdateMonitor> provider16, Provider<KeyguardStateController> provider17) {
        return new CentralSurfacesDependenciesModule_ProvideNotificationViewHierarchyManagerFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17);
    }

    public static NotificationViewHierarchyManager provideNotificationViewHierarchyManager(Context context, Handler handler, FeatureFlags featureFlags, NotificationLockscreenUserManager notificationLockscreenUserManager, NotificationGroupManagerLegacy notificationGroupManagerLegacy, VisualStabilityManager visualStabilityManager, StatusBarStateController statusBarStateController, NotificationEntryManager notificationEntryManager, KeyguardBypassController keyguardBypassController, Optional<Bubbles> optional, DynamicPrivacyController dynamicPrivacyController, DynamicChildBindController dynamicChildBindController, LowPriorityInflationHelper lowPriorityInflationHelper, AssistantFeedbackController assistantFeedbackController, NotifPipelineFlags notifPipelineFlags, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardStateController keyguardStateController) {
        return (NotificationViewHierarchyManager) Preconditions.checkNotNullFromProvides(CentralSurfacesDependenciesModule.provideNotificationViewHierarchyManager(context, handler, featureFlags, notificationLockscreenUserManager, notificationGroupManagerLegacy, visualStabilityManager, statusBarStateController, notificationEntryManager, keyguardBypassController, optional, dynamicPrivacyController, dynamicChildBindController, lowPriorityInflationHelper, assistantFeedbackController, notifPipelineFlags, keyguardUpdateMonitor, keyguardStateController));
    }
}
