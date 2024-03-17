package com.android.systemui.user;

import android.os.UserManager;
import android.view.LayoutInflater;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class UserSwitcherActivity_Factory implements Factory<UserSwitcherActivity> {
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<FalsingManager> falsingManagerProvider;
    public final Provider<LayoutInflater> layoutInflaterProvider;
    public final Provider<UserManager> userManagerProvider;
    public final Provider<UserSwitcherController> userSwitcherControllerProvider;
    public final Provider<UserTracker> userTrackerProvider;

    public UserSwitcherActivity_Factory(Provider<UserSwitcherController> provider, Provider<BroadcastDispatcher> provider2, Provider<LayoutInflater> provider3, Provider<FalsingManager> provider4, Provider<UserManager> provider5, Provider<UserTracker> provider6) {
        this.userSwitcherControllerProvider = provider;
        this.broadcastDispatcherProvider = provider2;
        this.layoutInflaterProvider = provider3;
        this.falsingManagerProvider = provider4;
        this.userManagerProvider = provider5;
        this.userTrackerProvider = provider6;
    }

    public UserSwitcherActivity get() {
        return newInstance(this.userSwitcherControllerProvider.get(), this.broadcastDispatcherProvider.get(), this.layoutInflaterProvider.get(), this.falsingManagerProvider.get(), this.userManagerProvider.get(), this.userTrackerProvider.get());
    }

    public static UserSwitcherActivity_Factory create(Provider<UserSwitcherController> provider, Provider<BroadcastDispatcher> provider2, Provider<LayoutInflater> provider3, Provider<FalsingManager> provider4, Provider<UserManager> provider5, Provider<UserTracker> provider6) {
        return new UserSwitcherActivity_Factory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static UserSwitcherActivity newInstance(UserSwitcherController userSwitcherController, BroadcastDispatcher broadcastDispatcher, LayoutInflater layoutInflater, FalsingManager falsingManager, UserManager userManager, UserTracker userTracker) {
        return new UserSwitcherActivity(userSwitcherController, broadcastDispatcher, layoutInflater, falsingManager, userManager, userTracker);
    }
}
