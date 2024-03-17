package com.android.systemui.keyguard;

import android.content.pm.PackageManager;
import android.os.UserManager;
import com.android.systemui.broadcast.BroadcastDispatcher;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class WorkLockActivity_Factory implements Factory<WorkLockActivity> {
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<PackageManager> packageManagerProvider;
    public final Provider<UserManager> userManagerProvider;

    public WorkLockActivity_Factory(Provider<BroadcastDispatcher> provider, Provider<UserManager> provider2, Provider<PackageManager> provider3) {
        this.broadcastDispatcherProvider = provider;
        this.userManagerProvider = provider2;
        this.packageManagerProvider = provider3;
    }

    public WorkLockActivity get() {
        return newInstance(this.broadcastDispatcherProvider.get(), this.userManagerProvider.get(), this.packageManagerProvider.get());
    }

    public static WorkLockActivity_Factory create(Provider<BroadcastDispatcher> provider, Provider<UserManager> provider2, Provider<PackageManager> provider3) {
        return new WorkLockActivity_Factory(provider, provider2, provider3);
    }

    public static WorkLockActivity newInstance(BroadcastDispatcher broadcastDispatcher, UserManager userManager, PackageManager packageManager) {
        return new WorkLockActivity(broadcastDispatcher, userManager, packageManager);
    }
}
