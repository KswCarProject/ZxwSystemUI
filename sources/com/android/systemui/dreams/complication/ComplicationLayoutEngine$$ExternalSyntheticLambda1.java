package com.android.systemui.dreams.complication;

import com.android.systemui.dreams.complication.ComplicationLayoutEngine;
import java.util.Map;
import java.util.function.Function;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ComplicationLayoutEngine$$ExternalSyntheticLambda1 implements Function {
    public final Object apply(Object obj) {
        return ((ComplicationLayoutEngine.PositionGroup) ((Map.Entry) obj).getValue()).getViews().stream();
    }
}
