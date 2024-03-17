package com.android.systemui.statusbar.notification.stack;

import android.content.res.Resources;
import android.view.LayoutInflater;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.statusbar.IStatusBarService;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.colorextraction.SysuiColorExtractor;
import com.android.systemui.media.KeyguardMediaController;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.LockscreenShadeTransitionController;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.notification.DynamicPrivacyController;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotifCollection;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager;
import com.android.systemui.statusbar.notification.collection.render.GroupExpansionManager;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import com.android.systemui.statusbar.notification.collection.render.SectionHeaderController;
import com.android.systemui.statusbar.notification.row.NotificationGutsManager;
import com.android.systemui.statusbar.notification.stack.NotificationSwipeHelper;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.phone.HeadsUpManagerPhone;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.ScrimController;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.phone.shade.transition.ShadeTransitionController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.ZenModeController;
import com.android.systemui.tuner.TunerService;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class NotificationStackScrollLayoutController_Factory implements Factory<NotificationStackScrollLayoutController> {
    public final Provider<Boolean> allowLongPressProvider;
    public final Provider<CentralSurfaces> centralSurfacesProvider;
    public final Provider<SysuiColorExtractor> colorExtractorProvider;
    public final Provider<ConfigurationController> configurationControllerProvider;
    public final Provider<DeviceProvisionedController> deviceProvisionedControllerProvider;
    public final Provider<DynamicPrivacyController> dynamicPrivacyControllerProvider;
    public final Provider<FalsingCollector> falsingCollectorProvider;
    public final Provider<FalsingManager> falsingManagerProvider;
    public final Provider<GroupExpansionManager> groupManagerProvider;
    public final Provider<HeadsUpManagerPhone> headsUpManagerProvider;
    public final Provider<IStatusBarService> iStatusBarServiceProvider;
    public final Provider<InteractionJankMonitor> jankMonitorProvider;
    public final Provider<KeyguardBypassController> keyguardBypassControllerProvider;
    public final Provider<KeyguardMediaController> keyguardMediaControllerProvider;
    public final Provider<LayoutInflater> layoutInflaterProvider;
    public final Provider<NotificationGroupManagerLegacy> legacyGroupManagerProvider;
    public final Provider<LockscreenShadeTransitionController> lockscreenShadeTransitionControllerProvider;
    public final Provider<NotificationLockscreenUserManager> lockscreenUserManagerProvider;
    public final Provider<NotificationStackScrollLogger> loggerProvider;
    public final Provider<MetricsLogger> metricsLoggerProvider;
    public final Provider<NotifCollection> notifCollectionProvider;
    public final Provider<NotifPipelineFlags> notifPipelineFlagsProvider;
    public final Provider<NotifPipeline> notifPipelineProvider;
    public final Provider<NotificationEntryManager> notificationEntryManagerProvider;
    public final Provider<NotificationGutsManager> notificationGutsManagerProvider;
    public final Provider<NotificationRoundnessManager> notificationRoundnessManagerProvider;
    public final Provider<NotificationStackSizeCalculator> notificationStackSizeCalculatorProvider;
    public final Provider<NotificationSwipeHelper.Builder> notificationSwipeHelperBuilderProvider;
    public final Provider<NotificationRemoteInputManager> remoteInputManagerProvider;
    public final Provider<Resources> resourcesProvider;
    public final Provider<ScrimController> scrimControllerProvider;
    public final Provider<ShadeController> shadeControllerProvider;
    public final Provider<ShadeTransitionController> shadeTransitionControllerProvider;
    public final Provider<SectionHeaderController> silentHeaderControllerProvider;
    public final Provider<StackStateLogger> stackLoggerProvider;
    public final Provider<SysuiStatusBarStateController> statusBarStateControllerProvider;
    public final Provider<TunerService> tunerServiceProvider;
    public final Provider<UiEventLogger> uiEventLoggerProvider;
    public final Provider<NotificationVisibilityProvider> visibilityProvider;
    public final Provider<VisualStabilityManager> visualStabilityManagerProvider;
    public final Provider<ZenModeController> zenModeControllerProvider;

    public NotificationStackScrollLayoutController_Factory(Provider<Boolean> provider, Provider<NotificationGutsManager> provider2, Provider<NotificationVisibilityProvider> provider3, Provider<HeadsUpManagerPhone> provider4, Provider<NotificationRoundnessManager> provider5, Provider<TunerService> provider6, Provider<DeviceProvisionedController> provider7, Provider<DynamicPrivacyController> provider8, Provider<ConfigurationController> provider9, Provider<SysuiStatusBarStateController> provider10, Provider<KeyguardMediaController> provider11, Provider<KeyguardBypassController> provider12, Provider<ZenModeController> provider13, Provider<SysuiColorExtractor> provider14, Provider<NotificationLockscreenUserManager> provider15, Provider<MetricsLogger> provider16, Provider<FalsingCollector> provider17, Provider<FalsingManager> provider18, Provider<Resources> provider19, Provider<NotificationSwipeHelper.Builder> provider20, Provider<CentralSurfaces> provider21, Provider<ScrimController> provider22, Provider<NotificationGroupManagerLegacy> provider23, Provider<GroupExpansionManager> provider24, Provider<SectionHeaderController> provider25, Provider<NotifPipelineFlags> provider26, Provider<NotifPipeline> provider27, Provider<NotifCollection> provider28, Provider<NotificationEntryManager> provider29, Provider<LockscreenShadeTransitionController> provider30, Provider<ShadeTransitionController> provider31, Provider<IStatusBarService> provider32, Provider<UiEventLogger> provider33, Provider<LayoutInflater> provider34, Provider<NotificationRemoteInputManager> provider35, Provider<VisualStabilityManager> provider36, Provider<ShadeController> provider37, Provider<InteractionJankMonitor> provider38, Provider<StackStateLogger> provider39, Provider<NotificationStackScrollLogger> provider40, Provider<NotificationStackSizeCalculator> provider41) {
        this.allowLongPressProvider = provider;
        this.notificationGutsManagerProvider = provider2;
        this.visibilityProvider = provider3;
        this.headsUpManagerProvider = provider4;
        this.notificationRoundnessManagerProvider = provider5;
        this.tunerServiceProvider = provider6;
        this.deviceProvisionedControllerProvider = provider7;
        this.dynamicPrivacyControllerProvider = provider8;
        this.configurationControllerProvider = provider9;
        this.statusBarStateControllerProvider = provider10;
        this.keyguardMediaControllerProvider = provider11;
        this.keyguardBypassControllerProvider = provider12;
        this.zenModeControllerProvider = provider13;
        this.colorExtractorProvider = provider14;
        this.lockscreenUserManagerProvider = provider15;
        this.metricsLoggerProvider = provider16;
        this.falsingCollectorProvider = provider17;
        this.falsingManagerProvider = provider18;
        this.resourcesProvider = provider19;
        this.notificationSwipeHelperBuilderProvider = provider20;
        this.centralSurfacesProvider = provider21;
        this.scrimControllerProvider = provider22;
        this.legacyGroupManagerProvider = provider23;
        this.groupManagerProvider = provider24;
        this.silentHeaderControllerProvider = provider25;
        this.notifPipelineFlagsProvider = provider26;
        this.notifPipelineProvider = provider27;
        this.notifCollectionProvider = provider28;
        this.notificationEntryManagerProvider = provider29;
        this.lockscreenShadeTransitionControllerProvider = provider30;
        this.shadeTransitionControllerProvider = provider31;
        this.iStatusBarServiceProvider = provider32;
        this.uiEventLoggerProvider = provider33;
        this.layoutInflaterProvider = provider34;
        this.remoteInputManagerProvider = provider35;
        this.visualStabilityManagerProvider = provider36;
        this.shadeControllerProvider = provider37;
        this.jankMonitorProvider = provider38;
        this.stackLoggerProvider = provider39;
        this.loggerProvider = provider40;
        this.notificationStackSizeCalculatorProvider = provider41;
    }

    public NotificationStackScrollLayoutController get() {
        return newInstance(this.allowLongPressProvider.get().booleanValue(), this.notificationGutsManagerProvider.get(), this.visibilityProvider.get(), this.headsUpManagerProvider.get(), this.notificationRoundnessManagerProvider.get(), this.tunerServiceProvider.get(), this.deviceProvisionedControllerProvider.get(), this.dynamicPrivacyControllerProvider.get(), this.configurationControllerProvider.get(), this.statusBarStateControllerProvider.get(), this.keyguardMediaControllerProvider.get(), this.keyguardBypassControllerProvider.get(), this.zenModeControllerProvider.get(), this.colorExtractorProvider.get(), this.lockscreenUserManagerProvider.get(), this.metricsLoggerProvider.get(), this.falsingCollectorProvider.get(), this.falsingManagerProvider.get(), this.resourcesProvider.get(), this.notificationSwipeHelperBuilderProvider.get(), this.centralSurfacesProvider.get(), this.scrimControllerProvider.get(), this.legacyGroupManagerProvider.get(), this.groupManagerProvider.get(), this.silentHeaderControllerProvider.get(), this.notifPipelineFlagsProvider.get(), this.notifPipelineProvider.get(), this.notifCollectionProvider.get(), this.notificationEntryManagerProvider.get(), this.lockscreenShadeTransitionControllerProvider.get(), this.shadeTransitionControllerProvider.get(), this.iStatusBarServiceProvider.get(), this.uiEventLoggerProvider.get(), this.layoutInflaterProvider.get(), this.remoteInputManagerProvider.get(), this.visualStabilityManagerProvider.get(), this.shadeControllerProvider.get(), this.jankMonitorProvider.get(), this.stackLoggerProvider.get(), this.loggerProvider.get(), this.notificationStackSizeCalculatorProvider.get());
    }

    public static NotificationStackScrollLayoutController_Factory create(Provider<Boolean> provider, Provider<NotificationGutsManager> provider2, Provider<NotificationVisibilityProvider> provider3, Provider<HeadsUpManagerPhone> provider4, Provider<NotificationRoundnessManager> provider5, Provider<TunerService> provider6, Provider<DeviceProvisionedController> provider7, Provider<DynamicPrivacyController> provider8, Provider<ConfigurationController> provider9, Provider<SysuiStatusBarStateController> provider10, Provider<KeyguardMediaController> provider11, Provider<KeyguardBypassController> provider12, Provider<ZenModeController> provider13, Provider<SysuiColorExtractor> provider14, Provider<NotificationLockscreenUserManager> provider15, Provider<MetricsLogger> provider16, Provider<FalsingCollector> provider17, Provider<FalsingManager> provider18, Provider<Resources> provider19, Provider<NotificationSwipeHelper.Builder> provider20, Provider<CentralSurfaces> provider21, Provider<ScrimController> provider22, Provider<NotificationGroupManagerLegacy> provider23, Provider<GroupExpansionManager> provider24, Provider<SectionHeaderController> provider25, Provider<NotifPipelineFlags> provider26, Provider<NotifPipeline> provider27, Provider<NotifCollection> provider28, Provider<NotificationEntryManager> provider29, Provider<LockscreenShadeTransitionController> provider30, Provider<ShadeTransitionController> provider31, Provider<IStatusBarService> provider32, Provider<UiEventLogger> provider33, Provider<LayoutInflater> provider34, Provider<NotificationRemoteInputManager> provider35, Provider<VisualStabilityManager> provider36, Provider<ShadeController> provider37, Provider<InteractionJankMonitor> provider38, Provider<StackStateLogger> provider39, Provider<NotificationStackScrollLogger> provider40, Provider<NotificationStackSizeCalculator> provider41) {
        return new NotificationStackScrollLayoutController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19, provider20, provider21, provider22, provider23, provider24, provider25, provider26, provider27, provider28, provider29, provider30, provider31, provider32, provider33, provider34, provider35, provider36, provider37, provider38, provider39, provider40, provider41);
    }

    public static NotificationStackScrollLayoutController newInstance(boolean z, NotificationGutsManager notificationGutsManager, NotificationVisibilityProvider notificationVisibilityProvider, HeadsUpManagerPhone headsUpManagerPhone, NotificationRoundnessManager notificationRoundnessManager, TunerService tunerService, DeviceProvisionedController deviceProvisionedController, DynamicPrivacyController dynamicPrivacyController, ConfigurationController configurationController, SysuiStatusBarStateController sysuiStatusBarStateController, KeyguardMediaController keyguardMediaController, KeyguardBypassController keyguardBypassController, ZenModeController zenModeController, SysuiColorExtractor sysuiColorExtractor, NotificationLockscreenUserManager notificationLockscreenUserManager, MetricsLogger metricsLogger, FalsingCollector falsingCollector, FalsingManager falsingManager, Resources resources, Object obj, CentralSurfaces centralSurfaces, ScrimController scrimController, NotificationGroupManagerLegacy notificationGroupManagerLegacy, GroupExpansionManager groupExpansionManager, SectionHeaderController sectionHeaderController, NotifPipelineFlags notifPipelineFlags, NotifPipeline notifPipeline, NotifCollection notifCollection, NotificationEntryManager notificationEntryManager, LockscreenShadeTransitionController lockscreenShadeTransitionController, ShadeTransitionController shadeTransitionController, IStatusBarService iStatusBarService, UiEventLogger uiEventLogger, LayoutInflater layoutInflater, NotificationRemoteInputManager notificationRemoteInputManager, VisualStabilityManager visualStabilityManager, ShadeController shadeController, InteractionJankMonitor interactionJankMonitor, StackStateLogger stackStateLogger, NotificationStackScrollLogger notificationStackScrollLogger, NotificationStackSizeCalculator notificationStackSizeCalculator) {
        return new NotificationStackScrollLayoutController(z, notificationGutsManager, notificationVisibilityProvider, headsUpManagerPhone, notificationRoundnessManager, tunerService, deviceProvisionedController, dynamicPrivacyController, configurationController, sysuiStatusBarStateController, keyguardMediaController, keyguardBypassController, zenModeController, sysuiColorExtractor, notificationLockscreenUserManager, metricsLogger, falsingCollector, falsingManager, resources, (NotificationSwipeHelper.Builder) obj, centralSurfaces, scrimController, notificationGroupManagerLegacy, groupExpansionManager, sectionHeaderController, notifPipelineFlags, notifPipeline, notifCollection, notificationEntryManager, lockscreenShadeTransitionController, shadeTransitionController, iStatusBarService, uiEventLogger, layoutInflater, notificationRemoteInputManager, visualStabilityManager, shadeController, interactionJankMonitor, stackStateLogger, notificationStackScrollLogger, notificationStackSizeCalculator);
    }
}
