package com.android.systemui.statusbar;

import android.content.Context;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.media.MediaHierarchyManager;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.LockscreenShadeKeyguardTransitionController;
import com.android.systemui.statusbar.SingleShadeLockScreenOverScroller;
import com.android.systemui.statusbar.SplitShadeLockScreenOverScroller;
import com.android.systemui.statusbar.notification.stack.AmbientState;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.LSShadeTransitionLogger;
import com.android.systemui.statusbar.policy.ConfigurationController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class LockscreenShadeTransitionController_Factory implements Factory<LockscreenShadeTransitionController> {
    public final Provider<AmbientState> ambientStateProvider;
    public final Provider<ConfigurationController> configurationControllerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<NotificationShadeDepthController> depthControllerProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<FalsingCollector> falsingCollectorProvider;
    public final Provider<FalsingManager> falsingManagerProvider;
    public final Provider<KeyguardBypassController> keyguardBypassControllerProvider;
    public final Provider<LockscreenShadeKeyguardTransitionController.Factory> keyguardTransitionControllerFactoryProvider;
    public final Provider<NotificationLockscreenUserManager> lockScreenUserManagerProvider;
    public final Provider<LSShadeTransitionLogger> loggerProvider;
    public final Provider<MediaHierarchyManager> mediaHierarchyManagerProvider;
    public final Provider<LockscreenShadeScrimTransitionController> scrimTransitionControllerProvider;
    public final Provider<SingleShadeLockScreenOverScroller.Factory> singleShadeOverScrollerFactoryProvider;
    public final Provider<SplitShadeLockScreenOverScroller.Factory> splitShadeOverScrollerFactoryProvider;
    public final Provider<SysuiStatusBarStateController> statusBarStateControllerProvider;
    public final Provider<WakefulnessLifecycle> wakefulnessLifecycleProvider;

    public LockscreenShadeTransitionController_Factory(Provider<SysuiStatusBarStateController> provider, Provider<LSShadeTransitionLogger> provider2, Provider<KeyguardBypassController> provider3, Provider<NotificationLockscreenUserManager> provider4, Provider<FalsingCollector> provider5, Provider<AmbientState> provider6, Provider<MediaHierarchyManager> provider7, Provider<LockscreenShadeScrimTransitionController> provider8, Provider<LockscreenShadeKeyguardTransitionController.Factory> provider9, Provider<NotificationShadeDepthController> provider10, Provider<Context> provider11, Provider<SplitShadeLockScreenOverScroller.Factory> provider12, Provider<SingleShadeLockScreenOverScroller.Factory> provider13, Provider<WakefulnessLifecycle> provider14, Provider<ConfigurationController> provider15, Provider<FalsingManager> provider16, Provider<DumpManager> provider17) {
        this.statusBarStateControllerProvider = provider;
        this.loggerProvider = provider2;
        this.keyguardBypassControllerProvider = provider3;
        this.lockScreenUserManagerProvider = provider4;
        this.falsingCollectorProvider = provider5;
        this.ambientStateProvider = provider6;
        this.mediaHierarchyManagerProvider = provider7;
        this.scrimTransitionControllerProvider = provider8;
        this.keyguardTransitionControllerFactoryProvider = provider9;
        this.depthControllerProvider = provider10;
        this.contextProvider = provider11;
        this.splitShadeOverScrollerFactoryProvider = provider12;
        this.singleShadeOverScrollerFactoryProvider = provider13;
        this.wakefulnessLifecycleProvider = provider14;
        this.configurationControllerProvider = provider15;
        this.falsingManagerProvider = provider16;
        this.dumpManagerProvider = provider17;
    }

    public LockscreenShadeTransitionController get() {
        return newInstance(this.statusBarStateControllerProvider.get(), this.loggerProvider.get(), this.keyguardBypassControllerProvider.get(), this.lockScreenUserManagerProvider.get(), this.falsingCollectorProvider.get(), this.ambientStateProvider.get(), this.mediaHierarchyManagerProvider.get(), this.scrimTransitionControllerProvider.get(), this.keyguardTransitionControllerFactoryProvider.get(), this.depthControllerProvider.get(), this.contextProvider.get(), this.splitShadeOverScrollerFactoryProvider.get(), this.singleShadeOverScrollerFactoryProvider.get(), this.wakefulnessLifecycleProvider.get(), this.configurationControllerProvider.get(), this.falsingManagerProvider.get(), this.dumpManagerProvider.get());
    }

    public static LockscreenShadeTransitionController_Factory create(Provider<SysuiStatusBarStateController> provider, Provider<LSShadeTransitionLogger> provider2, Provider<KeyguardBypassController> provider3, Provider<NotificationLockscreenUserManager> provider4, Provider<FalsingCollector> provider5, Provider<AmbientState> provider6, Provider<MediaHierarchyManager> provider7, Provider<LockscreenShadeScrimTransitionController> provider8, Provider<LockscreenShadeKeyguardTransitionController.Factory> provider9, Provider<NotificationShadeDepthController> provider10, Provider<Context> provider11, Provider<SplitShadeLockScreenOverScroller.Factory> provider12, Provider<SingleShadeLockScreenOverScroller.Factory> provider13, Provider<WakefulnessLifecycle> provider14, Provider<ConfigurationController> provider15, Provider<FalsingManager> provider16, Provider<DumpManager> provider17) {
        return new LockscreenShadeTransitionController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17);
    }

    public static LockscreenShadeTransitionController newInstance(SysuiStatusBarStateController sysuiStatusBarStateController, LSShadeTransitionLogger lSShadeTransitionLogger, KeyguardBypassController keyguardBypassController, NotificationLockscreenUserManager notificationLockscreenUserManager, FalsingCollector falsingCollector, AmbientState ambientState, MediaHierarchyManager mediaHierarchyManager, LockscreenShadeScrimTransitionController lockscreenShadeScrimTransitionController, LockscreenShadeKeyguardTransitionController.Factory factory, NotificationShadeDepthController notificationShadeDepthController, Context context, SplitShadeLockScreenOverScroller.Factory factory2, SingleShadeLockScreenOverScroller.Factory factory3, WakefulnessLifecycle wakefulnessLifecycle, ConfigurationController configurationController, FalsingManager falsingManager, DumpManager dumpManager) {
        return new LockscreenShadeTransitionController(sysuiStatusBarStateController, lSShadeTransitionLogger, keyguardBypassController, notificationLockscreenUserManager, falsingCollector, ambientState, mediaHierarchyManager, lockscreenShadeScrimTransitionController, factory, notificationShadeDepthController, context, factory2, factory3, wakefulnessLifecycle, configurationController, falsingManager, dumpManager);
    }
}
