package com.android.keyguard;

import android.content.Context;
import android.os.Looper;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.internal.util.LatencyTracker;
import com.android.internal.widget.LockPatternUtils;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.telephony.TelephonyListenerManager;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class KeyguardUpdateMonitor_Factory implements Factory<KeyguardUpdateMonitor> {
    public final Provider<ActiveUnlockConfig> activeUnlockConfigurationProvider;
    public final Provider<AuthController> authControllerProvider;
    public final Provider<Executor> backgroundExecutorProvider;
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<InteractionJankMonitor> interactionJankMonitorProvider;
    public final Provider<LatencyTracker> latencyTrackerProvider;
    public final Provider<LockPatternUtils> lockPatternUtilsProvider;
    public final Provider<Executor> mainExecutorProvider;
    public final Provider<Looper> mainLooperProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;
    public final Provider<TelephonyListenerManager> telephonyListenerManagerProvider;

    public KeyguardUpdateMonitor_Factory(Provider<Context> provider, Provider<Looper> provider2, Provider<BroadcastDispatcher> provider3, Provider<DumpManager> provider4, Provider<Executor> provider5, Provider<Executor> provider6, Provider<StatusBarStateController> provider7, Provider<LockPatternUtils> provider8, Provider<AuthController> provider9, Provider<TelephonyListenerManager> provider10, Provider<InteractionJankMonitor> provider11, Provider<LatencyTracker> provider12, Provider<ActiveUnlockConfig> provider13) {
        this.contextProvider = provider;
        this.mainLooperProvider = provider2;
        this.broadcastDispatcherProvider = provider3;
        this.dumpManagerProvider = provider4;
        this.backgroundExecutorProvider = provider5;
        this.mainExecutorProvider = provider6;
        this.statusBarStateControllerProvider = provider7;
        this.lockPatternUtilsProvider = provider8;
        this.authControllerProvider = provider9;
        this.telephonyListenerManagerProvider = provider10;
        this.interactionJankMonitorProvider = provider11;
        this.latencyTrackerProvider = provider12;
        this.activeUnlockConfigurationProvider = provider13;
    }

    public KeyguardUpdateMonitor get() {
        return newInstance(this.contextProvider.get(), this.mainLooperProvider.get(), this.broadcastDispatcherProvider.get(), this.dumpManagerProvider.get(), this.backgroundExecutorProvider.get(), this.mainExecutorProvider.get(), this.statusBarStateControllerProvider.get(), this.lockPatternUtilsProvider.get(), this.authControllerProvider.get(), this.telephonyListenerManagerProvider.get(), this.interactionJankMonitorProvider.get(), this.latencyTrackerProvider.get(), this.activeUnlockConfigurationProvider.get());
    }

    public static KeyguardUpdateMonitor_Factory create(Provider<Context> provider, Provider<Looper> provider2, Provider<BroadcastDispatcher> provider3, Provider<DumpManager> provider4, Provider<Executor> provider5, Provider<Executor> provider6, Provider<StatusBarStateController> provider7, Provider<LockPatternUtils> provider8, Provider<AuthController> provider9, Provider<TelephonyListenerManager> provider10, Provider<InteractionJankMonitor> provider11, Provider<LatencyTracker> provider12, Provider<ActiveUnlockConfig> provider13) {
        return new KeyguardUpdateMonitor_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13);
    }

    public static KeyguardUpdateMonitor newInstance(Context context, Looper looper, BroadcastDispatcher broadcastDispatcher, DumpManager dumpManager, Executor executor, Executor executor2, StatusBarStateController statusBarStateController, LockPatternUtils lockPatternUtils, AuthController authController, TelephonyListenerManager telephonyListenerManager, InteractionJankMonitor interactionJankMonitor, LatencyTracker latencyTracker, ActiveUnlockConfig activeUnlockConfig) {
        return new KeyguardUpdateMonitor(context, looper, broadcastDispatcher, dumpManager, executor, executor2, statusBarStateController, lockPatternUtils, authController, telephonyListenerManager, interactionJankMonitor, latencyTracker, activeUnlockConfig);
    }
}
