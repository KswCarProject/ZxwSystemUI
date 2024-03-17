package com.android.systemui.tuner;

import com.android.systemui.tuner.LockscreenFragment;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class LockscreenFragment$App$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ LockscreenFragment.Adapter f$0;

    public /* synthetic */ LockscreenFragment$App$$ExternalSyntheticLambda1(LockscreenFragment.Adapter adapter) {
        this.f$0 = adapter;
    }

    public final void accept(Object obj) {
        this.f$0.remItem((LockscreenFragment.Item) obj);
    }
}
