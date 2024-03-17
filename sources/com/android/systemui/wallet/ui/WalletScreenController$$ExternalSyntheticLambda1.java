package com.android.systemui.wallet.ui;

import android.service.quickaccesswallet.GetWalletCardsError;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class WalletScreenController$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ WalletScreenController f$0;
    public final /* synthetic */ GetWalletCardsError f$1;

    public /* synthetic */ WalletScreenController$$ExternalSyntheticLambda1(WalletScreenController walletScreenController, GetWalletCardsError getWalletCardsError) {
        this.f$0 = walletScreenController;
        this.f$1 = getWalletCardsError;
    }

    public final void run() {
        this.f$0.lambda$onWalletCardRetrievalError$1(this.f$1);
    }
}
