package com.android.systemui.flags;

import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class FeatureFlagsDebug$$ExternalSyntheticLambda5 implements Consumer {
    public final /* synthetic */ FeatureFlagsDebug f$0;

    public /* synthetic */ FeatureFlagsDebug$$ExternalSyntheticLambda5(FeatureFlagsDebug featureFlagsDebug) {
        this.f$0 = featureFlagsDebug;
    }

    public final void accept(Object obj) {
        this.f$0.restartAndroid(((Boolean) obj).booleanValue());
    }
}
