package com.android.systemui.statusbar.events;

import android.content.Context;
import com.android.systemui.statusbar.phone.StatusBarContentInsetsProvider;
import com.android.systemui.statusbar.window.StatusBarWindowController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class SystemEventChipAnimationController_Factory implements Factory<SystemEventChipAnimationController> {
    public final Provider<StatusBarContentInsetsProvider> contentInsetsProvider;
    public final Provider<Context> contextProvider;
    public final Provider<StatusBarWindowController> statusBarWindowControllerProvider;

    public SystemEventChipAnimationController_Factory(Provider<Context> provider, Provider<StatusBarWindowController> provider2, Provider<StatusBarContentInsetsProvider> provider3) {
        this.contextProvider = provider;
        this.statusBarWindowControllerProvider = provider2;
        this.contentInsetsProvider = provider3;
    }

    public SystemEventChipAnimationController get() {
        return newInstance(this.contextProvider.get(), this.statusBarWindowControllerProvider.get(), this.contentInsetsProvider.get());
    }

    public static SystemEventChipAnimationController_Factory create(Provider<Context> provider, Provider<StatusBarWindowController> provider2, Provider<StatusBarContentInsetsProvider> provider3) {
        return new SystemEventChipAnimationController_Factory(provider, provider2, provider3);
    }

    public static SystemEventChipAnimationController newInstance(Context context, StatusBarWindowController statusBarWindowController, StatusBarContentInsetsProvider statusBarContentInsetsProvider) {
        return new SystemEventChipAnimationController(context, statusBarWindowController, statusBarContentInsetsProvider);
    }
}
