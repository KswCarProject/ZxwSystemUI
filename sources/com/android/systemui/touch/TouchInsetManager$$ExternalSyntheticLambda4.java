package com.android.systemui.touch;

import android.graphics.Region;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class TouchInsetManager$$ExternalSyntheticLambda4 implements Predicate {
    public final /* synthetic */ int f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ TouchInsetManager$$ExternalSyntheticLambda4(int i, int i2) {
        this.f$0 = i;
        this.f$1 = i2;
    }

    public final boolean test(Object obj) {
        return ((Region) obj).contains(this.f$0, this.f$1);
    }
}
