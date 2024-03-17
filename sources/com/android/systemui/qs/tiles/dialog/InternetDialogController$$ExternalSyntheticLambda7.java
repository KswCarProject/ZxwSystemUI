package com.android.systemui.qs.tiles.dialog;

import android.content.Context;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class InternetDialogController$$ExternalSyntheticLambda7 implements Supplier {
    public final /* synthetic */ Supplier f$0;
    public final /* synthetic */ Set f$1;
    public final /* synthetic */ Context f$2;

    public /* synthetic */ InternetDialogController$$ExternalSyntheticLambda7(Supplier supplier, Set set, Context context) {
        this.f$0 = supplier;
        this.f$1 = set;
        this.f$2 = context;
    }

    public final Object get() {
        return ((Stream) this.f$0.get()).map(new InternetDialogController$$ExternalSyntheticLambda0(this.f$1, this.f$2));
    }
}
