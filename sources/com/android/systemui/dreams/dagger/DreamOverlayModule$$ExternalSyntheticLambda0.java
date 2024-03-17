package com.android.systemui.dreams.dagger;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import dagger.Lazy;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DreamOverlayModule$$ExternalSyntheticLambda0 implements LifecycleOwner {
    public final /* synthetic */ Lazy f$0;

    public /* synthetic */ DreamOverlayModule$$ExternalSyntheticLambda0(Lazy lazy) {
        this.f$0 = lazy;
    }

    public final Lifecycle getLifecycle() {
        return DreamOverlayModule.lambda$providesLifecycleOwner$0(this.f$0);
    }
}
