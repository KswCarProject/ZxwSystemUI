package com.android.systemui.statusbar.phone.ongoingcall;

import com.android.systemui.flags.FeatureFlags;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class OngoingCallFlags_Factory implements Factory<OngoingCallFlags> {
    public final Provider<FeatureFlags> featureFlagsProvider;

    public OngoingCallFlags_Factory(Provider<FeatureFlags> provider) {
        this.featureFlagsProvider = provider;
    }

    public OngoingCallFlags get() {
        return newInstance(this.featureFlagsProvider.get());
    }

    public static OngoingCallFlags_Factory create(Provider<FeatureFlags> provider) {
        return new OngoingCallFlags_Factory(provider);
    }

    public static OngoingCallFlags newInstance(FeatureFlags featureFlags) {
        return new OngoingCallFlags(featureFlags);
    }
}
