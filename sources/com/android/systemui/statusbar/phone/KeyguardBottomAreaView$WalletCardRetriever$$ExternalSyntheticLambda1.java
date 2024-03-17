package com.android.systemui.statusbar.phone;

import android.graphics.drawable.Drawable;
import com.android.systemui.statusbar.phone.KeyguardBottomAreaView;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class KeyguardBottomAreaView$WalletCardRetriever$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ KeyguardBottomAreaView.WalletCardRetriever f$0;
    public final /* synthetic */ Drawable f$1;

    public /* synthetic */ KeyguardBottomAreaView$WalletCardRetriever$$ExternalSyntheticLambda1(KeyguardBottomAreaView.WalletCardRetriever walletCardRetriever, Drawable drawable) {
        this.f$0 = walletCardRetriever;
        this.f$1 = drawable;
    }

    public final void run() {
        this.f$0.lambda$onWalletCardsRetrieved$0(this.f$1);
    }
}
