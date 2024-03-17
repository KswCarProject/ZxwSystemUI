package com.android.systemui.tuner;

import com.android.systemui.Dependency;
import com.android.systemui.tuner.TunerService;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class NavBarTuner$$ExternalSyntheticLambda0 implements Consumer {
    public final void accept(Object obj) {
        ((TunerService) Dependency.get(TunerService.class)).removeTunable((TunerService.Tunable) obj);
    }
}
