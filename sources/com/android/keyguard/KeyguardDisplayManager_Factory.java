package com.android.keyguard;

import android.content.Context;
import com.android.keyguard.dagger.KeyguardStatusViewComponent;
import com.android.systemui.navigationbar.NavigationBarController;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class KeyguardDisplayManager_Factory implements Factory<KeyguardDisplayManager> {
    public final Provider<Context> contextProvider;
    public final Provider<KeyguardStatusViewComponent.Factory> keyguardStatusViewComponentFactoryProvider;
    public final Provider<NavigationBarController> navigationBarControllerLazyProvider;
    public final Provider<Executor> uiBgExecutorProvider;

    public KeyguardDisplayManager_Factory(Provider<Context> provider, Provider<NavigationBarController> provider2, Provider<KeyguardStatusViewComponent.Factory> provider3, Provider<Executor> provider4) {
        this.contextProvider = provider;
        this.navigationBarControllerLazyProvider = provider2;
        this.keyguardStatusViewComponentFactoryProvider = provider3;
        this.uiBgExecutorProvider = provider4;
    }

    public KeyguardDisplayManager get() {
        return newInstance(this.contextProvider.get(), DoubleCheck.lazy(this.navigationBarControllerLazyProvider), this.keyguardStatusViewComponentFactoryProvider.get(), this.uiBgExecutorProvider.get());
    }

    public static KeyguardDisplayManager_Factory create(Provider<Context> provider, Provider<NavigationBarController> provider2, Provider<KeyguardStatusViewComponent.Factory> provider3, Provider<Executor> provider4) {
        return new KeyguardDisplayManager_Factory(provider, provider2, provider3, provider4);
    }

    public static KeyguardDisplayManager newInstance(Context context, Lazy<NavigationBarController> lazy, KeyguardStatusViewComponent.Factory factory, Executor executor) {
        return new KeyguardDisplayManager(context, lazy, factory, executor);
    }
}
