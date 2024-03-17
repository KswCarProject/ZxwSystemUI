package com.android.systemui.wallet.ui;

import android.service.quickaccesswallet.GetWalletCardsResponse;
import java.util.List;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class WalletScreenController$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ WalletScreenController f$0;
    public final /* synthetic */ List f$1;
    public final /* synthetic */ GetWalletCardsResponse f$2;

    public /* synthetic */ WalletScreenController$$ExternalSyntheticLambda0(WalletScreenController walletScreenController, List list, GetWalletCardsResponse getWalletCardsResponse) {
        this.f$0 = walletScreenController;
        this.f$1 = list;
        this.f$2 = getWalletCardsResponse;
    }

    public final void run() {
        this.f$0.lambda$onWalletCardsRetrieved$0(this.f$1, this.f$2);
    }
}
