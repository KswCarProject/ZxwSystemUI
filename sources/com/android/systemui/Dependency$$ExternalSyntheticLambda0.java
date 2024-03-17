package com.android.systemui;

import com.android.systemui.Dependency;
import dagger.Lazy;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class Dependency$$ExternalSyntheticLambda0 implements Dependency.LazyDependencyCreator {
    public final /* synthetic */ Lazy f$0;

    public /* synthetic */ Dependency$$ExternalSyntheticLambda0(Lazy lazy) {
        this.f$0 = lazy;
    }

    public final Object createDependency() {
        return this.f$0.get();
    }
}
