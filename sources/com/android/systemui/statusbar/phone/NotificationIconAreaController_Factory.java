package com.android.systemui.statusbar.phone;

import android.content.Context;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator;
import com.android.systemui.statusbar.window.StatusBarWindowController;
import com.android.wm.shell.bubbles.Bubbles;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;

public final class NotificationIconAreaController_Factory implements Factory<NotificationIconAreaController> {
    public final Provider<Optional<Bubbles>> bubblesOptionalProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DarkIconDispatcher> darkIconDispatcherProvider;
    public final Provider<DemoModeController> demoModeControllerProvider;
    public final Provider<DozeParameters> dozeParametersProvider;
    public final Provider<KeyguardBypassController> keyguardBypassControllerProvider;
    public final Provider<NotificationListener> notificationListenerProvider;
    public final Provider<NotificationMediaManager> notificationMediaManagerProvider;
    public final Provider<ScreenOffAnimationController> screenOffAnimationControllerProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;
    public final Provider<StatusBarWindowController> statusBarWindowControllerProvider;
    public final Provider<NotificationWakeUpCoordinator> wakeUpCoordinatorProvider;

    public NotificationIconAreaController_Factory(Provider<Context> provider, Provider<StatusBarStateController> provider2, Provider<NotificationWakeUpCoordinator> provider3, Provider<KeyguardBypassController> provider4, Provider<NotificationMediaManager> provider5, Provider<NotificationListener> provider6, Provider<DozeParameters> provider7, Provider<Optional<Bubbles>> provider8, Provider<DemoModeController> provider9, Provider<DarkIconDispatcher> provider10, Provider<StatusBarWindowController> provider11, Provider<ScreenOffAnimationController> provider12) {
        this.contextProvider = provider;
        this.statusBarStateControllerProvider = provider2;
        this.wakeUpCoordinatorProvider = provider3;
        this.keyguardBypassControllerProvider = provider4;
        this.notificationMediaManagerProvider = provider5;
        this.notificationListenerProvider = provider6;
        this.dozeParametersProvider = provider7;
        this.bubblesOptionalProvider = provider8;
        this.demoModeControllerProvider = provider9;
        this.darkIconDispatcherProvider = provider10;
        this.statusBarWindowControllerProvider = provider11;
        this.screenOffAnimationControllerProvider = provider12;
    }

    public NotificationIconAreaController get() {
        return newInstance(this.contextProvider.get(), this.statusBarStateControllerProvider.get(), this.wakeUpCoordinatorProvider.get(), this.keyguardBypassControllerProvider.get(), this.notificationMediaManagerProvider.get(), this.notificationListenerProvider.get(), this.dozeParametersProvider.get(), this.bubblesOptionalProvider.get(), this.demoModeControllerProvider.get(), this.darkIconDispatcherProvider.get(), this.statusBarWindowControllerProvider.get(), this.screenOffAnimationControllerProvider.get());
    }

    public static NotificationIconAreaController_Factory create(Provider<Context> provider, Provider<StatusBarStateController> provider2, Provider<NotificationWakeUpCoordinator> provider3, Provider<KeyguardBypassController> provider4, Provider<NotificationMediaManager> provider5, Provider<NotificationListener> provider6, Provider<DozeParameters> provider7, Provider<Optional<Bubbles>> provider8, Provider<DemoModeController> provider9, Provider<DarkIconDispatcher> provider10, Provider<StatusBarWindowController> provider11, Provider<ScreenOffAnimationController> provider12) {
        return new NotificationIconAreaController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12);
    }

    public static NotificationIconAreaController newInstance(Context context, StatusBarStateController statusBarStateController, NotificationWakeUpCoordinator notificationWakeUpCoordinator, KeyguardBypassController keyguardBypassController, NotificationMediaManager notificationMediaManager, NotificationListener notificationListener, DozeParameters dozeParameters, Optional<Bubbles> optional, DemoModeController demoModeController, DarkIconDispatcher darkIconDispatcher, StatusBarWindowController statusBarWindowController, ScreenOffAnimationController screenOffAnimationController) {
        return new NotificationIconAreaController(context, statusBarStateController, notificationWakeUpCoordinator, keyguardBypassController, notificationMediaManager, notificationListener, dozeParameters, optional, demoModeController, darkIconDispatcher, statusBarWindowController, screenOffAnimationController);
    }
}
