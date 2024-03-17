package com.android.systemui.qs;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class QSTileHost$$ExternalSyntheticLambda4 implements Predicate {
    public final /* synthetic */ List f$0;

    public /* synthetic */ QSTileHost$$ExternalSyntheticLambda4(List list) {
        this.f$0 = list;
    }

    public final boolean test(Object obj) {
        return QSTileHost.lambda$onTuningChanged$2(this.f$0, (Map.Entry) obj);
    }
}
