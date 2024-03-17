package com.android.systemui.dreams.complication;

import java.util.Collection;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ComplicationHostViewController$$ExternalSyntheticLambda2 implements Predicate {
    public final /* synthetic */ Collection f$0;

    public /* synthetic */ ComplicationHostViewController$$ExternalSyntheticLambda2(Collection collection) {
        this.f$0 = collection;
    }

    public final boolean test(Object obj) {
        return ComplicationHostViewController.lambda$updateComplications$2(this.f$0, (ComplicationId) obj);
    }
}
