package com.android.systemui.tuner;

import com.android.systemui.tuner.TunerService;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class LockscreenFragment$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ LockscreenFragment f$0;

    public /* synthetic */ LockscreenFragment$$ExternalSyntheticLambda0(LockscreenFragment lockscreenFragment) {
        this.f$0 = lockscreenFragment;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$onDestroy$0((TunerService.Tunable) obj);
    }
}
