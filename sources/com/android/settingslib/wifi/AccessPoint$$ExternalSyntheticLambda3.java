package com.android.settingslib.wifi;

import java.util.Iterator;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class AccessPoint$$ExternalSyntheticLambda3 implements Consumer {
    public final /* synthetic */ long f$0;
    public final /* synthetic */ Iterator f$1;

    public /* synthetic */ AccessPoint$$ExternalSyntheticLambda3(long j, Iterator it) {
        this.f$0 = j;
        this.f$1 = it;
    }

    public final void accept(Object obj) {
        AccessPoint.lambda$updateScores$0(this.f$0, this.f$1, (TimestampedScoredNetwork) obj);
    }
}
