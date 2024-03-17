package com.android.systemui.wallet.dagger;

import android.content.Context;
import android.service.quickaccesswallet.QuickAccessWalletClient;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class WalletModule_ProvideQuickAccessWalletClientFactory implements Factory<QuickAccessWalletClient> {
    public final Provider<Executor> bgExecutorProvider;
    public final Provider<Context> contextProvider;

    public WalletModule_ProvideQuickAccessWalletClientFactory(Provider<Context> provider, Provider<Executor> provider2) {
        this.contextProvider = provider;
        this.bgExecutorProvider = provider2;
    }

    public QuickAccessWalletClient get() {
        return provideQuickAccessWalletClient(this.contextProvider.get(), this.bgExecutorProvider.get());
    }

    public static WalletModule_ProvideQuickAccessWalletClientFactory create(Provider<Context> provider, Provider<Executor> provider2) {
        return new WalletModule_ProvideQuickAccessWalletClientFactory(provider, provider2);
    }

    public static QuickAccessWalletClient provideQuickAccessWalletClient(Context context, Executor executor) {
        return (QuickAccessWalletClient) Preconditions.checkNotNullFromProvides(WalletModule.provideQuickAccessWalletClient(context, executor));
    }
}
