package com.android.keyguard;

import android.telephony.PinResult;
import com.android.keyguard.KeyguardSimPukViewController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class KeyguardSimPukViewController$CheckSimPuk$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ KeyguardSimPukViewController.CheckSimPuk f$0;
    public final /* synthetic */ PinResult f$1;

    public /* synthetic */ KeyguardSimPukViewController$CheckSimPuk$$ExternalSyntheticLambda0(KeyguardSimPukViewController.CheckSimPuk checkSimPuk, PinResult pinResult) {
        this.f$0 = checkSimPuk;
        this.f$1 = pinResult;
    }

    public final void run() {
        this.f$0.lambda$run$0(this.f$1);
    }
}
