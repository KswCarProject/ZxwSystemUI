package com.android.wifitrackerlib;

import java.util.Map;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ScanResultUpdater$$ExternalSyntheticLambda0 implements Predicate {
    public final /* synthetic */ ScanResultUpdater f$0;

    public /* synthetic */ ScanResultUpdater$$ExternalSyntheticLambda0(ScanResultUpdater scanResultUpdater) {
        this.f$0 = scanResultUpdater;
    }

    public final boolean test(Object obj) {
        return this.f$0.lambda$evictOldScans$0((Map.Entry) obj);
    }
}
