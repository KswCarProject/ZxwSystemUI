package com.android.systemui.statusbar.phone;

import android.view.WindowManager;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;

public final class ShadeControllerImpl_Factory implements Factory<ShadeControllerImpl> {
    public final Provider<AssistManager> assistManagerLazyProvider;
    public final Provider<Optional<CentralSurfaces>> centralSurfacesOptionalLazyProvider;
    public final Provider<CommandQueue> commandQueueProvider;
    public final Provider<NotificationShadeWindowController> notificationShadeWindowControllerProvider;
    public final Provider<StatusBarKeyguardViewManager> statusBarKeyguardViewManagerProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;
    public final Provider<WindowManager> windowManagerProvider;

    public ShadeControllerImpl_Factory(Provider<CommandQueue> provider, Provider<StatusBarStateController> provider2, Provider<NotificationShadeWindowController> provider3, Provider<StatusBarKeyguardViewManager> provider4, Provider<WindowManager> provider5, Provider<Optional<CentralSurfaces>> provider6, Provider<AssistManager> provider7) {
        this.commandQueueProvider = provider;
        this.statusBarStateControllerProvider = provider2;
        this.notificationShadeWindowControllerProvider = provider3;
        this.statusBarKeyguardViewManagerProvider = provider4;
        this.windowManagerProvider = provider5;
        this.centralSurfacesOptionalLazyProvider = provider6;
        this.assistManagerLazyProvider = provider7;
    }

    public ShadeControllerImpl get() {
        return newInstance(this.commandQueueProvider.get(), this.statusBarStateControllerProvider.get(), this.notificationShadeWindowControllerProvider.get(), this.statusBarKeyguardViewManagerProvider.get(), this.windowManagerProvider.get(), DoubleCheck.lazy(this.centralSurfacesOptionalLazyProvider), DoubleCheck.lazy(this.assistManagerLazyProvider));
    }

    public static ShadeControllerImpl_Factory create(Provider<CommandQueue> provider, Provider<StatusBarStateController> provider2, Provider<NotificationShadeWindowController> provider3, Provider<StatusBarKeyguardViewManager> provider4, Provider<WindowManager> provider5, Provider<Optional<CentralSurfaces>> provider6, Provider<AssistManager> provider7) {
        return new ShadeControllerImpl_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7);
    }

    public static ShadeControllerImpl newInstance(CommandQueue commandQueue, StatusBarStateController statusBarStateController, NotificationShadeWindowController notificationShadeWindowController, StatusBarKeyguardViewManager statusBarKeyguardViewManager, WindowManager windowManager, Lazy<Optional<CentralSurfaces>> lazy, Lazy<AssistManager> lazy2) {
        return new ShadeControllerImpl(commandQueue, statusBarStateController, notificationShadeWindowController, statusBarKeyguardViewManager, windowManager, lazy, lazy2);
    }
}
