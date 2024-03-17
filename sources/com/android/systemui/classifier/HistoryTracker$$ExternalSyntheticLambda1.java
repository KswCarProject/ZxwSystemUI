package com.android.systemui.classifier;

import java.util.function.BinaryOperator;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class HistoryTracker$$ExternalSyntheticLambda1 implements BinaryOperator {
    public final Object apply(Object obj, Object obj2) {
        return Double.valueOf((((Double) obj).doubleValue() * ((Double) obj2).doubleValue()) / ((((Double) obj).doubleValue() * ((Double) obj2).doubleValue()) + ((1.0d - ((Double) obj).doubleValue()) * (1.0d - ((Double) obj2).doubleValue()))));
    }
}
