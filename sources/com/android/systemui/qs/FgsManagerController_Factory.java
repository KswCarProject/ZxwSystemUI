package com.android.systemui.qs;

import android.app.IActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.systemui.util.time.SystemClock;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class FgsManagerController_Factory implements Factory<FgsManagerController> {
    public final Provider<IActivityManager> activityManagerProvider;
    public final Provider<Executor> backgroundExecutorProvider;
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DeviceConfigProxy> deviceConfigProxyProvider;
    public final Provider<DialogLaunchAnimator> dialogLaunchAnimatorProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<Executor> mainExecutorProvider;
    public final Provider<PackageManager> packageManagerProvider;
    public final Provider<SystemClock> systemClockProvider;
    public final Provider<UserTracker> userTrackerProvider;

    public FgsManagerController_Factory(Provider<Context> provider, Provider<Executor> provider2, Provider<Executor> provider3, Provider<SystemClock> provider4, Provider<IActivityManager> provider5, Provider<PackageManager> provider6, Provider<UserTracker> provider7, Provider<DeviceConfigProxy> provider8, Provider<DialogLaunchAnimator> provider9, Provider<BroadcastDispatcher> provider10, Provider<DumpManager> provider11) {
        this.contextProvider = provider;
        this.mainExecutorProvider = provider2;
        this.backgroundExecutorProvider = provider3;
        this.systemClockProvider = provider4;
        this.activityManagerProvider = provider5;
        this.packageManagerProvider = provider6;
        this.userTrackerProvider = provider7;
        this.deviceConfigProxyProvider = provider8;
        this.dialogLaunchAnimatorProvider = provider9;
        this.broadcastDispatcherProvider = provider10;
        this.dumpManagerProvider = provider11;
    }

    public FgsManagerController get() {
        return newInstance(this.contextProvider.get(), this.mainExecutorProvider.get(), this.backgroundExecutorProvider.get(), this.systemClockProvider.get(), this.activityManagerProvider.get(), this.packageManagerProvider.get(), this.userTrackerProvider.get(), this.deviceConfigProxyProvider.get(), this.dialogLaunchAnimatorProvider.get(), this.broadcastDispatcherProvider.get(), this.dumpManagerProvider.get());
    }

    public static FgsManagerController_Factory create(Provider<Context> provider, Provider<Executor> provider2, Provider<Executor> provider3, Provider<SystemClock> provider4, Provider<IActivityManager> provider5, Provider<PackageManager> provider6, Provider<UserTracker> provider7, Provider<DeviceConfigProxy> provider8, Provider<DialogLaunchAnimator> provider9, Provider<BroadcastDispatcher> provider10, Provider<DumpManager> provider11) {
        return new FgsManagerController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11);
    }

    public static FgsManagerController newInstance(Context context, Executor executor, Executor executor2, SystemClock systemClock, IActivityManager iActivityManager, PackageManager packageManager, UserTracker userTracker, DeviceConfigProxy deviceConfigProxy, DialogLaunchAnimator dialogLaunchAnimator, BroadcastDispatcher broadcastDispatcher, DumpManager dumpManager) {
        return new FgsManagerController(context, executor, executor2, systemClock, iActivityManager, packageManager, userTracker, deviceConfigProxy, dialogLaunchAnimator, broadcastDispatcher, dumpManager);
    }
}
