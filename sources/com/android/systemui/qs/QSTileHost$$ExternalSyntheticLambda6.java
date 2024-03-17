package com.android.systemui.qs;

import java.util.List;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class QSTileHost$$ExternalSyntheticLambda6 implements Predicate {
    public final /* synthetic */ String f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ QSTileHost$$ExternalSyntheticLambda6(String str, int i) {
        this.f$0 = str;
        this.f$1 = i;
    }

    public final boolean test(Object obj) {
        return QSTileHost.lambda$addTile$6(this.f$0, this.f$1, (List) obj);
    }
}
