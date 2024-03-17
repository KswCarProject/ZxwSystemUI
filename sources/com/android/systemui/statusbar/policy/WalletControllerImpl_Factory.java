package com.android.systemui.statusbar.policy;

import android.service.quickaccesswallet.QuickAccessWalletClient;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class WalletControllerImpl_Factory implements Factory<WalletControllerImpl> {
    public final Provider<QuickAccessWalletClient> quickAccessWalletClientProvider;

    public WalletControllerImpl_Factory(Provider<QuickAccessWalletClient> provider) {
        this.quickAccessWalletClientProvider = provider;
    }

    public WalletControllerImpl get() {
        return newInstance(this.quickAccessWalletClientProvider.get());
    }

    public static WalletControllerImpl_Factory create(Provider<QuickAccessWalletClient> provider) {
        return new WalletControllerImpl_Factory(provider);
    }

    public static WalletControllerImpl newInstance(QuickAccessWalletClient quickAccessWalletClient) {
        return new WalletControllerImpl(quickAccessWalletClient);
    }
}
