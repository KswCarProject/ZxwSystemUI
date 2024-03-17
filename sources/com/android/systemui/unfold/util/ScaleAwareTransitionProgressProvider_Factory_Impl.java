package com.android.systemui.unfold.util;

import com.android.systemui.unfold.UnfoldTransitionProgressProvider;
import com.android.systemui.unfold.util.ScaleAwareTransitionProgressProvider;
import dagger.internal.InstanceFactory;
import javax.inject.Provider;

public final class ScaleAwareTransitionProgressProvider_Factory_Impl implements ScaleAwareTransitionProgressProvider.Factory {
    public final C0008ScaleAwareTransitionProgressProvider_Factory delegateFactory;

    public ScaleAwareTransitionProgressProvider_Factory_Impl(C0008ScaleAwareTransitionProgressProvider_Factory scaleAwareTransitionProgressProvider_Factory) {
        this.delegateFactory = scaleAwareTransitionProgressProvider_Factory;
    }

    public ScaleAwareTransitionProgressProvider wrap(UnfoldTransitionProgressProvider unfoldTransitionProgressProvider) {
        return this.delegateFactory.get(unfoldTransitionProgressProvider);
    }

    public static Provider<ScaleAwareTransitionProgressProvider.Factory> create(C0008ScaleAwareTransitionProgressProvider_Factory scaleAwareTransitionProgressProvider_Factory) {
        return InstanceFactory.create(new ScaleAwareTransitionProgressProvider_Factory_Impl(scaleAwareTransitionProgressProvider_Factory));
    }
}
