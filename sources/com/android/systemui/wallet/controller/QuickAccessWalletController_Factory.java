package com.android.systemui.wallet.controller;

import android.content.Context;
import android.service.quickaccesswallet.QuickAccessWalletClient;
import com.android.systemui.util.settings.SecureSettings;
import com.android.systemui.util.time.SystemClock;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class QuickAccessWalletController_Factory implements Factory<QuickAccessWalletController> {
    public final Provider<Executor> bgExecutorProvider;
    public final Provider<SystemClock> clockProvider;
    public final Provider<Context> contextProvider;
    public final Provider<Executor> executorProvider;
    public final Provider<QuickAccessWalletClient> quickAccessWalletClientProvider;
    public final Provider<SecureSettings> secureSettingsProvider;

    public QuickAccessWalletController_Factory(Provider<Context> provider, Provider<Executor> provider2, Provider<Executor> provider3, Provider<SecureSettings> provider4, Provider<QuickAccessWalletClient> provider5, Provider<SystemClock> provider6) {
        this.contextProvider = provider;
        this.executorProvider = provider2;
        this.bgExecutorProvider = provider3;
        this.secureSettingsProvider = provider4;
        this.quickAccessWalletClientProvider = provider5;
        this.clockProvider = provider6;
    }

    public QuickAccessWalletController get() {
        return newInstance(this.contextProvider.get(), this.executorProvider.get(), this.bgExecutorProvider.get(), this.secureSettingsProvider.get(), this.quickAccessWalletClientProvider.get(), this.clockProvider.get());
    }

    public static QuickAccessWalletController_Factory create(Provider<Context> provider, Provider<Executor> provider2, Provider<Executor> provider3, Provider<SecureSettings> provider4, Provider<QuickAccessWalletClient> provider5, Provider<SystemClock> provider6) {
        return new QuickAccessWalletController_Factory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static QuickAccessWalletController newInstance(Context context, Executor executor, Executor executor2, SecureSettings secureSettings, QuickAccessWalletClient quickAccessWalletClient, SystemClock systemClock) {
        return new QuickAccessWalletController(context, executor, executor2, secureSettings, quickAccessWalletClient, systemClock);
    }
}
