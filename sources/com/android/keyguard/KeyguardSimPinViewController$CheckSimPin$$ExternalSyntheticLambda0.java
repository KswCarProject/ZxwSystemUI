package com.android.keyguard;

import android.telephony.PinResult;
import com.android.keyguard.KeyguardSimPinViewController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class KeyguardSimPinViewController$CheckSimPin$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ KeyguardSimPinViewController.CheckSimPin f$0;
    public final /* synthetic */ PinResult f$1;

    public /* synthetic */ KeyguardSimPinViewController$CheckSimPin$$ExternalSyntheticLambda0(KeyguardSimPinViewController.CheckSimPin checkSimPin, PinResult pinResult) {
        this.f$0 = checkSimPin;
        this.f$1 = pinResult;
    }

    public final void run() {
        this.f$0.lambda$run$0(this.f$1);
    }
}
