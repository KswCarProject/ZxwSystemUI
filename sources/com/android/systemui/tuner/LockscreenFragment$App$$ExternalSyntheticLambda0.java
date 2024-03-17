package com.android.systemui.tuner;

import com.android.systemui.tuner.LockscreenFragment;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class LockscreenFragment$App$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ LockscreenFragment.App f$0;
    public final /* synthetic */ LockscreenFragment.Adapter f$1;

    public /* synthetic */ LockscreenFragment$App$$ExternalSyntheticLambda0(LockscreenFragment.App app, LockscreenFragment.Adapter adapter) {
        this.f$0 = app;
        this.f$1 = adapter;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$toggleExpando$0(this.f$1, (LockscreenFragment.Item) obj);
    }
}
