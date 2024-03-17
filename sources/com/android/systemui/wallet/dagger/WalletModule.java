package com.android.systemui.wallet.dagger;

import android.content.Context;
import android.service.quickaccesswallet.QuickAccessWalletClient;
import java.util.concurrent.Executor;

public abstract class WalletModule {
    public static QuickAccessWalletClient provideQuickAccessWalletClient(Context context, Executor executor) {
        return QuickAccessWalletClient.create(context, executor);
    }
}
