package com.android.systemui.wallet.controller;

import android.app.PendingIntent;
import android.service.quickaccesswallet.QuickAccessWalletClient;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.plugins.ActivityStarter;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class QuickAccessWalletController$$ExternalSyntheticLambda0 implements QuickAccessWalletClient.WalletPendingIntentCallback {
    public final /* synthetic */ QuickAccessWalletController f$0;
    public final /* synthetic */ ActivityStarter f$1;
    public final /* synthetic */ ActivityLaunchAnimator.Controller f$2;
    public final /* synthetic */ boolean f$3;

    public /* synthetic */ QuickAccessWalletController$$ExternalSyntheticLambda0(QuickAccessWalletController quickAccessWalletController, ActivityStarter activityStarter, ActivityLaunchAnimator.Controller controller, boolean z) {
        this.f$0 = quickAccessWalletController;
        this.f$1 = activityStarter;
        this.f$2 = controller;
        this.f$3 = z;
    }

    public final void onWalletPendingIntentRetrieved(PendingIntent pendingIntent) {
        this.f$0.lambda$startQuickAccessUiIntent$0(this.f$1, this.f$2, this.f$3, pendingIntent);
    }
}
