package com.android.systemui.util.leak;

import java.util.Collection;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class LeakDetector$$ExternalSyntheticLambda1 implements Predicate {
    public final boolean test(Object obj) {
        return TrackedObjects.isTrackedObject((Collection) obj);
    }
}
