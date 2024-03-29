package com.android.systemui.statusbar.phone;

import android.content.Context;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.ActionClickLogger;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.notification.collection.render.GroupExpansionManager;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class StatusBarRemoteInputCallback_Factory implements Factory<StatusBarRemoteInputCallback> {
    public final Provider<ActivityStarter> activityStarterProvider;
    public final Provider<ActionClickLogger> clickLoggerProvider;
    public final Provider<CommandQueue> commandQueueProvider;
    public final Provider<Context> contextProvider;
    public final Provider<Executor> executorProvider;
    public final Provider<GroupExpansionManager> groupExpansionManagerProvider;
    public final Provider<KeyguardStateController> keyguardStateControllerProvider;
    public final Provider<NotificationLockscreenUserManager> notificationLockscreenUserManagerProvider;
    public final Provider<ShadeController> shadeControllerProvider;
    public final Provider<StatusBarKeyguardViewManager> statusBarKeyguardViewManagerProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;

    public StatusBarRemoteInputCallback_Factory(Provider<Context> provider, Provider<GroupExpansionManager> provider2, Provider<NotificationLockscreenUserManager> provider3, Provider<KeyguardStateController> provider4, Provider<StatusBarStateController> provider5, Provider<StatusBarKeyguardViewManager> provider6, Provider<ActivityStarter> provider7, Provider<ShadeController> provider8, Provider<CommandQueue> provider9, Provider<ActionClickLogger> provider10, Provider<Executor> provider11) {
        this.contextProvider = provider;
        this.groupExpansionManagerProvider = provider2;
        this.notificationLockscreenUserManagerProvider = provider3;
        this.keyguardStateControllerProvider = provider4;
        this.statusBarStateControllerProvider = provider5;
        this.statusBarKeyguardViewManagerProvider = provider6;
        this.activityStarterProvider = provider7;
        this.shadeControllerProvider = provider8;
        this.commandQueueProvider = provider9;
        this.clickLoggerProvider = provider10;
        this.executorProvider = provider11;
    }

    public StatusBarRemoteInputCallback get() {
        return newInstance(this.contextProvider.get(), this.groupExpansionManagerProvider.get(), this.notificationLockscreenUserManagerProvider.get(), this.keyguardStateControllerProvider.get(), this.statusBarStateControllerProvider.get(), this.statusBarKeyguardViewManagerProvider.get(), this.activityStarterProvider.get(), this.shadeControllerProvider.get(), this.commandQueueProvider.get(), this.clickLoggerProvider.get(), this.executorProvider.get());
    }

    public static StatusBarRemoteInputCallback_Factory create(Provider<Context> provider, Provider<GroupExpansionManager> provider2, Provider<NotificationLockscreenUserManager> provider3, Provider<KeyguardStateController> provider4, Provider<StatusBarStateController> provider5, Provider<StatusBarKeyguardViewManager> provider6, Provider<ActivityStarter> provider7, Provider<ShadeController> provider8, Provider<CommandQueue> provider9, Provider<ActionClickLogger> provider10, Provider<Executor> provider11) {
        return new StatusBarRemoteInputCallback_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11);
    }

    public static StatusBarRemoteInputCallback newInstance(Context context, GroupExpansionManager groupExpansionManager, NotificationLockscreenUserManager notificationLockscreenUserManager, KeyguardStateController keyguardStateController, StatusBarStateController statusBarStateController, StatusBarKeyguardViewManager statusBarKeyguardViewManager, ActivityStarter activityStarter, ShadeController shadeController, CommandQueue commandQueue, ActionClickLogger actionClickLogger, Executor executor) {
        return new StatusBarRemoteInputCallback(context, groupExpansionManager, notificationLockscreenUserManager, keyguardStateController, statusBarStateController, statusBarKeyguardViewManager, activityStarter, shadeController, commandQueue, actionClickLogger, executor);
    }
}
