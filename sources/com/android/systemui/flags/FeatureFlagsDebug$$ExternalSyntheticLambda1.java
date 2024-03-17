package com.android.systemui.flags;

import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class FeatureFlagsDebug$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ FeatureFlagsDebug f$0;

    public /* synthetic */ FeatureFlagsDebug$$ExternalSyntheticLambda1(FeatureFlagsDebug featureFlagsDebug) {
        this.f$0 = featureFlagsDebug;
    }

    public final void accept(Object obj) {
        this.f$0.removeFromCache(((Integer) obj).intValue());
    }
}
