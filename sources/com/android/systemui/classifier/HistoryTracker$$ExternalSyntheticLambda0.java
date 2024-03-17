package com.android.systemui.classifier;

import com.android.systemui.classifier.HistoryTracker;
import java.util.function.Function;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class HistoryTracker$$ExternalSyntheticLambda0 implements Function {
    public final /* synthetic */ long f$0;

    public /* synthetic */ HistoryTracker$$ExternalSyntheticLambda0(long j) {
        this.f$0 = j;
    }

    public final Object apply(Object obj) {
        return Double.valueOf(((HistoryTracker.CombinedResult) obj).getDecayedScore(this.f$0));
    }
}
