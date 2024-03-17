package com.android.systemui.statusbar.core;

import com.android.systemui.statusbar.window.StatusBarWindowController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class StatusBarInitializer_Factory implements Factory<StatusBarInitializer> {
    public final Provider<StatusBarWindowController> windowControllerProvider;

    public StatusBarInitializer_Factory(Provider<StatusBarWindowController> provider) {
        this.windowControllerProvider = provider;
    }

    public StatusBarInitializer get() {
        return newInstance(this.windowControllerProvider.get());
    }

    public static StatusBarInitializer_Factory create(Provider<StatusBarWindowController> provider) {
        return new StatusBarInitializer_Factory(provider);
    }

    public static StatusBarInitializer newInstance(StatusBarWindowController statusBarWindowController) {
        return new StatusBarInitializer(statusBarWindowController);
    }
}
