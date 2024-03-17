package com.android.systemui.navigationbar;

import android.content.Context;
import android.view.WindowManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class NavigationBarModule_ProvideWindowManagerFactory implements Factory<WindowManager> {
    public final Provider<Context> contextProvider;

    public NavigationBarModule_ProvideWindowManagerFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public WindowManager get() {
        return provideWindowManager(this.contextProvider.get());
    }

    public static NavigationBarModule_ProvideWindowManagerFactory create(Provider<Context> provider) {
        return new NavigationBarModule_ProvideWindowManagerFactory(provider);
    }

    public static WindowManager provideWindowManager(Context context) {
        return (WindowManager) Preconditions.checkNotNullFromProvides(NavigationBarModule.provideWindowManager(context));
    }
}
