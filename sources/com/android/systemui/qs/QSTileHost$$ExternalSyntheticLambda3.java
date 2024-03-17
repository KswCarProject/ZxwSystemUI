package com.android.systemui.qs;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class QSTileHost$$ExternalSyntheticLambda3 implements Predicate {
    public final /* synthetic */ Collection f$0;

    public /* synthetic */ QSTileHost$$ExternalSyntheticLambda3(Collection collection) {
        this.f$0 = collection;
    }

    public final boolean test(Object obj) {
        return ((List) obj).removeAll(this.f$0);
    }
}
