package com.android.systemui.statusbar.phone;

import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.notification.init.NotificationsController;
import com.android.systemui.statusbar.window.StatusBarWindowController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class StatusBarHeadsUpChangeListener_Factory implements Factory<StatusBarHeadsUpChangeListener> {
    public final Provider<DozeScrimController> dozeScrimControllerProvider;
    public final Provider<DozeServiceHost> dozeServiceHostProvider;
    public final Provider<HeadsUpManagerPhone> headsUpManagerProvider;
    public final Provider<KeyguardBypassController> keyguardBypassControllerProvider;
    public final Provider<NotificationPanelViewController> notificationPanelViewControllerProvider;
    public final Provider<NotificationRemoteInputManager> notificationRemoteInputManagerProvider;
    public final Provider<NotificationShadeWindowController> notificationShadeWindowControllerProvider;
    public final Provider<NotificationsController> notificationsControllerProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;
    public final Provider<StatusBarWindowController> statusBarWindowControllerProvider;

    public StatusBarHeadsUpChangeListener_Factory(Provider<NotificationShadeWindowController> provider, Provider<StatusBarWindowController> provider2, Provider<NotificationPanelViewController> provider3, Provider<KeyguardBypassController> provider4, Provider<HeadsUpManagerPhone> provider5, Provider<StatusBarStateController> provider6, Provider<NotificationRemoteInputManager> provider7, Provider<NotificationsController> provider8, Provider<DozeServiceHost> provider9, Provider<DozeScrimController> provider10) {
        this.notificationShadeWindowControllerProvider = provider;
        this.statusBarWindowControllerProvider = provider2;
        this.notificationPanelViewControllerProvider = provider3;
        this.keyguardBypassControllerProvider = provider4;
        this.headsUpManagerProvider = provider5;
        this.statusBarStateControllerProvider = provider6;
        this.notificationRemoteInputManagerProvider = provider7;
        this.notificationsControllerProvider = provider8;
        this.dozeServiceHostProvider = provider9;
        this.dozeScrimControllerProvider = provider10;
    }

    public StatusBarHeadsUpChangeListener get() {
        return newInstance(this.notificationShadeWindowControllerProvider.get(), this.statusBarWindowControllerProvider.get(), this.notificationPanelViewControllerProvider.get(), this.keyguardBypassControllerProvider.get(), this.headsUpManagerProvider.get(), this.statusBarStateControllerProvider.get(), this.notificationRemoteInputManagerProvider.get(), this.notificationsControllerProvider.get(), this.dozeServiceHostProvider.get(), this.dozeScrimControllerProvider.get());
    }

    public static StatusBarHeadsUpChangeListener_Factory create(Provider<NotificationShadeWindowController> provider, Provider<StatusBarWindowController> provider2, Provider<NotificationPanelViewController> provider3, Provider<KeyguardBypassController> provider4, Provider<HeadsUpManagerPhone> provider5, Provider<StatusBarStateController> provider6, Provider<NotificationRemoteInputManager> provider7, Provider<NotificationsController> provider8, Provider<DozeServiceHost> provider9, Provider<DozeScrimController> provider10) {
        return new StatusBarHeadsUpChangeListener_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10);
    }

    public static StatusBarHeadsUpChangeListener newInstance(NotificationShadeWindowController notificationShadeWindowController, StatusBarWindowController statusBarWindowController, NotificationPanelViewController notificationPanelViewController, KeyguardBypassController keyguardBypassController, HeadsUpManagerPhone headsUpManagerPhone, StatusBarStateController statusBarStateController, NotificationRemoteInputManager notificationRemoteInputManager, NotificationsController notificationsController, DozeServiceHost dozeServiceHost, DozeScrimController dozeScrimController) {
        return new StatusBarHeadsUpChangeListener(notificationShadeWindowController, statusBarWindowController, notificationPanelViewController, keyguardBypassController, headsUpManagerPhone, statusBarStateController, notificationRemoteInputManager, notificationsController, dozeServiceHost, dozeScrimController);
    }
}
