package com.android.systemui.unfold;

import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class UnfoldTransitionModule_TracingTagPrefixFactory implements Factory<String> {
    public final UnfoldTransitionModule module;

    public UnfoldTransitionModule_TracingTagPrefixFactory(UnfoldTransitionModule unfoldTransitionModule) {
        this.module = unfoldTransitionModule;
    }

    public String get() {
        return tracingTagPrefix(this.module);
    }

    public static UnfoldTransitionModule_TracingTagPrefixFactory create(UnfoldTransitionModule unfoldTransitionModule) {
        return new UnfoldTransitionModule_TracingTagPrefixFactory(unfoldTransitionModule);
    }

    public static String tracingTagPrefix(UnfoldTransitionModule unfoldTransitionModule) {
        return (String) Preconditions.checkNotNullFromProvides(unfoldTransitionModule.tracingTagPrefix());
    }
}
