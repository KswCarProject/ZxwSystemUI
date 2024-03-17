package com.android.wifitrackerlib;

import android.telephony.SubscriptionInfo;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class Utils$$ExternalSyntheticLambda1 implements Predicate {
    public final /* synthetic */ int f$0;

    public /* synthetic */ Utils$$ExternalSyntheticLambda1(int i) {
        this.f$0 = i;
    }

    public final boolean test(Object obj) {
        return Utils.lambda$isSimPresent$1(this.f$0, (SubscriptionInfo) obj);
    }
}
