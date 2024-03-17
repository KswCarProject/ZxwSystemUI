package com.android.systemui.statusbar.phone;

import android.content.Context;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.InitController;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.KeyguardIndicationController;
import com.android.systemui.statusbar.LockscreenShadeTransitionController;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.NotificationViewHierarchyManager;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.notification.DynamicPrivacyController;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.render.NotifShadeEventSource;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProvider;
import com.android.systemui.statusbar.notification.row.NotificationGutsManager;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class StatusBarNotificationPresenter_Factory implements Factory<StatusBarNotificationPresenter> {
    public final Provider<ActivityStarter> activityStarterProvider;
    public final Provider<CentralSurfaces> centralSurfacesProvider;
    public final Provider<CommandQueue> commandQueueProvider;
    public final Provider<ConfigurationController> configurationControllerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DozeScrimController> dozeScrimControllerProvider;
    public final Provider<DynamicPrivacyController> dynamicPrivacyControllerProvider;
    public final Provider<HeadsUpManagerPhone> headsUpProvider;
    public final Provider<InitController> initControllerProvider;
    public final Provider<KeyguardIndicationController> keyguardIndicationControllerProvider;
    public final Provider<KeyguardStateController> keyguardStateControllerProvider;
    public final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    public final Provider<LockscreenGestureLogger> lockscreenGestureLoggerProvider;
    public final Provider<NotificationLockscreenUserManager> lockscreenUserManagerProvider;
    public final Provider<NotifPipelineFlags> notifPipelineFlagsProvider;
    public final Provider<NotifShadeEventSource> notifShadeEventSourceProvider;
    public final Provider<NotificationEntryManager> notificationEntryManagerProvider;
    public final Provider<NotificationGutsManager> notificationGutsManagerProvider;
    public final Provider<NotificationInterruptStateProvider> notificationInterruptStateProvider;
    public final Provider<NotificationListContainer> notificationListContainerProvider;
    public final Provider<NotificationMediaManager> notificationMediaManagerProvider;
    public final Provider<NotificationShadeWindowController> notificationShadeWindowControllerProvider;
    public final Provider<NotificationViewHierarchyManager> notificationViewHierarchyManagerProvider;
    public final Provider<NotificationPanelViewController> panelProvider;
    public final Provider<NotificationRemoteInputManager.Callback> remoteInputManagerCallbackProvider;
    public final Provider<NotificationRemoteInputManager> remoteInputManagerProvider;
    public final Provider<ScrimController> scrimControllerProvider;
    public final Provider<ShadeController> shadeControllerProvider;
    public final Provider<LockscreenShadeTransitionController> shadeTransitionControllerProvider;
    public final Provider<NotificationStackScrollLayoutController> stackScrollerControllerProvider;
    public final Provider<NotificationShadeWindowView> statusBarWindowProvider;
    public final Provider<SysuiStatusBarStateController> sysuiStatusBarStateControllerProvider;

    public StatusBarNotificationPresenter_Factory(Provider<Context> provider, Provider<NotificationPanelViewController> provider2, Provider<HeadsUpManagerPhone> provider3, Provider<NotificationShadeWindowView> provider4, Provider<ActivityStarter> provider5, Provider<NotificationStackScrollLayoutController> provider6, Provider<DozeScrimController> provider7, Provider<ScrimController> provider8, Provider<NotificationShadeWindowController> provider9, Provider<DynamicPrivacyController> provider10, Provider<KeyguardStateController> provider11, Provider<KeyguardIndicationController> provider12, Provider<CentralSurfaces> provider13, Provider<ShadeController> provider14, Provider<LockscreenShadeTransitionController> provider15, Provider<CommandQueue> provider16, Provider<NotificationViewHierarchyManager> provider17, Provider<NotificationLockscreenUserManager> provider18, Provider<SysuiStatusBarStateController> provider19, Provider<NotifShadeEventSource> provider20, Provider<NotificationEntryManager> provider21, Provider<NotificationMediaManager> provider22, Provider<NotificationGutsManager> provider23, Provider<KeyguardUpdateMonitor> provider24, Provider<LockscreenGestureLogger> provider25, Provider<InitController> provider26, Provider<NotificationInterruptStateProvider> provider27, Provider<NotificationRemoteInputManager> provider28, Provider<ConfigurationController> provider29, Provider<NotifPipelineFlags> provider30, Provider<NotificationRemoteInputManager.Callback> provider31, Provider<NotificationListContainer> provider32) {
        this.contextProvider = provider;
        this.panelProvider = provider2;
        this.headsUpProvider = provider3;
        this.statusBarWindowProvider = provider4;
        this.activityStarterProvider = provider5;
        this.stackScrollerControllerProvider = provider6;
        this.dozeScrimControllerProvider = provider7;
        this.scrimControllerProvider = provider8;
        this.notificationShadeWindowControllerProvider = provider9;
        this.dynamicPrivacyControllerProvider = provider10;
        this.keyguardStateControllerProvider = provider11;
        this.keyguardIndicationControllerProvider = provider12;
        this.centralSurfacesProvider = provider13;
        this.shadeControllerProvider = provider14;
        this.shadeTransitionControllerProvider = provider15;
        this.commandQueueProvider = provider16;
        this.notificationViewHierarchyManagerProvider = provider17;
        this.lockscreenUserManagerProvider = provider18;
        this.sysuiStatusBarStateControllerProvider = provider19;
        this.notifShadeEventSourceProvider = provider20;
        this.notificationEntryManagerProvider = provider21;
        this.notificationMediaManagerProvider = provider22;
        this.notificationGutsManagerProvider = provider23;
        this.keyguardUpdateMonitorProvider = provider24;
        this.lockscreenGestureLoggerProvider = provider25;
        this.initControllerProvider = provider26;
        this.notificationInterruptStateProvider = provider27;
        this.remoteInputManagerProvider = provider28;
        this.configurationControllerProvider = provider29;
        this.notifPipelineFlagsProvider = provider30;
        this.remoteInputManagerCallbackProvider = provider31;
        this.notificationListContainerProvider = provider32;
    }

    public StatusBarNotificationPresenter get() {
        return newInstance(this.contextProvider.get(), this.panelProvider.get(), this.headsUpProvider.get(), this.statusBarWindowProvider.get(), this.activityStarterProvider.get(), this.stackScrollerControllerProvider.get(), this.dozeScrimControllerProvider.get(), this.scrimControllerProvider.get(), this.notificationShadeWindowControllerProvider.get(), this.dynamicPrivacyControllerProvider.get(), this.keyguardStateControllerProvider.get(), this.keyguardIndicationControllerProvider.get(), this.centralSurfacesProvider.get(), this.shadeControllerProvider.get(), this.shadeTransitionControllerProvider.get(), this.commandQueueProvider.get(), this.notificationViewHierarchyManagerProvider.get(), this.lockscreenUserManagerProvider.get(), this.sysuiStatusBarStateControllerProvider.get(), this.notifShadeEventSourceProvider.get(), this.notificationEntryManagerProvider.get(), this.notificationMediaManagerProvider.get(), this.notificationGutsManagerProvider.get(), this.keyguardUpdateMonitorProvider.get(), this.lockscreenGestureLoggerProvider.get(), this.initControllerProvider.get(), this.notificationInterruptStateProvider.get(), this.remoteInputManagerProvider.get(), this.configurationControllerProvider.get(), this.notifPipelineFlagsProvider.get(), this.remoteInputManagerCallbackProvider.get(), this.notificationListContainerProvider.get());
    }

    public static StatusBarNotificationPresenter_Factory create(Provider<Context> provider, Provider<NotificationPanelViewController> provider2, Provider<HeadsUpManagerPhone> provider3, Provider<NotificationShadeWindowView> provider4, Provider<ActivityStarter> provider5, Provider<NotificationStackScrollLayoutController> provider6, Provider<DozeScrimController> provider7, Provider<ScrimController> provider8, Provider<NotificationShadeWindowController> provider9, Provider<DynamicPrivacyController> provider10, Provider<KeyguardStateController> provider11, Provider<KeyguardIndicationController> provider12, Provider<CentralSurfaces> provider13, Provider<ShadeController> provider14, Provider<LockscreenShadeTransitionController> provider15, Provider<CommandQueue> provider16, Provider<NotificationViewHierarchyManager> provider17, Provider<NotificationLockscreenUserManager> provider18, Provider<SysuiStatusBarStateController> provider19, Provider<NotifShadeEventSource> provider20, Provider<NotificationEntryManager> provider21, Provider<NotificationMediaManager> provider22, Provider<NotificationGutsManager> provider23, Provider<KeyguardUpdateMonitor> provider24, Provider<LockscreenGestureLogger> provider25, Provider<InitController> provider26, Provider<NotificationInterruptStateProvider> provider27, Provider<NotificationRemoteInputManager> provider28, Provider<ConfigurationController> provider29, Provider<NotifPipelineFlags> provider30, Provider<NotificationRemoteInputManager.Callback> provider31, Provider<NotificationListContainer> provider32) {
        return new StatusBarNotificationPresenter_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19, provider20, provider21, provider22, provider23, provider24, provider25, provider26, provider27, provider28, provider29, provider30, provider31, provider32);
    }

    public static StatusBarNotificationPresenter newInstance(Context context, NotificationPanelViewController notificationPanelViewController, HeadsUpManagerPhone headsUpManagerPhone, NotificationShadeWindowView notificationShadeWindowView, ActivityStarter activityStarter, NotificationStackScrollLayoutController notificationStackScrollLayoutController, DozeScrimController dozeScrimController, ScrimController scrimController, NotificationShadeWindowController notificationShadeWindowController, DynamicPrivacyController dynamicPrivacyController, KeyguardStateController keyguardStateController, KeyguardIndicationController keyguardIndicationController, CentralSurfaces centralSurfaces, ShadeController shadeController, LockscreenShadeTransitionController lockscreenShadeTransitionController, CommandQueue commandQueue, NotificationViewHierarchyManager notificationViewHierarchyManager, NotificationLockscreenUserManager notificationLockscreenUserManager, SysuiStatusBarStateController sysuiStatusBarStateController, NotifShadeEventSource notifShadeEventSource, NotificationEntryManager notificationEntryManager, NotificationMediaManager notificationMediaManager, NotificationGutsManager notificationGutsManager, KeyguardUpdateMonitor keyguardUpdateMonitor, LockscreenGestureLogger lockscreenGestureLogger, InitController initController, NotificationInterruptStateProvider notificationInterruptStateProvider2, NotificationRemoteInputManager notificationRemoteInputManager, ConfigurationController configurationController, NotifPipelineFlags notifPipelineFlags, NotificationRemoteInputManager.Callback callback, NotificationListContainer notificationListContainer) {
        return new StatusBarNotificationPresenter(context, notificationPanelViewController, headsUpManagerPhone, notificationShadeWindowView, activityStarter, notificationStackScrollLayoutController, dozeScrimController, scrimController, notificationShadeWindowController, dynamicPrivacyController, keyguardStateController, keyguardIndicationController, centralSurfaces, shadeController, lockscreenShadeTransitionController, commandQueue, notificationViewHierarchyManager, notificationLockscreenUserManager, sysuiStatusBarStateController, notifShadeEventSource, notificationEntryManager, notificationMediaManager, notificationGutsManager, keyguardUpdateMonitor, lockscreenGestureLogger, initController, notificationInterruptStateProvider2, notificationRemoteInputManager, configurationController, notifPipelineFlags, callback, notificationListContainer);
    }
}
