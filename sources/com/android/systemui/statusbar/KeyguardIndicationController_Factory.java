package com.android.systemui.statusbar;

import android.app.IActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.os.Looper;
import android.os.UserManager;
import com.android.internal.app.IBatteryStats;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dock.DockManager;
import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.wakelock.WakeLock;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class KeyguardIndicationController_Factory implements Factory<KeyguardIndicationController> {
    public final Provider<DelayableExecutor> bgExecutorProvider;
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DevicePolicyManager> devicePolicyManagerProvider;
    public final Provider<DockManager> dockManagerProvider;
    public final Provider<DelayableExecutor> executorProvider;
    public final Provider<FalsingManager> falsingManagerProvider;
    public final Provider<IActivityManager> iActivityManagerProvider;
    public final Provider<IBatteryStats> iBatteryStatsProvider;
    public final Provider<KeyguardBypassController> keyguardBypassControllerProvider;
    public final Provider<KeyguardStateController> keyguardStateControllerProvider;
    public final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    public final Provider<LockPatternUtils> lockPatternUtilsProvider;
    public final Provider<Looper> mainLooperProvider;
    public final Provider<ScreenLifecycle> screenLifecycleProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;
    public final Provider<UserManager> userManagerProvider;
    public final Provider<WakeLock.Builder> wakeLockBuilderProvider;

    public KeyguardIndicationController_Factory(Provider<Context> provider, Provider<Looper> provider2, Provider<WakeLock.Builder> provider3, Provider<KeyguardStateController> provider4, Provider<StatusBarStateController> provider5, Provider<KeyguardUpdateMonitor> provider6, Provider<DockManager> provider7, Provider<BroadcastDispatcher> provider8, Provider<DevicePolicyManager> provider9, Provider<IBatteryStats> provider10, Provider<UserManager> provider11, Provider<DelayableExecutor> provider12, Provider<DelayableExecutor> provider13, Provider<FalsingManager> provider14, Provider<LockPatternUtils> provider15, Provider<ScreenLifecycle> provider16, Provider<IActivityManager> provider17, Provider<KeyguardBypassController> provider18) {
        this.contextProvider = provider;
        this.mainLooperProvider = provider2;
        this.wakeLockBuilderProvider = provider3;
        this.keyguardStateControllerProvider = provider4;
        this.statusBarStateControllerProvider = provider5;
        this.keyguardUpdateMonitorProvider = provider6;
        this.dockManagerProvider = provider7;
        this.broadcastDispatcherProvider = provider8;
        this.devicePolicyManagerProvider = provider9;
        this.iBatteryStatsProvider = provider10;
        this.userManagerProvider = provider11;
        this.executorProvider = provider12;
        this.bgExecutorProvider = provider13;
        this.falsingManagerProvider = provider14;
        this.lockPatternUtilsProvider = provider15;
        this.screenLifecycleProvider = provider16;
        this.iActivityManagerProvider = provider17;
        this.keyguardBypassControllerProvider = provider18;
    }

    public KeyguardIndicationController get() {
        return newInstance(this.contextProvider.get(), this.mainLooperProvider.get(), this.wakeLockBuilderProvider.get(), this.keyguardStateControllerProvider.get(), this.statusBarStateControllerProvider.get(), this.keyguardUpdateMonitorProvider.get(), this.dockManagerProvider.get(), this.broadcastDispatcherProvider.get(), this.devicePolicyManagerProvider.get(), this.iBatteryStatsProvider.get(), this.userManagerProvider.get(), this.executorProvider.get(), this.bgExecutorProvider.get(), this.falsingManagerProvider.get(), this.lockPatternUtilsProvider.get(), this.screenLifecycleProvider.get(), this.iActivityManagerProvider.get(), this.keyguardBypassControllerProvider.get());
    }

    public static KeyguardIndicationController_Factory create(Provider<Context> provider, Provider<Looper> provider2, Provider<WakeLock.Builder> provider3, Provider<KeyguardStateController> provider4, Provider<StatusBarStateController> provider5, Provider<KeyguardUpdateMonitor> provider6, Provider<DockManager> provider7, Provider<BroadcastDispatcher> provider8, Provider<DevicePolicyManager> provider9, Provider<IBatteryStats> provider10, Provider<UserManager> provider11, Provider<DelayableExecutor> provider12, Provider<DelayableExecutor> provider13, Provider<FalsingManager> provider14, Provider<LockPatternUtils> provider15, Provider<ScreenLifecycle> provider16, Provider<IActivityManager> provider17, Provider<KeyguardBypassController> provider18) {
        return new KeyguardIndicationController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18);
    }

    public static KeyguardIndicationController newInstance(Context context, Looper looper, WakeLock.Builder builder, KeyguardStateController keyguardStateController, StatusBarStateController statusBarStateController, KeyguardUpdateMonitor keyguardUpdateMonitor, DockManager dockManager, BroadcastDispatcher broadcastDispatcher, DevicePolicyManager devicePolicyManager, IBatteryStats iBatteryStats, UserManager userManager, DelayableExecutor delayableExecutor, DelayableExecutor delayableExecutor2, FalsingManager falsingManager, LockPatternUtils lockPatternUtils, ScreenLifecycle screenLifecycle, IActivityManager iActivityManager, KeyguardBypassController keyguardBypassController) {
        return new KeyguardIndicationController(context, looper, builder, keyguardStateController, statusBarStateController, keyguardUpdateMonitor, dockManager, broadcastDispatcher, devicePolicyManager, iBatteryStats, userManager, delayableExecutor, delayableExecutor2, falsingManager, lockPatternUtils, screenLifecycle, iActivityManager, keyguardBypassController);
    }
}
