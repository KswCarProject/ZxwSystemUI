package com.android.wm.shell.legacysplitscreen;

import java.lang.ref.WeakReference;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class LegacySplitScreenController$$ExternalSyntheticLambda4 implements Predicate {
    public final /* synthetic */ boolean f$0;

    public /* synthetic */ LegacySplitScreenController$$ExternalSyntheticLambda4(boolean z) {
        this.f$0 = z;
    }

    public final boolean test(Object obj) {
        return LegacySplitScreenController.lambda$updateVisibility$3(this.f$0, (WeakReference) obj);
    }
}
