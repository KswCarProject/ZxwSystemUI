package com.android.systemui.dagger;

import android.app.INotificationManager;
import android.content.Context;
import com.android.internal.statusbar.IStatusBarService;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.model.SysUiState;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProvider;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.ZenModeController;
import com.android.systemui.wmshell.BubblesManager;
import com.android.wm.shell.bubbles.Bubbles;
import dagger.internal.Factory;
import java.util.Optional;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class SystemUIModule_ProvideBubblesManagerFactory implements Factory<Optional<BubblesManager>> {
    public final Provider<Optional<Bubbles>> bubblesOptionalProvider;
    public final Provider<ConfigurationController> configurationControllerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<NotificationEntryManager> entryManagerProvider;
    public final Provider<NotificationGroupManagerLegacy> groupManagerProvider;
    public final Provider<NotificationInterruptStateProvider> interruptionStateProvider;
    public final Provider<KeyguardStateController> keyguardStateControllerProvider;
    public final Provider<CommonNotifCollection> notifCollectionProvider;
    public final Provider<NotifPipelineFlags> notifPipelineFlagsProvider;
    public final Provider<NotifPipeline> notifPipelineProvider;
    public final Provider<NotificationLockscreenUserManager> notifUserManagerProvider;
    public final Provider<INotificationManager> notificationManagerProvider;
    public final Provider<NotificationShadeWindowController> notificationShadeWindowControllerProvider;
    public final Provider<ShadeController> shadeControllerProvider;
    public final Provider<IStatusBarService> statusBarServiceProvider;
    public final Provider<SysUiState> sysUiStateProvider;
    public final Provider<Executor> sysuiMainExecutorProvider;
    public final Provider<NotificationVisibilityProvider> visibilityProvider;
    public final Provider<ZenModeController> zenModeControllerProvider;

    public SystemUIModule_ProvideBubblesManagerFactory(Provider<Context> provider, Provider<Optional<Bubbles>> provider2, Provider<NotificationShadeWindowController> provider3, Provider<KeyguardStateController> provider4, Provider<ShadeController> provider5, Provider<ConfigurationController> provider6, Provider<IStatusBarService> provider7, Provider<INotificationManager> provider8, Provider<NotificationVisibilityProvider> provider9, Provider<NotificationInterruptStateProvider> provider10, Provider<ZenModeController> provider11, Provider<NotificationLockscreenUserManager> provider12, Provider<NotificationGroupManagerLegacy> provider13, Provider<NotificationEntryManager> provider14, Provider<CommonNotifCollection> provider15, Provider<NotifPipeline> provider16, Provider<SysUiState> provider17, Provider<NotifPipelineFlags> provider18, Provider<DumpManager> provider19, Provider<Executor> provider20) {
        this.contextProvider = provider;
        this.bubblesOptionalProvider = provider2;
        this.notificationShadeWindowControllerProvider = provider3;
        this.keyguardStateControllerProvider = provider4;
        this.shadeControllerProvider = provider5;
        this.configurationControllerProvider = provider6;
        this.statusBarServiceProvider = provider7;
        this.notificationManagerProvider = provider8;
        this.visibilityProvider = provider9;
        this.interruptionStateProvider = provider10;
        this.zenModeControllerProvider = provider11;
        this.notifUserManagerProvider = provider12;
        this.groupManagerProvider = provider13;
        this.entryManagerProvider = provider14;
        this.notifCollectionProvider = provider15;
        this.notifPipelineProvider = provider16;
        this.sysUiStateProvider = provider17;
        this.notifPipelineFlagsProvider = provider18;
        this.dumpManagerProvider = provider19;
        this.sysuiMainExecutorProvider = provider20;
    }

    public Optional<BubblesManager> get() {
        return provideBubblesManager(this.contextProvider.get(), this.bubblesOptionalProvider.get(), this.notificationShadeWindowControllerProvider.get(), this.keyguardStateControllerProvider.get(), this.shadeControllerProvider.get(), this.configurationControllerProvider.get(), this.statusBarServiceProvider.get(), this.notificationManagerProvider.get(), this.visibilityProvider.get(), this.interruptionStateProvider.get(), this.zenModeControllerProvider.get(), this.notifUserManagerProvider.get(), this.groupManagerProvider.get(), this.entryManagerProvider.get(), this.notifCollectionProvider.get(), this.notifPipelineProvider.get(), this.sysUiStateProvider.get(), this.notifPipelineFlagsProvider.get(), this.dumpManagerProvider.get(), this.sysuiMainExecutorProvider.get());
    }

    public static SystemUIModule_ProvideBubblesManagerFactory create(Provider<Context> provider, Provider<Optional<Bubbles>> provider2, Provider<NotificationShadeWindowController> provider3, Provider<KeyguardStateController> provider4, Provider<ShadeController> provider5, Provider<ConfigurationController> provider6, Provider<IStatusBarService> provider7, Provider<INotificationManager> provider8, Provider<NotificationVisibilityProvider> provider9, Provider<NotificationInterruptStateProvider> provider10, Provider<ZenModeController> provider11, Provider<NotificationLockscreenUserManager> provider12, Provider<NotificationGroupManagerLegacy> provider13, Provider<NotificationEntryManager> provider14, Provider<CommonNotifCollection> provider15, Provider<NotifPipeline> provider16, Provider<SysUiState> provider17, Provider<NotifPipelineFlags> provider18, Provider<DumpManager> provider19, Provider<Executor> provider20) {
        return new SystemUIModule_ProvideBubblesManagerFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19, provider20);
    }

    /* JADX WARNING: type inference failed for: r2v0, types: [java.util.Optional<com.android.wm.shell.bubbles.Bubbles>, java.util.Optional] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.Optional<com.android.systemui.wmshell.BubblesManager> provideBubblesManager(android.content.Context r1, java.util.Optional<com.android.wm.shell.bubbles.Bubbles> r2, com.android.systemui.statusbar.NotificationShadeWindowController r3, com.android.systemui.statusbar.policy.KeyguardStateController r4, com.android.systemui.statusbar.phone.ShadeController r5, com.android.systemui.statusbar.policy.ConfigurationController r6, com.android.internal.statusbar.IStatusBarService r7, android.app.INotificationManager r8, com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider r9, com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProvider r10, com.android.systemui.statusbar.policy.ZenModeController r11, com.android.systemui.statusbar.NotificationLockscreenUserManager r12, com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy r13, com.android.systemui.statusbar.notification.NotificationEntryManager r14, com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection r15, com.android.systemui.statusbar.notification.collection.NotifPipeline r16, com.android.systemui.model.SysUiState r17, com.android.systemui.statusbar.notification.NotifPipelineFlags r18, com.android.systemui.dump.DumpManager r19, java.util.concurrent.Executor r20) {
        /*
            java.util.Optional r0 = com.android.systemui.dagger.SystemUIModule.provideBubblesManager(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20)
            java.lang.Object r0 = dagger.internal.Preconditions.checkNotNullFromProvides(r0)
            java.util.Optional r0 = (java.util.Optional) r0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.dagger.SystemUIModule_ProvideBubblesManagerFactory.provideBubblesManager(android.content.Context, java.util.Optional, com.android.systemui.statusbar.NotificationShadeWindowController, com.android.systemui.statusbar.policy.KeyguardStateController, com.android.systemui.statusbar.phone.ShadeController, com.android.systemui.statusbar.policy.ConfigurationController, com.android.internal.statusbar.IStatusBarService, android.app.INotificationManager, com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider, com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProvider, com.android.systemui.statusbar.policy.ZenModeController, com.android.systemui.statusbar.NotificationLockscreenUserManager, com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy, com.android.systemui.statusbar.notification.NotificationEntryManager, com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection, com.android.systemui.statusbar.notification.collection.NotifPipeline, com.android.systemui.model.SysUiState, com.android.systemui.statusbar.notification.NotifPipelineFlags, com.android.systemui.dump.DumpManager, java.util.concurrent.Executor):java.util.Optional");
    }
}
